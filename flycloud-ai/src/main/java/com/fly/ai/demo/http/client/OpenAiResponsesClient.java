package com.fly.ai.demo.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fly.ai.demo.http.client.tool.OpenAiResponseParser;
import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.common.model.AiStreamEvent;
import com.fly.common.enums.ai.AiProviderEnum;
import com.fly.ai.common.config.AiProperties;
import com.fly.ai.common.model.AiEmbeddingRequest;
import com.fly.ai.common.model.AiEmbeddingResponse;
import com.fly.common.utils.ai.AiUtils;
import com.fly.common.exception.AiProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * OpenAI Responses API 原生调用实现。
 * <p>
 * 不依赖供应商 SDK，完整展示 HTTP 请求、认证头、请求 JSON、响应 JSON 与 SSE 的处理过程。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Slf4j
@Component
public class OpenAiResponsesClient {

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    private final AiProperties properties;

    private final OpenAiResponseParser responseParser;

    private final Executor streamTaskExecutor;

    /**
     * 初始化 OpenAI Responses API 客户端。
     *
     * @param aiHttpClient 模型 HTTP 客户端
     * @param objectMapper JSON 序列化工具
     * @param properties AI 配置
     * @param streamTaskExecutor 流式任务线程池
     */
    public OpenAiResponsesClient(HttpClient aiHttpClient, ObjectMapper objectMapper, AiProperties properties,
            @org.springframework.beans.factory.annotation.Qualifier("aiStreamTaskExecutor") Executor streamTaskExecutor) {
        this.httpClient = aiHttpClient;
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.responseParser = new OpenAiResponseParser(objectMapper);
        this.streamTaskExecutor = streamTaskExecutor;
    }

    /**
     * 调用 OpenAI Responses API 的普通聊天接口。
     *
     * @param request 聊天请求
     * @return 完整聊天响应
     */
    public AiChatResponse chat(AiChatRequest request) {
        validateAvailable();
        ObjectNode body = buildChatBody(request, false);
        String requestBody = AiUtils.toJson(objectMapper, body);
        HttpRequest httpRequest = requestBuilder(properties.getOpenai().getChatPath())
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        AiHttpLog.request(AiProviderEnum.OPENAI.getDisplayName(), httpRequest, requestBody);
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        AiHttpLog.response(objectMapper, AiProviderEnum.OPENAI.getDisplayName(), httpRequest, response.statusCode(), response.body());
        ensureSuccess(response.statusCode(), response.body());
        return responseParser.parseChatResponse(response.body(), resolveChatModel(request.model()));
    }

    /**
     * 调用 OpenAI Responses API 的流式聊天接口。
     *
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    public SseEmitter stream(AiChatRequest request) {
        validateAvailable();
        SseEmitter emitter = new SseEmitter(0L);
        CompletableFuture.runAsync(() -> readStream(request, emitter), streamTaskExecutor);
        return emitter;
    }

    /**
     * 调用 OpenAI Embeddings API 的文本向量化接口。
     *
     * @param request 向量化请求
     * @return 文本向量响应
     */
    public AiEmbeddingResponse embed(AiEmbeddingRequest request) {
        validateAvailable();
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", resolveEmbeddingModel(request.model()));
        body.put("input", request.input());
        String requestBody = AiUtils.toJson(objectMapper, body);
        HttpRequest httpRequest = requestBuilder(properties.getOpenai().getEmbeddingPath())
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        AiHttpLog.request(AiProviderEnum.OPENAI.getDisplayName(), httpRequest, requestBody);
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        AiHttpLog.response(objectMapper, AiProviderEnum.OPENAI.getDisplayName(), httpRequest, response.statusCode(), response.body());
        ensureSuccess(response.statusCode(), response.body());
        return responseParser.parseEmbeddingResponse(response.body(), resolveEmbeddingModel(request.model()));
    }

    /**
     * 在异步线程中读取 OpenAI 流式响应，并转换为统一 SSE 事件。
     *
     * @param request 聊天请求
     * @param emitter 下游 SSE 发送器
     */
    private void readStream(AiChatRequest request, SseEmitter emitter) {
        try {
            ObjectNode body = buildChatBody(request, true);
            String requestBody = AiUtils.toJson(objectMapper, body);
            HttpRequest httpRequest = requestBuilder(properties.getOpenai().getChatPath())
                    .header("Accept", MediaType.TEXT_EVENT_STREAM_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();
            AiHttpLog.request(AiProviderEnum.OPENAI.getDisplayName(), httpRequest, requestBody);
            HttpResponse<java.util.stream.Stream<String>> response = send(httpRequest, HttpResponse.BodyHandlers.ofLines());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String errorBody;
                try (java.util.stream.Stream<String> lines = response.body()) {
                    errorBody = lines.reduce("", (left, right) -> left + right);
                }
                AiHttpLog.response(objectMapper, AiProviderEnum.OPENAI.getDisplayName(), httpRequest, response.statusCode(), errorBody);
                ensureSuccess(response.statusCode(), errorBody);
            }
            try (java.util.stream.Stream<String> lines = response.body()) {
                List<String> responseEvents = new ArrayList<>();
                lines.filter(line -> line.startsWith("data:"))
                        .map(line -> line.substring("data:".length()).trim())
                        .filter(data -> !data.isBlank() && !"[DONE]".equals(data))
                        .forEach(data -> {
                            responseEvents.add(data);
                            responseParser.parseStreamEvent(data).ifPresent(event -> sendEvent(emitter, event));
                        });
                AiHttpLog.streamResponse(objectMapper, AiProviderEnum.OPENAI.getDisplayName(), httpRequest,
                        response.statusCode(), responseEvents);
            }
            emitter.complete();
        } catch (Exception exception) {
            String message = AiUtils.providerMessage(exception, "AI 流式响应处理失败");
            log.error("AI 流式调用失败: {}", message, exception);
            try {
                sendEvent(emitter, AiStreamEvent.error(message));
                emitter.complete();
            } catch (Exception sendException) {
                emitter.completeWithError(sendException);
            }
        }
    }

    /**
     * 向下游客户端发送统一格式的 SSE 事件。
     *
     * @param emitter SSE 发送器
     * @param event 事件内容
     */
    private void sendEvent(SseEmitter emitter, AiStreamEvent event) {
        try {
            emitter.send(SseEmitter.event().name(event.type()).data(event, MediaType.APPLICATION_JSON));
        } catch (IOException exception) {
            throw new AiProviderException(499, "客户端已断开 AI 流式连接", exception);
        }
    }

    /**
     * 构造 OpenAI Responses API 请求体。
     *
     * @param request 聊天请求
     * @param stream 是否启用流式输出
     * @return 请求 JSON 对象
     */
    private ObjectNode buildChatBody(AiChatRequest request, boolean stream) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", resolveChatModel(request.model()));
        if (AiUtils.hasText(properties.getSystemPrompt())) {
            body.put("instructions", properties.getSystemPrompt());
        }
        ArrayNode input = body.putArray("input");
        ObjectNode userMessage = input.addObject();
        userMessage.put("role", "user");
        ArrayNode content = userMessage.putArray("content");
        content.addObject().put("type", "input_text").put("text", request.message());
        body.put("stream", stream);
        body.put("max_output_tokens", resolveMaxOutputTokens(request.maxOutputTokens()));
        return body;
    }

    /**
     * 创建包含 OpenAI 认证和超时配置的 HTTP 请求构造器。
     *
     * @param path 接口路径
     * @return HTTP 请求构造器
     */
    private HttpRequest.Builder requestBuilder(String path) {
        return HttpRequest.newBuilder(AiUtils.resolveUri(properties.getOpenai().getBaseUrl(), path))
                .timeout(properties.getOpenai().getResponseTimeout())
                .header("Authorization", "Bearer " + properties.getOpenai().getApiKey())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 发送 HTTP 请求，并将网络异常转换为统一异常。
     *
     * @param request HTTP 请求
     * @param bodyHandler 响应体处理器
     * @param <T> 响应体类型
     * @return HTTP 响应
     */
    private <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) {
        try {
            return httpClient.send(request, bodyHandler);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AiProviderException(503, "AI 服务调用被中断", exception);
        } catch (IOException exception) {
            throw new AiProviderException(503, "无法连接 AI 模型服务，请检查网络和服务地址", exception);
        }
    }

    /**
     * 校验上游 HTTP 状态码，非成功状态转换为供应商异常。
     *
     * @param statusCode HTTP 状态码
     * @param responseBody 上游响应内容
     */
    private void ensureSuccess(int statusCode, String responseBody) {
        if (statusCode >= 200 && statusCode < 300) {
            return;
        }
        throw new AiProviderException(statusCode, responseParser.parseErrorMessage(responseBody));
    }

    /**
     * 校验 AI 服务和 OpenAI 配置是否可用。
     */
    private void validateAvailable() {
        AiUtils.requireServiceEnabled(properties.isEnabled());
        if (properties.getProvider() != AiProviderEnum.OPENAI) {
            throw new AiProviderException(503, "当前版本仅支持 " + AiProviderEnum.OPENAI.getValue()
                    + " provider，实际配置为：" + properties.getProvider());
        }
        if (!AiUtils.hasText(properties.getOpenai().getApiKey())) {
            throw new AiProviderException(503, "未配置 AI API Key，请在 Nacos 设置 OPENAI_API_KEY 环境变量或 flycloud.ai.openai.api-key");
        }
    }

    /**
     * 选择调用时指定的聊天模型，未指定时回退到默认模型。
     *
     * @param model 请求指定模型
     * @return 实际使用的聊天模型
     */
    private String resolveChatModel(String model) {
        return AiUtils.hasText(model) ? model : properties.getOpenai().getChatModel();
    }

    /**
     * 选择调用时指定的向量模型，未指定时回退到默认模型。
     *
     * @param model 请求指定模型
     * @return 实际使用的向量模型
     */
    private String resolveEmbeddingModel(String model) {
        return AiUtils.hasText(model) ? model : properties.getOpenai().getEmbeddingModel();
    }

    /**
     * 选择调用时指定的最大输出 Token，未指定时回退到默认配置。
     *
     * @param maxOutputTokens 请求指定的最大输出 Token
     * @return 实际使用的最大输出 Token
     */
    private int resolveMaxOutputTokens(Integer maxOutputTokens) {
        return maxOutputTokens == null ? properties.getMaxOutputTokens() : maxOutputTokens;
    }

}

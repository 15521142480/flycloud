package com.fly.ai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fly.ai.client.tool.OpenAiResponseParser;
import com.fly.ai.model.AiChatRequest;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.model.AiStreamEvent;
import com.fly.ai.config.AiProperties;
import com.fly.ai.model.AiEmbeddingRequest;
import com.fly.ai.model.AiEmbeddingResponse;
import com.fly.common.exception.AiProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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
        String requestBody = toJson(body);
        HttpRequest httpRequest = requestBuilder(properties.getOpenai().getChatPath())
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        AiHttpLog.request("OpenAI", httpRequest, requestBody);
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        AiHttpLog.response("OpenAI", httpRequest, response.statusCode(), response.body());
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
        String requestBody = toJson(body);
        HttpRequest httpRequest = requestBuilder(properties.getOpenai().getEmbeddingPath())
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        AiHttpLog.request("OpenAI", httpRequest, requestBody);
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        AiHttpLog.response("OpenAI", httpRequest, response.statusCode(), response.body());
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
            String requestBody = toJson(body);
            HttpRequest httpRequest = requestBuilder(properties.getOpenai().getChatPath())
                    .header("Accept", MediaType.TEXT_EVENT_STREAM_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();
            AiHttpLog.request("OpenAI", httpRequest, requestBody);
            HttpResponse<java.util.stream.Stream<String>> response = send(httpRequest, HttpResponse.BodyHandlers.ofLines());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String errorBody;
                try (java.util.stream.Stream<String> lines = response.body()) {
                    errorBody = lines.reduce("", (left, right) -> left + right);
                }
                AiHttpLog.response("OpenAI", httpRequest, response.statusCode(), errorBody);
                ensureSuccess(response.statusCode(), errorBody);
            }
            try (java.util.stream.Stream<String> lines = response.body()) {
                StringBuilder responsePreview = new StringBuilder();
                lines.filter(line -> line.startsWith("data:"))
                        .map(line -> line.substring("data:".length()).trim())
                        .filter(data -> !data.isBlank() && !"[DONE]".equals(data))
                        .forEach(data -> {
                            appendPreview(responsePreview, data);
                            responseParser.parseStreamEvent(data).ifPresent(event -> sendEvent(emitter, event));
                        });
                AiHttpLog.response("OpenAI", httpRequest, response.statusCode(), responsePreview.toString());
            }
            emitter.complete();
        } catch (Exception exception) {
            String message = providerMessage(exception);
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
     * 将流式数据追加到日志预览缓冲区，最多保留前 50 个字符。
     *
     * @param responsePreview 响应预览缓冲区
     * @param content 当前 SSE 数据
     */
    private void appendPreview(StringBuilder responsePreview, String content) {
        int remaining = 50 - responsePreview.length();
        if (remaining > 0) {
            responsePreview.append(content, 0, Math.min(remaining, content.length()));
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
        if (hasText(properties.getSystemPrompt())) {
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
        return HttpRequest.newBuilder(resolveUri(path))
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
        if (!properties.isEnabled()) {
            throw new AiProviderException(503, "AI 服务当前已关闭");
        }
        if (!"openai".equalsIgnoreCase(properties.getProvider())) {
            throw new AiProviderException(503, "当前版本仅支持 openai provider，实际配置为：" + properties.getProvider());
        }
        if (!hasText(properties.getOpenai().getApiKey())) {
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
        return hasText(model) ? model : properties.getOpenai().getChatModel();
    }

    /**
     * 选择调用时指定的向量模型，未指定时回退到默认模型。
     *
     * @param model 请求指定模型
     * @return 实际使用的向量模型
     */
    private String resolveEmbeddingModel(String model) {
        return hasText(model) ? model : properties.getOpenai().getEmbeddingModel();
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

    /**
     * 拼接 OpenAI 服务地址和接口路径。
     *
     * @param path 接口路径
     * @return 完整请求 URI
     */
    private URI resolveUri(String path) {
        String baseUrl = properties.getOpenai().getBaseUrl().replaceAll("/+$", "");
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        return URI.create(baseUrl + normalizedPath);
    }

    /**
     * 将 JSON 对象序列化为请求字符串。
     *
     * @param body JSON 对象
     * @return JSON 字符串
     */
    private String toJson(ObjectNode body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (Exception exception) {
            throw new AiProviderException(500, "AI 请求 JSON 序列化失败", exception);
        }
    }

    /**
     * 提取适合返回给客户端的流式调用失败说明。
     *
     * @param exception 异常对象
     * @return 错误说明
     */
    private String providerMessage(Exception exception) {
        if (exception instanceof AiProviderException providerException) {
            return providerException.getMessage();
        }
        return "AI 流式响应处理失败";
    }

    /**
     * 判断字符串是否包含非空白字符。
     *
     * @param value 待判断字符串
     * @return 存在有效内容时返回 {@code true}
     */
    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}

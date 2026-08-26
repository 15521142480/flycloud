package com.fly.ai.original.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fly.ai.original.client.tool.ChatCompletionsResponseParser;
import com.fly.ai.model.AiChatRequest;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.model.AiStreamEvent;
import com.fly.ai.original.config.AiProperties;
import com.fly.ai.model.AiEmbeddingRequest;
import com.fly.ai.model.AiEmbeddingResponse;
import com.fly.common.exception.AiProviderException;
import com.fly.ai.utils.AiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Chat Completions 协议的公共 HTTP 实现。
 * <p>
 * 该类只复用 HTTP、JSON 和 SSE 协议细节。每个供应商必须拥有自己的客户端类和配置对象。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Slf4j
public abstract class AbstractChatCompletionsClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final AiProperties properties;
    private final ChatCompletionsResponseParser responseParser;
    private final Executor streamTaskExecutor;

    /**
     * 初始化 Chat Completions 协议所需的公共依赖。
     *
     * @param httpClient 模型 HTTP 客户端
     * @param objectMapper JSON 序列化工具
     * @param properties AI 配置
     * @param streamTaskExecutor 流式任务线程池
     */
    protected AbstractChatCompletionsClient(HttpClient httpClient, ObjectMapper objectMapper, AiProperties properties,
            Executor streamTaskExecutor) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.responseParser = new ChatCompletionsResponseParser(objectMapper);
        this.streamTaskExecutor = streamTaskExecutor;
    }

    /**
     * 调用兼容 Chat Completions 协议的普通聊天接口。
     *
     * @param providerName 供应商名称
     * @param baseUrl 服务地址
     * @param apiKey API Key
     * @param chatModel 默认聊天模型
     * @param chatPath 聊天接口路径
     * @param responseTimeout 响应超时时间
     * @param request 聊天请求
     * @return 完整聊天响应
     */
    protected AiChatResponse chat(String providerName, String baseUrl, String apiKey, String chatModel, String chatPath,
            Duration responseTimeout, AiChatRequest request) {
        validateProvider(providerName, baseUrl, apiKey, chatModel);
        ObjectNode body = buildChatBody(chatModel, request, false);
        String requestBody = AiUtils.toJson(objectMapper, body);
        HttpRequest httpRequest = requestBuilder(baseUrl, apiKey, chatPath, responseTimeout)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        AiHttpLog.request(providerName, httpRequest, requestBody);
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        AiHttpLog.response(objectMapper, providerName, httpRequest, response.statusCode(), response.body());
        ensureSuccess(response.statusCode(), response.body());
        return responseParser.parseChatResponse(response.body(), resolveModel(chatModel, request.model()));
    }

    /**
     * 调用兼容 Chat Completions 协议的流式聊天接口。
     *
     * @param providerName 供应商名称
     * @param baseUrl 服务地址
     * @param apiKey API Key
     * @param chatModel 默认聊天模型
     * @param chatPath 聊天接口路径
     * @param responseTimeout 响应超时时间
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    protected SseEmitter stream(String providerName, String baseUrl, String apiKey, String chatModel, String chatPath,
            Duration responseTimeout, AiChatRequest request) {
        validateProvider(providerName, baseUrl, apiKey, chatModel);
        SseEmitter emitter = new SseEmitter(0L);
        CompletableFuture.runAsync(() -> readStream(providerName, baseUrl, apiKey, chatModel, chatPath, responseTimeout, request, emitter),
                streamTaskExecutor);
        return emitter;
    }

    /**
     * 调用兼容 Chat Completions 协议的文本向量化接口。
     *
     * @param providerName 供应商名称
     * @param baseUrl 服务地址
     * @param apiKey API Key
     * @param chatModel 聊天模型，用于校验供应商基础配置
     * @param embeddingModel 默认向量模型
     * @param embeddingPath 向量接口路径
     * @param responseTimeout 响应超时时间
     * @param request 向量化请求
     * @return 文本向量响应
     */
    protected AiEmbeddingResponse embed(String providerName, String baseUrl, String apiKey, String chatModel,
            String embeddingModel, String embeddingPath, Duration responseTimeout, AiEmbeddingRequest request) {
        validateProvider(providerName, baseUrl, apiKey, chatModel);
        if (!AiUtils.hasText(embeddingModel)) {
            throw new AiProviderException(400, providerName + " 未配置 Embedding 模型");
        }
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", resolveModel(embeddingModel, request.model()));
        body.put("input", request.input());
        String requestBody = AiUtils.toJson(objectMapper, body);
        HttpRequest httpRequest = requestBuilder(baseUrl, apiKey, embeddingPath, responseTimeout)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        AiHttpLog.request(providerName, httpRequest, requestBody);
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        AiHttpLog.response(objectMapper, providerName, httpRequest, response.statusCode(), response.body());
        ensureSuccess(response.statusCode(), response.body());
        return responseParser.parseEmbeddingResponse(response.body(), resolveModel(embeddingModel, request.model()));
    }

    /**
     * 在异步线程中读取上游 SSE，并转换为本服务统一事件。
     *
     * @param providerName 供应商名称
     * @param baseUrl 服务地址
     * @param apiKey API Key
     * @param chatModel 默认聊天模型
     * @param chatPath 聊天接口路径
     * @param responseTimeout 响应超时时间
     * @param request 聊天请求
     * @param emitter 下游 SSE 发送器
     */
    private void readStream(String providerName, String baseUrl, String apiKey, String chatModel, String chatPath,
            Duration responseTimeout,
            AiChatRequest request, SseEmitter emitter) {
        try {
            ObjectNode body = buildChatBody(chatModel, request, true);
            String requestBody = AiUtils.toJson(objectMapper, body);
            HttpRequest httpRequest = requestBuilder(baseUrl, apiKey, chatPath, responseTimeout)
                    .header("Accept", MediaType.TEXT_EVENT_STREAM_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();
            AiHttpLog.request(providerName, httpRequest, requestBody);
            HttpResponse<java.util.stream.Stream<String>> response = send(httpRequest, HttpResponse.BodyHandlers.ofLines());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String errorBody;
                try (java.util.stream.Stream<String> lines = response.body()) {
                    errorBody = lines.reduce("", (left, right) -> left + right);
                }
                AiHttpLog.response(objectMapper, providerName, httpRequest, response.statusCode(), errorBody);
                ensureSuccess(response.statusCode(), errorBody);
            }
            try (java.util.stream.Stream<String> lines = response.body()) {
                AtomicBoolean completed = new AtomicBoolean(false);
                List<String> responseEvents = new ArrayList<>();
                lines.filter(line -> line.startsWith("data:"))
                        .map(line -> line.substring("data:".length()).trim())
                        .filter(data -> !data.isBlank() && !"[DONE]".equals(data))
                        .forEach(data -> {
                            responseEvents.add(data);
                            responseParser.parseStreamEvent(data).ifPresent(event -> {
                                if ("completed".equals(event.type())) {
                                    completed.set(true);
                                }
                                sendEvent(emitter, event);
                            });
                        });
                if (!completed.get()) {
                    sendEvent(emitter, AiStreamEvent.completed(null, null));
                }
                AiHttpLog.streamResponse(objectMapper, providerName, httpRequest, response.statusCode(), responseEvents);
            }
            emitter.complete();
        } catch (Exception exception) {
            String message = AiUtils.providerMessage(exception, "AI 流式响应处理失败");
            log.error("Chat Completions 流式调用失败: {}", message, exception);
            try {
                sendEvent(emitter, AiStreamEvent.error(message));
                emitter.complete();
            } catch (Exception sendException) {
                emitter.completeWithError(sendException);
            }
        }
    }

    /**
     * 构造 Chat Completions 协议请求体。
     *
     * @param defaultChatModel 默认聊天模型
     * @param request 聊天请求
     * @param stream 是否启用流式输出
     * @return 请求 JSON 对象
     */
    private ObjectNode buildChatBody(String defaultChatModel, AiChatRequest request, boolean stream) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", resolveModel(defaultChatModel, request.model()));
        ArrayNode messages = body.putArray("messages");
        if (AiUtils.hasText(properties.getSystemPrompt())) {
            messages.addObject().put("role", "system").put("content", properties.getSystemPrompt());
        }
        messages.addObject().put("role", "user").put("content", request.message());
        body.put("stream", stream);
        body.put("max_tokens", resolveMaxOutputTokens(request.maxOutputTokens()));
        return body;
    }

    /**
     * 创建包含认证和超时配置的 HTTP 请求构造器。
     *
     * @param baseUrl 服务地址
     * @param apiKey API Key
     * @param path 接口路径
     * @param responseTimeout 响应超时时间
     * @return HTTP 请求构造器
     */
    private HttpRequest.Builder requestBuilder(String baseUrl, String apiKey, String path, Duration responseTimeout) {
        return HttpRequest.newBuilder(AiUtils.resolveUri(baseUrl, path))
                .timeout(responseTimeout)
                .header("Authorization", "Bearer " + apiKey)
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
     * 校验供应商是否已启用且基础配置完整。
     *
     * @param providerName 供应商名称
     * @param baseUrl 服务地址
     * @param apiKey API Key
     * @param chatModel 聊天模型
     */
    private void validateProvider(String providerName, String baseUrl, String apiKey, String chatModel) {
        AiUtils.requireServiceEnabled(properties.isEnabled());
        if (!AiUtils.hasText(apiKey)) {
            throw new AiProviderException(503, "未配置 " + providerName + " API Key，请在运行环境设置对应环境变量或 Nacos 配置");
        }
        if (!AiUtils.hasText(baseUrl) || !AiUtils.hasText(chatModel)) {
            throw new AiProviderException(503, providerName + " 的服务地址或聊天模型未配置");
        }
    }

    /**
     * 选择调用时指定的模型，未指定时回退到默认模型。
     *
     * @param defaultModel 默认模型
     * @param model 请求指定模型
     * @return 实际使用的模型
     */
    private String resolveModel(String defaultModel, String model) {
        return AiUtils.hasText(model) ? model : defaultModel;
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

}

package com.fly.ai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fly.ai.client.tool.ChatCompletionsResponseParser;
import com.fly.ai.model.AiChatRequest;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.model.AiStreamEvent;
import com.fly.ai.config.AiProperties;
import com.fly.ai.model.AiEmbeddingRequest;
import com.fly.ai.model.AiEmbeddingResponse;
import com.fly.common.exception.AiProviderException;
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

    protected AbstractChatCompletionsClient(HttpClient httpClient, ObjectMapper objectMapper, AiProperties properties,
            Executor streamTaskExecutor) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.responseParser = new ChatCompletionsResponseParser(objectMapper);
        this.streamTaskExecutor = streamTaskExecutor;
    }

    protected AiChatResponse chat(String providerName, String baseUrl, String apiKey, String chatModel, String chatPath,
            Duration responseTimeout, AiChatRequest request) {
        validateProvider(providerName, baseUrl, apiKey, chatModel);
        ObjectNode body = buildChatBody(chatModel, request, false);
        HttpRequest httpRequest = requestBuilder(baseUrl, apiKey, chatPath, responseTimeout)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(toJson(body), StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        ensureSuccess(response.statusCode(), response.body());
        return responseParser.parseChatResponse(response.body(), resolveModel(chatModel, request.model()));
    }

    protected SseEmitter stream(String providerName, String baseUrl, String apiKey, String chatModel, String chatPath,
            Duration responseTimeout, AiChatRequest request) {
        validateProvider(providerName, baseUrl, apiKey, chatModel);
        SseEmitter emitter = new SseEmitter(0L);
        CompletableFuture.runAsync(() -> readStream(baseUrl, apiKey, chatModel, chatPath, responseTimeout, request, emitter),
                streamTaskExecutor);
        return emitter;
    }

    protected AiEmbeddingResponse embed(String providerName, String baseUrl, String apiKey, String chatModel,
            String embeddingModel, String embeddingPath, Duration responseTimeout, AiEmbeddingRequest request) {
        validateProvider(providerName, baseUrl, apiKey, chatModel);
        if (!hasText(embeddingModel)) {
            throw new AiProviderException(400, providerName + " 未配置 Embedding 模型");
        }
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", resolveModel(embeddingModel, request.model()));
        body.put("input", request.input());
        HttpRequest httpRequest = requestBuilder(baseUrl, apiKey, embeddingPath, responseTimeout)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(toJson(body), StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        ensureSuccess(response.statusCode(), response.body());
        return responseParser.parseEmbeddingResponse(response.body(), resolveModel(embeddingModel, request.model()));
    }

    private void readStream(String baseUrl, String apiKey, String chatModel, String chatPath, Duration responseTimeout,
            AiChatRequest request, SseEmitter emitter) {
        try {
            ObjectNode body = buildChatBody(chatModel, request, true);
            HttpRequest httpRequest = requestBuilder(baseUrl, apiKey, chatPath, responseTimeout)
                    .header("Accept", MediaType.TEXT_EVENT_STREAM_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(toJson(body), StandardCharsets.UTF_8))
                    .build();
            HttpResponse<java.util.stream.Stream<String>> response = send(httpRequest, HttpResponse.BodyHandlers.ofLines());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String errorBody;
                try (java.util.stream.Stream<String> lines = response.body()) {
                    errorBody = lines.reduce("", (left, right) -> left + right);
                }
                ensureSuccess(response.statusCode(), errorBody);
            }
            try (java.util.stream.Stream<String> lines = response.body()) {
                AtomicBoolean completed = new AtomicBoolean(false);
                lines.filter(line -> line.startsWith("data:"))
                        .map(line -> line.substring("data:".length()).trim())
                        .filter(data -> !data.isBlank() && !"[DONE]".equals(data))
                        .forEach(data -> responseParser.parseStreamEvent(data).ifPresent(event -> {
                            if ("completed".equals(event.type())) {
                                completed.set(true);
                            }
                            sendEvent(emitter, event);
                        }));
                if (!completed.get()) {
                    sendEvent(emitter, AiStreamEvent.completed(null, null));
                }
            }
            emitter.complete();
        } catch (Exception exception) {
            String message = providerMessage(exception);
            log.warn("Chat Completions 流式调用失败: {}", message);
            try {
                sendEvent(emitter, AiStreamEvent.error(message));
                emitter.complete();
            } catch (Exception sendException) {
                emitter.completeWithError(sendException);
            }
        }
    }

    private ObjectNode buildChatBody(String defaultChatModel, AiChatRequest request, boolean stream) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", resolveModel(defaultChatModel, request.model()));
        ArrayNode messages = body.putArray("messages");
        if (hasText(properties.getSystemPrompt())) {
            messages.addObject().put("role", "system").put("content", properties.getSystemPrompt());
        }
        messages.addObject().put("role", "user").put("content", request.message());
        body.put("stream", stream);
        body.put("max_tokens", resolveMaxOutputTokens(request.maxOutputTokens()));
        return body;
    }

    private HttpRequest.Builder requestBuilder(String baseUrl, String apiKey, String path, Duration responseTimeout) {
        return HttpRequest.newBuilder(resolveUri(baseUrl, path))
                .timeout(responseTimeout)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    }

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

    private void ensureSuccess(int statusCode, String responseBody) {
        if (statusCode >= 200 && statusCode < 300) {
            return;
        }
        throw new AiProviderException(statusCode, responseParser.parseErrorMessage(responseBody));
    }

    private void validateProvider(String providerName, String baseUrl, String apiKey, String chatModel) {
        if (!properties.isEnabled()) {
            throw new AiProviderException(503, "AI 服务当前已关闭");
        }
        if (!hasText(apiKey)) {
            throw new AiProviderException(503, "未配置 " + providerName + " API Key，请在运行环境设置对应环境变量或 Nacos 配置");
        }
        if (!hasText(baseUrl) || !hasText(chatModel)) {
            throw new AiProviderException(503, providerName + " 的服务地址或聊天模型未配置");
        }
    }

    private String resolveModel(String defaultModel, String model) {
        return hasText(model) ? model : defaultModel;
    }

    private int resolveMaxOutputTokens(Integer maxOutputTokens) {
        return maxOutputTokens == null ? properties.getMaxOutputTokens() : maxOutputTokens;
    }

    private URI resolveUri(String baseUrl, String path) {
        String normalizedBaseUrl = baseUrl.replaceAll("/+$", "");
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        return URI.create(normalizedBaseUrl + normalizedPath);
    }

    private String toJson(ObjectNode body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (Exception exception) {
            throw new AiProviderException(500, "AI 请求 JSON 序列化失败", exception);
        }
    }

    private void sendEvent(SseEmitter emitter, AiStreamEvent event) {
        try {
            emitter.send(SseEmitter.event().name(event.type()).data(event, MediaType.APPLICATION_JSON));
        } catch (IOException exception) {
            throw new AiProviderException(499, "客户端已断开 AI 流式连接", exception);
        }
    }

    private String providerMessage(Exception exception) {
        if (exception instanceof AiProviderException providerException) {
            return providerException.getMessage();
        }
        return "AI 流式响应处理失败";
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}

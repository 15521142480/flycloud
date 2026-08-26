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

    public OpenAiResponsesClient(HttpClient aiHttpClient, ObjectMapper objectMapper, AiProperties properties,
            @org.springframework.beans.factory.annotation.Qualifier("aiStreamTaskExecutor") Executor streamTaskExecutor) {
        this.httpClient = aiHttpClient;
        this.objectMapper = objectMapper;
        this.properties = properties;
        this.responseParser = new OpenAiResponseParser(objectMapper);
        this.streamTaskExecutor = streamTaskExecutor;
    }

    public AiChatResponse chat(AiChatRequest request) {
        validateAvailable();
        ObjectNode body = buildChatBody(request, false);
        HttpRequest httpRequest = requestBuilder(properties.getOpenai().getChatPath())
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(toJson(body), StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        ensureSuccess(response.statusCode(), response.body());
        return responseParser.parseChatResponse(response.body(), resolveChatModel(request.model()));
    }

    public SseEmitter stream(AiChatRequest request) {
        validateAvailable();
        SseEmitter emitter = new SseEmitter(0L);
        CompletableFuture.runAsync(() -> readStream(request, emitter), streamTaskExecutor);
        return emitter;
    }

    public AiEmbeddingResponse embed(AiEmbeddingRequest request) {
        validateAvailable();
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", resolveEmbeddingModel(request.model()));
        body.put("input", request.input());
        HttpRequest httpRequest = requestBuilder(properties.getOpenai().getEmbeddingPath())
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(toJson(body), StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        ensureSuccess(response.statusCode(), response.body());
        return responseParser.parseEmbeddingResponse(response.body(), resolveEmbeddingModel(request.model()));
    }

    private void readStream(AiChatRequest request, SseEmitter emitter) {
        try {
            ObjectNode body = buildChatBody(request, true);
            HttpRequest httpRequest = requestBuilder(properties.getOpenai().getChatPath())
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
                lines.filter(line -> line.startsWith("data:"))
                        .map(line -> line.substring("data:".length()).trim())
                        .filter(data -> !data.isBlank() && !"[DONE]".equals(data))
                        .forEach(data -> responseParser.parseStreamEvent(data).ifPresent(event -> sendEvent(emitter, event)));
            }
            emitter.complete();
        } catch (Exception exception) {
            String message = providerMessage(exception);
            log.warn("AI 流式调用失败: {}", message);
            try {
                sendEvent(emitter, AiStreamEvent.error(message));
                emitter.complete();
            } catch (Exception sendException) {
                emitter.completeWithError(sendException);
            }
        }
    }

    private void sendEvent(SseEmitter emitter, AiStreamEvent event) {
        try {
            emitter.send(SseEmitter.event().name(event.type()).data(event, MediaType.APPLICATION_JSON));
        } catch (IOException exception) {
            throw new AiProviderException(499, "客户端已断开 AI 流式连接", exception);
        }
    }

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

    private HttpRequest.Builder requestBuilder(String path) {
        return HttpRequest.newBuilder(resolveUri(path))
                .timeout(properties.getOpenai().getResponseTimeout())
                .header("Authorization", "Bearer " + properties.getOpenai().getApiKey())
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

    private String resolveChatModel(String model) {
        return hasText(model) ? model : properties.getOpenai().getChatModel();
    }

    private String resolveEmbeddingModel(String model) {
        return hasText(model) ? model : properties.getOpenai().getEmbeddingModel();
    }

    private int resolveMaxOutputTokens(Integer maxOutputTokens) {
        return maxOutputTokens == null ? properties.getMaxOutputTokens() : maxOutputTokens;
    }

    private URI resolveUri(String path) {
        String baseUrl = properties.getOpenai().getBaseUrl().replaceAll("/+$", "");
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        return URI.create(baseUrl + normalizedPath);
    }

    private String toJson(ObjectNode body) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (Exception exception) {
            throw new AiProviderException(500, "AI 请求 JSON 序列化失败", exception);
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

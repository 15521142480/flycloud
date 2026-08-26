package com.fly.ai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.ai.model.AiChatRequest;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.config.AiProperties;
import com.fly.ai.model.AiEmbeddingRequest;
import com.fly.ai.model.AiEmbeddingResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.http.HttpClient;
import java.util.concurrent.Executor;

/**
 * 阿里云百炼（通义千问）原生客户端。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Component
public class DashscopeClient extends AbstractChatCompletionsClient {

    private final AiProperties properties;

    public DashscopeClient(HttpClient aiHttpClient, ObjectMapper objectMapper, AiProperties properties,
            @org.springframework.beans.factory.annotation.Qualifier("aiStreamTaskExecutor") Executor streamTaskExecutor) {
        super(aiHttpClient, objectMapper, properties, streamTaskExecutor);
        this.properties = properties;
    }

    public AiChatResponse chat(AiChatRequest request) {
        AiProperties.Dashscope dashscope = properties.getDashscope();
        return chat("阿里云百炼", dashscope.getBaseUrl(), dashscope.getApiKey(), dashscope.getChatModel(), dashscope.getChatPath(),
                dashscope.getResponseTimeout(), request);
    }

    public SseEmitter stream(AiChatRequest request) {
        AiProperties.Dashscope dashscope = properties.getDashscope();
        return stream("阿里云百炼", dashscope.getBaseUrl(), dashscope.getApiKey(), dashscope.getChatModel(), dashscope.getChatPath(),
                dashscope.getResponseTimeout(), request);
    }

    public AiEmbeddingResponse embed(AiEmbeddingRequest request) {
        AiProperties.Dashscope dashscope = properties.getDashscope();
        return embed("阿里云百炼", dashscope.getBaseUrl(), dashscope.getApiKey(), dashscope.getChatModel(),
                dashscope.getEmbeddingModel(), dashscope.getEmbeddingPath(), dashscope.getResponseTimeout(), request);
    }

}

package com.fly.ai.service;

import com.fly.ai.client.DashscopeClient;
import com.fly.ai.client.DeepseekClient;
import com.fly.ai.client.OpenAiResponsesClient;
import com.fly.ai.config.AiProperties;
import com.fly.ai.model.AiChatRequest;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.model.AiEmbeddingRequest;
import com.fly.ai.model.AiEmbeddingResponse;
import com.fly.common.exception.AiProviderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * AI 模型供应商路由。
 * <p>
 * 只负责根据配置选择供应商；每个供应商由自己的客户端完成实际调用。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Component
@RequiredArgsConstructor
public class AiModelProviderRouter {

    private final AiProperties properties;
    private final OpenAiResponsesClient openAiResponsesClient;
    private final DeepseekClient deepseekClient;
    private final DashscopeClient dashscopeClient;

    public AiChatResponse chat(AiChatRequest request) {
        return switch (provider()) {
            case "openai" -> openAiResponsesClient.chat(request);
            case "deepseek" -> deepseekClient.chat(request);
            case "dashscope" -> dashscopeClient.chat(request);
            default -> throw unsupportedProvider();
        };
    }

    public SseEmitter stream(AiChatRequest request) {
        return switch (provider()) {
            case "openai" -> openAiResponsesClient.stream(request);
            case "deepseek" -> deepseekClient.stream(request);
            case "dashscope" -> dashscopeClient.stream(request);
            default -> throw unsupportedProvider();
        };
    }

    public AiEmbeddingResponse embed(AiEmbeddingRequest request) {
        return switch (provider()) {
            case "openai" -> openAiResponsesClient.embed(request);
            case "dashscope" -> dashscopeClient.embed(request);
            case "deepseek" -> throw new AiProviderException(400, "DeepSeek 当前不提供本项目使用的 Embedding 接口，请切换到 dashscope 或 openai");
            default -> throw unsupportedProvider();
        };
    }

    private String provider() {
        return properties.getProvider() == null ? "" : properties.getProvider().trim().toLowerCase();
    }

    private AiProviderException unsupportedProvider() {
        return new AiProviderException(400, "不支持的 AI provider：" + properties.getProvider() + "；可选 openai、deepseek、dashscope");
    }

}

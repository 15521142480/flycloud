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

    /**
     * 按当前 provider 路由普通聊天请求。
     *
     * @param request 聊天请求
     * @return 完整聊天响应
     */
    public AiChatResponse chat(AiChatRequest request) {
        return switch (provider()) {
            case "openai" -> openAiResponsesClient.chat(request);
            case "deepseek" -> deepseekClient.chat(request);
            case "dashscope" -> dashscopeClient.chat(request);
            default -> throw unsupportedProvider();
        };
    }

    /**
     * 按当前 provider 路由流式聊天请求。
     *
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    public SseEmitter stream(AiChatRequest request) {
        return switch (provider()) {
            case "openai" -> openAiResponsesClient.stream(request);
            case "deepseek" -> deepseekClient.stream(request);
            case "dashscope" -> dashscopeClient.stream(request);
            default -> throw unsupportedProvider();
        };
    }

    /**
     * 按当前 provider 路由文本向量化请求。
     *
     * @param request 向量化请求
     * @return 文本向量响应
     */
    public AiEmbeddingResponse embed(AiEmbeddingRequest request) {
        return switch (provider()) {
            case "openai" -> openAiResponsesClient.embed(request);
            case "dashscope" -> dashscopeClient.embed(request);
            case "deepseek" -> throw new AiProviderException(400, "DeepSeek 当前不提供本项目使用的 Embedding 接口，请切换到 dashscope 或 openai");
            default -> throw unsupportedProvider();
        };
    }

    /**
     * 获取标准化后的供应商标识。
     *
     * @return 小写且去除空白后的 provider；未配置时返回空字符串
     */
    private String provider() {
        return properties.getProvider() == null ? "" : properties.getProvider().trim().toLowerCase();
    }

    /**
     * 构造不支持供应商时的异常。
     *
     * @return 供应商配置异常
     */
    private AiProviderException unsupportedProvider() {
        return new AiProviderException(400, "不支持的 AI provider：" + properties.getProvider() + "；可选 openai、deepseek、dashscope");
    }

}

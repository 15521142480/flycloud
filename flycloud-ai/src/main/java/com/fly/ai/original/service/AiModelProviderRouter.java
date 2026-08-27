package com.fly.ai.original.service;

import com.fly.ai.original.client.DashscopeClient;
import com.fly.ai.original.client.DeepseekClient;
import com.fly.ai.original.client.OpenAiResponsesClient;
import com.fly.ai.original.config.AiProperties;
import com.fly.common.enums.ai.AiProviderEnum;
import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.ai.common.model.AiEmbeddingRequest;
import com.fly.ai.common.model.AiEmbeddingResponse;
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
        return switch (properties.getProvider()) {
            case OPENAI -> openAiResponsesClient.chat(request);
            case DEEPSEEK -> deepseekClient.chat(request);
            case DASHSCOPE -> dashscopeClient.chat(request);
        };
    }

    /**
     * 按当前 provider 路由流式聊天请求。
     *
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    public SseEmitter stream(AiChatRequest request) {
        return switch (properties.getProvider()) {
            case OPENAI -> openAiResponsesClient.stream(request);
            case DEEPSEEK -> deepseekClient.stream(request);
            case DASHSCOPE -> dashscopeClient.stream(request);
        };
    }

    /**
     * 按当前 provider 路由文本向量化请求。
     *
     * @param request 向量化请求
     * @return 文本向量响应
     */
    public AiEmbeddingResponse embed(AiEmbeddingRequest request) {
        return switch (properties.getProvider()) {
            case OPENAI -> openAiResponsesClient.embed(request);
            case DASHSCOPE -> dashscopeClient.embed(request);
            case DEEPSEEK -> throw new AiProviderException(400, AiProviderEnum.DEEPSEEK.getDisplayName()
                    + " 当前不提供本项目使用的 Embedding 接口，请切换到 " + AiProviderEnum.DASHSCOPE.getValue()
                    + " 或 " + AiProviderEnum.OPENAI.getValue());
        };
    }

}

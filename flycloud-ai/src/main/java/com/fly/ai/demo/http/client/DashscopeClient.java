package com.fly.ai.demo.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.ai.common.model.AiChatRequest;
import com.fly.ai.common.model.AiChatResponse;
import com.fly.common.enums.ai.AiProviderEnum;
import com.fly.ai.common.config.AiProperties;
import com.fly.ai.common.model.AiEmbeddingRequest;
import com.fly.ai.common.model.AiEmbeddingResponse;
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

    /**
     * 创建阿里云百炼客户端。
     *
     * @param aiHttpClient 模型 HTTP 客户端
     * @param objectMapper JSON 序列化工具
     * @param properties AI 配置
     * @param streamTaskExecutor 流式任务线程池
     */
    public DashscopeClient(HttpClient aiHttpClient, ObjectMapper objectMapper, AiProperties properties,
            @org.springframework.beans.factory.annotation.Qualifier("aiStreamTaskExecutor") Executor streamTaskExecutor) {
        super(aiHttpClient, objectMapper, properties, streamTaskExecutor);
        this.properties = properties;
    }

    /**
     * 调用百炼普通聊天接口。
     *
     * @param request 聊天请求
     * @return 完整聊天响应
     */
    public AiChatResponse chat(AiChatRequest request) {
        AiProperties.Dashscope dashscope = properties.getDashscope();
        return chat(AiProviderEnum.DASHSCOPE.getDisplayName(), dashscope.getBaseUrl(), dashscope.getApiKey(), dashscope.getChatModel(), dashscope.getChatPath(),
                dashscope.getResponseTimeout(), request);
    }

    /**
     * 调用百炼流式聊天接口。
     *
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    public SseEmitter stream(AiChatRequest request) {
        AiProperties.Dashscope dashscope = properties.getDashscope();
        return stream(AiProviderEnum.DASHSCOPE.getDisplayName(), dashscope.getBaseUrl(), dashscope.getApiKey(), dashscope.getChatModel(), dashscope.getChatPath(),
                dashscope.getResponseTimeout(), request);
    }

    /**
     * 调用百炼文本向量化接口。
     *
     * @param request 向量化请求
     * @return 文本向量响应
     */
    public AiEmbeddingResponse embed(AiEmbeddingRequest request) {
        AiProperties.Dashscope dashscope = properties.getDashscope();
        return embed(AiProviderEnum.DASHSCOPE.getDisplayName(), dashscope.getBaseUrl(), dashscope.getApiKey(), dashscope.getChatModel(),
                dashscope.getEmbeddingModel(), dashscope.getEmbeddingPath(), dashscope.getResponseTimeout(), request);
    }

}

package com.fly.ai.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.ai.model.AiChatRequest;
import com.fly.ai.model.AiChatResponse;
import com.fly.ai.config.AiProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.http.HttpClient;
import java.util.concurrent.Executor;

/**
 * DeepSeek 原生客户端。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Component
public class DeepseekClient extends AbstractChatCompletionsClient {

    private final AiProperties properties;

    /**
     * 创建 DeepSeek 客户端。
     *
     * @param aiHttpClient 模型 HTTP 客户端
     * @param objectMapper JSON 序列化工具
     * @param properties AI 配置
     * @param streamTaskExecutor 流式任务线程池
     */
    public DeepseekClient(HttpClient aiHttpClient, ObjectMapper objectMapper, AiProperties properties,
            @org.springframework.beans.factory.annotation.Qualifier("aiStreamTaskExecutor") Executor streamTaskExecutor) {
        super(aiHttpClient, objectMapper, properties, streamTaskExecutor);
        this.properties = properties;
    }

    /**
     * 调用 DeepSeek 普通聊天接口。
     *
     * @param request 聊天请求
     * @return 完整聊天响应
     */
    public AiChatResponse chat(AiChatRequest request) {
        AiProperties.Deepseek deepseek = properties.getDeepseek();
        return chat("DeepSeek", deepseek.getBaseUrl(), deepseek.getApiKey(), deepseek.getChatModel(), deepseek.getChatPath(),
                deepseek.getResponseTimeout(), request);
    }

    /**
     * 调用 DeepSeek 流式聊天接口。
     *
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    public SseEmitter stream(AiChatRequest request) {
        AiProperties.Deepseek deepseek = properties.getDeepseek();
        return stream("DeepSeek", deepseek.getBaseUrl(), deepseek.getApiKey(), deepseek.getChatModel(), deepseek.getChatPath(),
                deepseek.getResponseTimeout(), request);
    }

}

package com.fly.ai.service;

import com.fly.ai.model.AiChatRequest;
import com.fly.ai.model.AiChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * AI 聊天应用服务。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Service
@RequiredArgsConstructor
public class AiChatService {

    private final AiModelProviderRouter aiModelProviderRouter;

    /**
     * 根据当前供应商配置执行普通聊天。
     *
     * @param request 聊天请求
     * @return 完整聊天响应
     */
    public AiChatResponse chat(AiChatRequest request) {
        return aiModelProviderRouter.chat(request);
    }

    /**
     * 根据当前供应商配置执行流式聊天。
     *
     * @param request 聊天请求
     * @return SSE 响应发送器
     */
    public SseEmitter stream(AiChatRequest request) {
        return aiModelProviderRouter.stream(request);
    }

}

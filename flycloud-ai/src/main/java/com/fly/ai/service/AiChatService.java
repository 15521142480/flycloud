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

    public AiChatResponse chat(AiChatRequest request) {
        return aiModelProviderRouter.chat(request);
    }

    public SseEmitter stream(AiChatRequest request) {
        return aiModelProviderRouter.stream(request);
    }

}

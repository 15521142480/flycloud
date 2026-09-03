package com.fly.ai.chat.service;

import com.fly.ai.common.model.AiChatResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 纯模型统一聊天的流式生命周期回调。
 *
 * @author lxs
 * @date 2026-09-03
 */
public interface AiGeneralChatStreamObserver {

    /**
     * 流式响应创建完成时调用。
     *
     * @param emitter SSE 响应发送器
     */
    default void onStarted(SseEmitter emitter) {
    }

    /**
     * 模型成功完成本轮回答时调用。
     *
     * @param response 模型响应
     */
    default void onCompleted(AiChatResponse response) {
    }

    /**
     * 模型调用失败时调用。
     *
     * @param exception 异常原因
     */
    default void onError(Throwable exception) {
    }
}

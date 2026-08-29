package com.fly.ai.common.tool.service;

import com.fly.ai.common.tool.model.AiToolCallingResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Tool Calling 流式执行观察器。
 * <p>
 * 正式统一聊天通过该观察器持久化消息；Tool Calling 学习 Demo 不需要复制调用逻辑，直接使用默认空实现。
 *
 * @author lxs
 * @date 2026-08-28
 */
public interface AiToolCallingStreamObserver {

    /**
     * 流式响应已创建。
     *
     * @param emitter SSE 发送器
     */
    default void onStarted(SseEmitter emitter) {
    }

    /**
     * 接收到一段模型文本。
     *
     * @param delta 文本增量
     */
    default void onDelta(String delta) {
    }

    /**
     * 流式响应正常完成。
     *
     * @param response 聚合后的模型响应
     */
    default void onCompleted(AiToolCallingResponse response) {
    }

    /**
     * 流式响应异常结束。
     *
     * @param exception 异常原因
     */
    default void onError(Throwable exception) {
    }
}

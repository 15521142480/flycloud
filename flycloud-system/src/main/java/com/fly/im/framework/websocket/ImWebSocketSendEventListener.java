package com.fly.im.framework.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * IM WebSocket 发送事件监听器，负责把业务层发布的消息推送到在线连接。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Component
@RequiredArgsConstructor
public class ImWebSocketSendEventListener {

    private final ImWebSocketSessionManager sessionManager;

    /**
     * 消费 IM 消息发送事件，按单人、多人或全员广播三种方式投递。
     */
    @EventListener
    public void onSendEvent(ImWebSocketSendEvent event) {
        if (event.getUserId() != null) {
            sessionManager.send(event.getUserType(), event.getUserId(), event.getMessageType(), event.getMessageContent());
            return;
        }
        if (event.getUserIds() != null && !event.getUserIds().isEmpty()) {
            sessionManager.send(event.getUserType(), event.getUserIds(), event.getMessageType(), event.getMessageContent());
            return;
        }
        sessionManager.broadcast(event.getUserType(), event.getMessageType(), event.getMessageContent());
    }

}

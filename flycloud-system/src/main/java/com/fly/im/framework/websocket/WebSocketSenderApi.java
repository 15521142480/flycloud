package com.fly.im.framework.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * IM WebSocket 发送适配。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Component
@RequiredArgsConstructor
public class WebSocketSenderApi {

    private final ApplicationEventPublisher eventPublisher;

    public void sendObject(Integer userType, Long userId, String messageType, Object messageContent) {
        eventPublisher.publishEvent(new ImWebSocketSendEvent(userType, userId, null, messageType, messageContent));
    }

    public void sendObject(Integer userType, Collection<Long> userIds, String messageType, Object messageContent) {
        eventPublisher.publishEvent(new ImWebSocketSendEvent(userType, null, userIds, messageType, messageContent));
    }

    public void sendObject(Integer userType, String messageType, Object messageContent) {
        eventPublisher.publishEvent(new ImWebSocketSendEvent(userType, null, null, messageType, messageContent));
    }

}

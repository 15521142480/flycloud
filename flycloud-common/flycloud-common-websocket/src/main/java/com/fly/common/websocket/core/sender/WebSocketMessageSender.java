package com.fly.common.websocket.core.sender;

import com.fly.common.utils.json.JsonUtils;

/**
 * WebSocket 消息发送器接口，支持按用户、用户类型和会话发送。
 *
 * @author lxs
 * @date 2026-07-09
 */
public interface WebSocketMessageSender {

    /**
     * 发送消息给指定用户。
     */
    void send(Integer userType, Long userId, String messageType, String messageContent);

    /**
     * 发送消息给指定用户类型下的全部在线用户。
     */
    void send(Integer userType, String messageType, String messageContent);

    /**
     * 发送消息给指定会话。
     */
    void send(String sessionId, String messageType, String messageContent);

    /**
     * 发送对象消息给指定用户。
     */
    default void sendObject(Integer userType, Long userId, String messageType, Object messageContent) {
        send(userType, userId, messageType, JsonUtils.toJsonString(messageContent));
    }

    /**
     * 发送对象消息给指定用户类型下的全部在线用户。
     */
    default void sendObject(Integer userType, String messageType, Object messageContent) {
        send(userType, messageType, JsonUtils.toJsonString(messageContent));
    }

    /**
     * 发送对象消息给指定会话。
     */
    default void sendObject(String sessionId, String messageType, Object messageContent) {
        send(sessionId, messageType, JsonUtils.toJsonString(messageContent));
    }

}

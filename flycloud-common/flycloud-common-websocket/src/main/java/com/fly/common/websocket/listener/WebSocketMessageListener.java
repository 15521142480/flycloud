package com.fly.common.websocket.listener;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket 客户端上行消息监听器。
 *
 * @author lxs
 * @date 2026-07-09
 */
public interface WebSocketMessageListener<T> {

    /**
     * 处理客户端发送的业务消息。
     *
     * @param session 当前连接
     * @param message 消息内容
     */
    void onMessage(WebSocketSession session, T message);

    /**
     * 获取当前监听器处理的消息类型。
     *
     * @return 消息类型
     */
    String getType();

}

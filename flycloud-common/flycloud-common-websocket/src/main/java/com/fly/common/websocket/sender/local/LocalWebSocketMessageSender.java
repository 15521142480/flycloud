package com.fly.common.websocket.sender.local;

import com.fly.common.websocket.sender.AbstractWebSocketMessageSender;
import com.fly.common.websocket.session.WebSocketSessionManager;

/**
 * 本地 WebSocket 消息发送器，适合单机或开发环境。
 *
 * @author lxs
 * @date 2026-07-09
 */
public class LocalWebSocketMessageSender extends AbstractWebSocketMessageSender {

    public LocalWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        super(sessionManager);
    }

}

package com.fly.common.websocket.session;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * WebSocket 会话装饰器，负责连接注册、关闭清理和并发发送保护。
 *
 * @author lxs
 * @date 2026-07-09
 */
public class WebSocketSessionHandlerDecorator extends WebSocketHandlerDecorator {

    private static final Integer SEND_TIME_LIMIT = 1000 * 5;

    private static final Integer BUFFER_SIZE_LIMIT = 1024 * 100;

    private final WebSocketSessionManager sessionManager;

    public WebSocketSessionHandlerDecorator(WebSocketHandler delegate, WebSocketSessionManager sessionManager) {
        super(delegate);
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketSession concurrentSession =
                new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, BUFFER_SIZE_LIMIT);
        sessionManager.addSession(concurrentSession);
        super.afterConnectionEstablished(concurrentSession);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessionManager.removeSession(session);
        super.afterConnectionClosed(session, closeStatus);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        sessionManager.removeSession(session);
        super.handleTransportError(session, exception);
    }

}

package com.fly.common.websocket.core.sender;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fly.common.utils.json.JsonUtils;
import com.fly.common.websocket.core.message.JsonWebSocketMessage;
import com.fly.common.websocket.core.session.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * WebSocket 消息发送器抽象实现，负责查找在线会话并包装统一消息帧。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractWebSocketMessageSender implements WebSocketMessageSender {

    private final WebSocketSessionManager sessionManager;

    @Override
    public void send(Integer userType, Long userId, String messageType, String messageContent) {
        send(null, userType, userId, messageType, messageContent);
    }

    @Override
    public void send(Integer userType, String messageType, String messageContent) {
        send(null, userType, null, messageType, messageContent);
    }

    @Override
    public void send(String sessionId, String messageType, String messageContent) {
        send(sessionId, null, null, messageType, messageContent);
    }

    /**
     * 根据会话编号、用户类型或用户编号投递消息。
     */
    public void send(String sessionId, Integer userType, Long userId, String messageType, String messageContent) {
        Collection<WebSocketSession> sessions = Collections.emptyList();
        if (StrUtil.isNotBlank(sessionId)) {
            WebSocketSession session = sessionManager.getSession(sessionId);
            if (session != null) {
                sessions = Collections.singletonList(session);
            }
        } else if (userType != null && userId != null) {
            sessions = sessionManager.getSessionList(userType, userId);
        } else if (userType != null) {
            sessions = sessionManager.getSessionList(userType);
        }
        if (CollUtil.isEmpty(sessions)) {
            log.debug("[send][sessionId({}) userType({}) userId({}) messageType({}) 未匹配到在线会话]",
                    sessionId, userType, userId, messageType);
            return;
        }
        doSend(sessions, messageType, messageContent);
    }

    /**
     * 对在线会话执行真实发送。
     */
    protected void doSend(Collection<WebSocketSession> sessions, String messageType, String messageContent) {
        JsonWebSocketMessage message = new JsonWebSocketMessage().setType(messageType).setContent(messageContent);
        String payload = JsonUtils.toJsonString(message);
        sessions.forEach(session -> sendToSession(session, payload, message));
    }

    private void sendToSession(WebSocketSession session, String payload, JsonWebSocketMessage message) {
        if (session == null || !session.isOpen()) {
            log.debug("[sendToSession][session 不可用 message({})]", message);
            return;
        }
        try {
            session.sendMessage(new TextMessage(payload));
        } catch (IOException e) {
            log.warn("[sendToSession][session({}) message({}) 发送失败]", session.getId(), message, e);
        }
    }

}

package com.fly.im.framework.websocket;

import com.fly.common.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * IM WebSocket 在线会话管理器，负责按用户类型和用户编号维护连接并发送消息。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Slf4j
@Component
public class ImWebSocketSessionManager {

    public static final String ATTR_USER_TYPE = "userType";

    public static final String ATTR_USER_ID = "userId";

    private final Map<String, Set<WebSocketSession>> sessionMap = new ConcurrentHashMap<>();

    /**
     * 注册用户连接，支持同一个用户多端同时在线。
     */
    public void register(WebSocketSession session) {
        Integer userType = (Integer) session.getAttributes().get(ATTR_USER_TYPE);
        Long userId = (Long) session.getAttributes().get(ATTR_USER_ID);
        if (userType == null || userId == null) {
            return;
        }
        sessionMap.computeIfAbsent(buildKey(userType, userId), key -> new CopyOnWriteArraySet<>()).add(session);
    }

    /**
     * 移除断开的连接，避免后续消息推送到失效会话。
     */
    public void unregister(WebSocketSession session) {
        Integer userType = (Integer) session.getAttributes().get(ATTR_USER_TYPE);
        Long userId = (Long) session.getAttributes().get(ATTR_USER_ID);
        if (userType == null || userId == null) {
            return;
        }
        Set<WebSocketSession> sessions = sessionMap.get(buildKey(userType, userId));
        if (sessions == null) {
            return;
        }
        sessions.remove(session);
        if (sessions.isEmpty()) {
            sessionMap.remove(buildKey(userType, userId));
        }
    }

    /**
     * 向单个用户的所有在线端发送消息。
     */
    public void send(Integer userType, Long userId, String messageType, Object messageContent) {
        if (userType == null || userId == null) {
            return;
        }
        sendToSessions(sessionMap.get(buildKey(userType, userId)), messageType, messageContent);
    }

    /**
     * 向多个用户的所有在线端发送消息。
     */
    public void send(Integer userType, Collection<Long> userIds, String messageType, Object messageContent) {
        if (userType == null || userIds == null || userIds.isEmpty()) {
            return;
        }
        userIds.stream().filter(Objects::nonNull).forEach(userId -> send(userType, userId, messageType, messageContent));
    }

    /**
     * 向指定用户类型下的所有在线端广播消息。
     */
    public void broadcast(Integer userType, String messageType, Object messageContent) {
        if (userType == null) {
            return;
        }
        String prefix = userType + ":";
        sessionMap.forEach((key, sessions) -> {
            if (key.startsWith(prefix)) {
                sendToSessions(sessions, messageType, messageContent);
            }
        });
    }

    private void sendToSessions(Set<WebSocketSession> sessions, String messageType, Object messageContent) {
        if (sessions == null || sessions.isEmpty()) {
            return;
        }
        String payload = JsonUtils.toJsonString(Map.of("type", messageType, "content", messageContent));
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                unregister(session);
                continue;
            }
            try {
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.warn("[sendToSessions][sessionId({}) type({}) 发送失败]", session.getId(), messageType, e);
                unregister(session);
            }
        }
    }

    private static String buildKey(Integer userType, Long userId) {
        return userType + ":" + userId;
    }

}

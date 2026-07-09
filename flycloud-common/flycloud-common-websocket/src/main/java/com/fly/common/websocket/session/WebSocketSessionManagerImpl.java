package com.fly.common.websocket.session;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.security.user.FlyUser;
import com.fly.common.websocket.util.WebSocketFrameworkUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 默认 WebSocket 在线会话管理器，支持按用户类型和用户编号查询连接。
 *
 * @author lxs
 * @date 2026-07-09
 */
public class WebSocketSessionManagerImpl implements WebSocketSessionManager {

    private final ConcurrentMap<String, WebSocketSession> idSessions = new ConcurrentHashMap<>();

    private final ConcurrentMap<Integer, ConcurrentMap<Long, CopyOnWriteArrayList<WebSocketSession>>> userSessions =
            new ConcurrentHashMap<>();

    @Override
    public void addSession(WebSocketSession session) {
        idSessions.put(session.getId(), session);
        FlyUser user = WebSocketFrameworkUtils.getLoginUser(session);
        if (user == null || user.getId() == null) {
            return;
        }
        ConcurrentMap<Long, CopyOnWriteArrayList<WebSocketSession>> userSessionsMap =
                userSessions.computeIfAbsent(user.getUserType(), key -> new ConcurrentHashMap<>());
        CopyOnWriteArrayList<WebSocketSession> sessions =
                userSessionsMap.computeIfAbsent(user.getId(), key -> new CopyOnWriteArrayList<>());
        sessions.add(session);
    }

    @Override
    public void removeSession(WebSocketSession session) {
        idSessions.remove(session.getId());
        FlyUser user = WebSocketFrameworkUtils.getLoginUser(session);
        if (user == null || user.getId() == null) {
            return;
        }
        ConcurrentMap<Long, CopyOnWriteArrayList<WebSocketSession>> userSessionsMap = userSessions.get(user.getUserType());
        if (CollUtil.isEmpty(userSessionsMap)) {
            return;
        }
        CopyOnWriteArrayList<WebSocketSession> sessions = userSessionsMap.get(user.getId());
        if (CollUtil.isEmpty(sessions)) {
            return;
        }
        sessions.removeIf(item -> item.getId().equals(session.getId()));
        if (sessions.isEmpty()) {
            userSessionsMap.remove(user.getId(), sessions);
        }
    }

    @Override
    public WebSocketSession getSession(String id) {
        return idSessions.get(id);
    }

    @Override
    public Collection<WebSocketSession> getSessionList(Integer userType) {
        ConcurrentMap<Long, CopyOnWriteArrayList<WebSocketSession>> userSessionsMap = userSessions.get(userType);
        if (CollUtil.isEmpty(userSessionsMap)) {
            return new ArrayList<>();
        }
        LinkedList<WebSocketSession> result = new LinkedList<>();
        userSessionsMap.values().forEach(result::addAll);
        return result;
    }

    @Override
    public Collection<WebSocketSession> getSessionList(Integer userType, Long userId) {
        ConcurrentMap<Long, CopyOnWriteArrayList<WebSocketSession>> userSessionsMap = userSessions.get(userType);
        if (CollUtil.isEmpty(userSessionsMap)) {
            return new ArrayList<>();
        }
        CopyOnWriteArrayList<WebSocketSession> sessions = userSessionsMap.get(userId);
        return CollUtil.isNotEmpty(sessions) ? new ArrayList<>(sessions) : new ArrayList<>();
    }

}

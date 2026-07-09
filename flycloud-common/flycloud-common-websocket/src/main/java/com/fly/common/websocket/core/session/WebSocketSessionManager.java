package com.fly.common.websocket.core.session;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

/**
 * WebSocket 在线会话管理器接口。
 *
 * @author lxs
 * @date 2026-07-09
 */
public interface WebSocketSessionManager {

    /**
     * 添加在线会话。
     */
    void addSession(WebSocketSession session);

    /**
     * 移除在线会话。
     */
    void removeSession(WebSocketSession session);

    /**
     * 根据会话编号获取会话。
     */
    WebSocketSession getSession(String id);

    /**
     * 获取指定用户类型的全部会话。
     */
    Collection<WebSocketSession> getSessionList(Integer userType);

    /**
     * 获取指定用户的全部会话。
     */
    Collection<WebSocketSession> getSessionList(Integer userType, Long userId);

}

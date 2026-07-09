package com.fly.common.websocket.core.util;

import com.fly.common.security.user.FlyUser;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * WebSocket 会话工具类，统一读写握手阶段绑定的登录用户。
 *
 * @author lxs
 * @date 2026-07-09
 */
public final class WebSocketFrameworkUtils {

    public static final String LOGIN_USER = "loginUser";

    private WebSocketFrameworkUtils() {
    }

    /**
     * 把登录用户绑定到 WebSocket 握手属性。
     */
    public static void setLoginUser(FlyUser loginUser, Map<String, Object> attributes) {
        attributes.put(LOGIN_USER, loginUser);
    }

    /**
     * 从 WebSocket 会话读取登录用户。
     */
    public static FlyUser getLoginUser(WebSocketSession session) {
        Object loginUser = session.getAttributes().get(LOGIN_USER);
        return loginUser instanceof FlyUser user ? user : null;
    }

}

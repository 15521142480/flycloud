package com.fly.common.websocket.security;

import com.fly.common.security.user.FlyUser;
import com.fly.common.websocket.util.WebSocketFrameworkUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket 登录用户握手拦截器，把资源服务认证后的用户绑定到长连接会话。
 *
 * @author lxs
 * @date 2026-07-09
 */
public class LoginUserHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof FlyUser loginUser)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        WebSocketFrameworkUtils.setLoginUser(loginUser, attributes);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

}

package com.fly.im.framework.websocket;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.utils.auth.TokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

/**
 * IM WebSocket 握手拦截器，解析 header 或 query 中的 token 并绑定当前系统用户。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Component
public class ImWebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = resolveToken(request);
        Claims claims = SecurityUtils.getClaims(token);
        if (claims == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        Long userId = Convert.toLong(claims.get(Oauth2Constants.USER_ID));
        Integer userType = Convert.toInt(claims.get(Oauth2Constants.USER_TYPE), 0);
        if (userId == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        attributes.put(ImWebSocketSessionManager.ATTR_USER_ID, userId);
        attributes.put(ImWebSocketSessionManager.ATTR_USER_TYPE, userType);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

    private String resolveToken(ServerHttpRequest request) {
        String headerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = TokenUtils.getToken(headerToken);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        Map<String, List<String>> queryParams = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        token = firstQueryValue(queryParams, "access_token");
        if (StrUtil.isBlank(token)) {
            token = firstQueryValue(queryParams, "token");
        }
        if (StrUtil.isBlank(token)) {
            token = firstQueryValue(queryParams, Oauth2Constants.HEADER_TOKEN_KEY);
        }
        return TokenUtils.getToken(token);
    }

    private static String firstQueryValue(Map<String, List<String>> queryParams, String key) {
        List<String> values = queryParams.get(key);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

}

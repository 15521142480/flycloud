package com.fly.im.framework.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * IM WebSocket 配置，注册客户端长连接入口。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSocket
@RequiredArgsConstructor
public class ImWebSocketConfiguration implements WebSocketConfigurer {

    private final ImWebSocketHandler imWebSocketHandler;

    private final ImWebSocketHandshakeInterceptor imWebSocketHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(imWebSocketHandler, "/im/ws")
                .addInterceptors(imWebSocketHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
    }

}

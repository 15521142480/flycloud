package com.fly.common.websocket.config;

import com.fly.common.websocket.handler.JsonWebSocketMessageHandler;
import com.fly.common.websocket.listener.WebSocketMessageListener;
import com.fly.common.websocket.security.LoginUserHandshakeInterceptor;
import com.fly.common.websocket.sender.WebSocketMessageSender;
import com.fly.common.websocket.sender.local.LocalWebSocketMessageSender;
import com.fly.common.websocket.session.WebSocketSessionHandlerDecorator;
import com.fly.common.websocket.session.WebSocketSessionManager;
import com.fly.common.websocket.session.WebSocketSessionManagerImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

/**
 * WebSocket 自动配置，统一注册连接入口、握手鉴权、会话管理和消息发送器。
 *
 * @author lxs
 * @date 2026-07-09
 */
@EnableWebSocket
@AutoConfiguration
@EnableConfigurationProperties(WebSocketProperties.class)
@ConditionalOnProperty(prefix = "flycloud.websocket", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebSocketAutoConfiguration {

    /**
     * 注册 WebSocket 客户端连接入口。
     */
    @Bean
    public WebSocketConfigurer webSocketConfigurer(HandshakeInterceptor[] handshakeInterceptors,
                                                   WebSocketHandler webSocketHandler,
                                                   WebSocketProperties webSocketProperties) {
        return registry -> registry.addHandler(webSocketHandler, webSocketProperties.getPath())
                .addInterceptors(handshakeInterceptors)
                .setAllowedOriginPatterns("*");
    }

    /**
     * 创建登录用户握手拦截器。
     */
    @Bean
    @ConditionalOnMissingBean
    public HandshakeInterceptor loginUserHandshakeInterceptor() {
        return new LoginUserHandshakeInterceptor();
    }

    /**
     * 创建 JSON 消息处理器，并用装饰器维护在线会话。
     */
    @Bean
    @ConditionalOnMissingBean
    public WebSocketHandler webSocketHandler(WebSocketSessionManager sessionManager,
                                             List<? extends WebSocketMessageListener<?>> messageListeners) {
        JsonWebSocketMessageHandler messageHandler = new JsonWebSocketMessageHandler(messageListeners);
        return new WebSocketSessionHandlerDecorator(messageHandler, sessionManager);
    }

    /**
     * 创建在线会话管理器。
     */
    @Bean
    @ConditionalOnMissingBean
    public WebSocketSessionManager webSocketSessionManager() {
        return new WebSocketSessionManagerImpl();
    }

    /**
     * 创建本地消息发送器，适合单实例或开发环境。
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "flycloud.websocket", name = "sender-type", havingValue = "local", matchIfMissing = true)
    public WebSocketMessageSender localWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        return new LocalWebSocketMessageSender(sessionManager);
    }

}

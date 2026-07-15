package com.fly.common.websocket.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * WebSocket 配置项，控制连接路径和是否开启长连接能力。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Data
@Validated
@ConfigurationProperties(prefix = "flycloud.websocket")
public class WebSocketProperties {

    /**
     * 是否开启 WebSocket 自动配置。
     */
    private Boolean enabled = true;

    /**
     * WebSocket 客户端握手路径。
     */
    @NotBlank(message = "WebSocket 连接路径不能为空")
    private String path = "/ws";

    /**
     * 消息发送器类型，当前先支持本地单机发送。
     */
    @NotBlank(message = "WebSocket 消息发送器类型不能为空")
    private String senderType = "local";

}

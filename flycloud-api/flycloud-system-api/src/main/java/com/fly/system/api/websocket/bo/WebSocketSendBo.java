package com.fly.system.api.websocket.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * WebSocket 消息发送参数。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Data
@Accessors(chain = true)
@Schema(description = "WebSocket 消息发送参数")
public class WebSocketSendBo {

    /**
     * 用户类型。
     */
    @NotNull(message = "用户类型不能为空")
    @Schema(description = "用户类型")
    private Integer userType;

    /**
     * 用户编号，为空时按用户类型广播。
     */
    @Schema(description = "用户编号")
    private Long userId;

    /**
     * WebSocket 会话编号。
     */
    @Schema(description = "WebSocket 会话编号")
    private String sessionId;

    /**
     * 消息类型。
     */
    @NotBlank(message = "消息类型不能为空")
    @Schema(description = "消息类型")
    private String messageType;

    /**
     * 消息内容，JSON 字符串。
     */
    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "消息内容")
    private String messageContent;

}

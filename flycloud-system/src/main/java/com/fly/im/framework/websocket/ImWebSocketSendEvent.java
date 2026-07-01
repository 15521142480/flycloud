package com.fly.im.framework.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * IM WebSocket 发送事件。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ImWebSocketSendEvent {

    /**
     * 用户类型。
     */
    private Integer userType;

    /**
     * 单个用户编号。
     */
    private Long userId;

    /**
     * 多个用户编号。
     */
    private Collection<Long> userIds;

    /**
     * 消息类型。
     */
    private String messageType;

    /**
     * 消息内容。
     */
    private Object messageContent;

}

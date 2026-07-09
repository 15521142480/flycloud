package com.fly.im.service.websocket.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * IM WebSocket 通知外层消息业务对象
 * <p>
 * 前端统一监听 im-notification，再根据会话类型和内容类型分发到私聊、群聊、频道等处理逻辑。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Data
@Accessors(chain = true)
public class ImNotificationWebSocketBo {

    public static final String TYPE = "im-notification";

    /**
     * 会话类型
     */
    private Integer conversationType;

    /**
     * 内容类型
     */
    private Integer contentType;

    /**
     * 真实推送内容
     */
    private Object payload;

}

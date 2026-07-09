package com.fly.common.websocket.message;

import com.fly.common.websocket.listener.WebSocketMessageListener;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * WebSocket JSON 消息帧，前后端统一使用 type 分发消息。
 *
 * @author lxs
 * @date 2026-07-09
 */
@Data
@Accessors(chain = true)
public class JsonWebSocketMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息类型，用于匹配 {@link WebSocketMessageListener#getType()}。
     */
    private String type;

    /**
     * 消息内容，使用 JSON 字符串承载业务对象。
     */
    private String content;

}

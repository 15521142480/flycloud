package com.fly.im.service.websocket.dto.message;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 撤回提示消息内容
 */
@Data
@Accessors(chain = true)
public class RecallMessage {

    /**
     * 被撤回的原消息编号
     */
    @JsonLongId
    private Long messageId;

}

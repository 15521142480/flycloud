package com.fly.im.service.websocket.dto.notification.group;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 群消息取消置顶事件通知
 */
@Data
@Accessors(chain = true)
public class GroupMessageUnpinNotification extends BaseGroupNotification {

    /**
     * 被取消置顶的消息编号
     */
    private Long messageId;

}

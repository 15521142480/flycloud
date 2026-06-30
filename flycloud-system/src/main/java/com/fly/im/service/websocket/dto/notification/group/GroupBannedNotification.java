package com.fly.im.service.websocket.dto.notification.group;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 群封禁 / 解封通知
 */
@Data
@Accessors(chain = true)
public class GroupBannedNotification extends BaseGroupNotification {

    /**
     * 是否封禁
     */
    private Boolean banned;

}

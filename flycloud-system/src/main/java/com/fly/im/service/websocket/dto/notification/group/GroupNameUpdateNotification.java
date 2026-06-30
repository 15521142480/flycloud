package com.fly.im.service.websocket.dto.notification.group;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 群名变更事件通知
 */
@Data
@Accessors(chain = true)
public class GroupNameUpdateNotification extends BaseGroupNotification {

    /**
     * 旧群名
     */
    private String oldName;
    /**
     * 新群名
     */
    private String newName;

}

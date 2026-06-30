package com.fly.im.service.websocket.dto.notification.group;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 成员组内昵称变更事件通知
 */
@Data
@Accessors(chain = true)
public class GroupMemberNicknameUpdateNotification extends BaseGroupNotification {

    /**
     * 群内昵称
     */
    private String displayUserName;

}

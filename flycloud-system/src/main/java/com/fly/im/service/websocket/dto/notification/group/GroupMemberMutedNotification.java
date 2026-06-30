package com.fly.im.service.websocket.dto.notification.group;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 成员禁言通知
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMemberMutedNotification extends BaseGroupNotification {

    /**
     * 被禁言的用户编号
     */
    private Long mutedUserId;
    /**
     * 禁言到期时间
     */
    private LocalDateTime muteEndTime;

}

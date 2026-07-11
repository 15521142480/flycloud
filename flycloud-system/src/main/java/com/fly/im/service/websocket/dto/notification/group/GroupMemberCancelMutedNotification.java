package com.fly.im.service.websocket.dto.notification.group;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 取消成员禁言通知
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMemberCancelMutedNotification extends BaseGroupNotification {

    /**
     * 被取消禁言的用户编号
     */
    @JsonLongId
    private Long mutedUserId;

}

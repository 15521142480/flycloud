package com.fly.im.service.websocket.dto.notification.group;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 全群取消禁言通知
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GroupCancelMutedNotification extends BaseGroupNotification {

}

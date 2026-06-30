package com.fly.im.service.websocket.dto.notification.friend;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 加入黑名单通知
 * <p>
 * A 拉黑 B 后仅推 A 多端；B 端不感知（B 那边 blocked=0 不变）
 */
@Data
@Accessors(chain = true)
public class FriendBlockNotification extends BaseFriendNotification {

}

package com.fly.im.service.websocket.dto.notification.friend;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 移出黑名单通知
 * <p>
 * A 把 B 移出黑名单后仅推 A 多端
 */
@Data
@Accessors(chain = true)
public class FriendUnblockNotification extends BaseFriendNotification {

}

package com.fly.im.service.websocket.dto.notification.friend;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新增好友通知
 * <p>
 * 双向建立好友关系后，推送给 A、B 双方多端；前端拉取好友信息入库
 */
@Data
@Accessors(chain = true)
public class FriendAddNotification extends BaseFriendNotification {

}

package com.fly.im.service.websocket.dto.notification.friend;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 好友申请被同意通知
 * <p>
 * 推送给原申请发起方多端；前端按 requestId 把对应申请记录 handleResult 更新为「已同意」
 */
@Data
@Accessors(chain = true)
public class FriendRequestApprovedNotification extends BaseFriendNotification {

    /**
     * 已处理的申请记录编号
     */
    @JsonLongId
    private Long requestId;

}

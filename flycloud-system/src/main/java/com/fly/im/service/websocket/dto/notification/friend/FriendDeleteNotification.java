package com.fly.im.service.websocket.dto.notification.friend;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 好友删除通知
 * <p>
 * 仅推送给操作人多端；前端清除本地好友，并按 clear 决定是否级联清理历史会话。
 */
@Data
@Accessors(chain = true)
public class FriendDeleteNotification extends BaseFriendNotification {

    /**
     * 是否级联清理本端相关数据（当前包含私聊会话；未来可能扩展更多 clear 项）
     */
    private Boolean clear;

}

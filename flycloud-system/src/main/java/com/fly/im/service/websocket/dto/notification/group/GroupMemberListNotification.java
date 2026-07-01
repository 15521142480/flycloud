package com.fly.im.service.websocket.dto.notification.group;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 群事件成员列表通知基类
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Accessors(chain = true)
public abstract class GroupMemberListNotification extends BaseGroupNotification {

    /**
     * 受影响的成员用户编号列表
     */
    private List<Long> memberUserIds;

}

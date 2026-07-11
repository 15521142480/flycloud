package com.fly.im.service.websocket.dto.notification.group;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 群事件通知基类：所有群事件 payload 共享 operatorUserId
 */
@Data
@Accessors(chain = true)
public abstract class BaseGroupNotification {

    /**
     * 操作人用户编号
     */
    @JsonLongId
    private Long operatorUserId;

}

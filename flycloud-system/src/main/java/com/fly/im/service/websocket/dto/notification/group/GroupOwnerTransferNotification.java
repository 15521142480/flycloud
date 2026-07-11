package com.fly.im.service.websocket.dto.notification.group;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 群主转让事件通知
 */
@Data
@Accessors(chain = true)
public class GroupOwnerTransferNotification extends BaseGroupNotification {

    /**
     * 新群主用户编号
     */
    @JsonLongId
    private Long newOwnerUserId;

}

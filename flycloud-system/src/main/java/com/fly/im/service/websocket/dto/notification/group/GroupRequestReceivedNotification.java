package com.fly.im.service.websocket.dto.notification.group;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 收到新的入群申请通知
 * <p>
 * 走私聊通道定向推送给群主 + 全部管理员（多端同步）；payload 已携带申请方昵称 / 头像，前端按 requestId 直接 push 进列表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class GroupRequestReceivedNotification extends BaseGroupNotification {

    /**
     * 申请记录编号
     */
    @JsonLongId
    private Long requestId;
    /**
     * 群编号
     */
    @JsonLongId
    private Long groupId;
    /**
     * 申请人 / 被邀请人用户编号
     */
    @JsonLongId
    private Long userId;
    /**
     * 邀请人用户编号；NULL 表示用户主动申请
     */
    @JsonLongId
    private Long inviterUserId;
    /**
     * 申请理由
     */
    private String applyContent;
    /**
     * 加入来源
     */
    private Integer addSource;

    // ========== 聚合自 AdminUser，避免前端再调 system 接口 ==========

    /**
     * 申请方 / 被邀请人昵称
     */
    private String userNickname;
    /**
     * 申请方 / 被邀请人头像
     */
    private String userAvatar;

}

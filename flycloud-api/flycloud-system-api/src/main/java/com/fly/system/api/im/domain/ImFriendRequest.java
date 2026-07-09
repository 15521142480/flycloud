package com.fly.system.api.im.domain;

import com.fly.common.domain.BaseEntity;
import com.fly.system.api.im.enums.friend.ImFriendAddSourceEnum;
import com.fly.system.api.im.enums.friend.ImFriendRequestHandleResultEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * IM 好友申请记录 DO
 * <p>
 * 配合「申请 - 审批」流程：
 * - 发起方调 apply 接口落库（handleResult=UNHANDLED）
 * - 接收方调 agree / refuse 处理（更新 handleResult / handleTime / handleContent）
 * - 申请通过后，displayName / addSource 同步写入 {@link ImFriend}
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("im_friend_request")
@KeySequence("im_friend_request_seq")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImFriendRequest extends BaseEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 发起方用户编号
     * <p>
     * 关联 AdminUserDO 的 id 字段
     */
    private Long fromUserId;
    /**
     * 接收方用户编号
     * <p>
     * 关联 AdminUserDO 的 id 字段
     */
    private Long toUserId;

    // ========== 申请发起阶段（发起方填写） ==========
    /**
     * 申请理由
     */
    private String applyContent;
    /**
     * 发起方对接收方的备注
     * <p>
     * 同意后写入 A 侧 {@link ImFriend#getDisplayName()}；B 侧不动
     */
    private String displayName;
    /**
     * 添加来源
     * <p>
     * 枚举 {@link ImFriendAddSourceEnum}
     */
    private Integer addSource;

    // ========== 申请处理阶段（接收方填写） ==========
    /**
     * 处理结果
     * <p>
     * 枚举 {@link ImFriendRequestHandleResultEnum}
     */
    private Integer handleResult;
    /**
     * 处理理由（接收方拒绝时可选填）
     */
    private String handleContent;
    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

}

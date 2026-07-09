package com.fly.system.api.im.domain;

import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * IM 好友关系 DO
 * <p>
 * 业务语义：
 * - 双向关系：A-B 互为好友会存 2 条记录（userId=A, friendUserId=B 和 userId=B, friendUserId=A）
 * - 状态管理：{@link #status} 使用 {@link CommonStatusEnum}，ENABLE=正常，DISABLE=已删除
 * - 免打扰：{@link #silent} 控制是否屏蔽来自该好友的通知
 * - 联系人置顶：{@link #pinned} 单边，影响联系人 / 会话排序
 * - 黑名单：{@link #blocked} 弱关联 friend，单边屏蔽对方消息（必须先是好友）
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("im_friend")
@KeySequence("im_friend_seq")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImFriend extends BaseEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     * <p>
     * 关联 AdminUserDO 的 id 字段
     */
    private Long userId;
    /**
     * 好友用户编号
     * <p>
     * 关联 AdminUserDO 的 id 字段
     */
    private Long friendUserId;
    /**
     * 是否免打扰
     */
    private Boolean silent;
    /**
     * 好友展示备注
     */
    private String displayName;
    /**
     * 添加来源
     * <p>
     * 枚举 {@link com.fly.system.api.im.enums.friend.ImFriendAddSourceEnum}
     */
    private Integer addSource;
    /**
     * 是否置顶联系人
     */
    private Boolean pinned;
    /**
     * 是否拉黑（弱关联 friend，单边屏蔽对方私聊消息）
     */
    private Boolean blocked;
    /**
     * 好友状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 添加好友时间
     */
    private LocalDateTime addTime;
    /**
     * 删除好友时间
     * <p>
     * 不为 null 时表示已删除
     */
    private LocalDateTime deleteTime;

}

package com.fly.system.api.im.domain.conversation;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * IM 会话读位置 DO
 * <p>
 * 只表达「用户在某个会话的最大已读位置」，私聊 / 群聊 / 频道统一落这张表，是读位置的唯一权威。
 *
 */
@TableName("im_conversation_read")
@KeySequence("im_conversation_read_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImConversationRead extends BaseEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     *
     * 关联 AdminUserDO 的 id 字段
     */
    private Long userId;
    /**
     * 会话类型
     * <p>
     * 枚举 ImConversationTypeEnum
     */
    private Integer conversationType;
    /**
     * 目标编号
     * <p>
     * 私聊关联 AdminUserDO 的 id；群聊关联 {@link ImGroup#getId()}；频道关联 {@link ImChannel#getId()}
     */
    private Long targetId;
    /**
     * 最大已读消息编号
     * <p>
     * 关联 {@link ImPrivateMessage#getId()}、{@link ImGroupMessage#getId()}、{@link ImChannelMessage#getId()}
     */
    private Long messageId;
    /**
     * 最近已读时间
     */
    private LocalDateTime readTime;

}

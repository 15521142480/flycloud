package com.fly.ai.chat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 统一 AI 聊天会话。
 * <p>
 * 会话是用户的历史记录和 Redis 短期记忆的共同边界；会话内的每条消息由 {@link AiMessage} 单独保存。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_conversation")
public class AiConversation extends BaseEntity {

    /** 对外暴露的 UUID 会话标识，避免前端处理大整数精度问题。 */
    @TableId(type = IdType.INPUT)
    private String id;

    /** 会话所属用户编号。 */
    private Long userId;

    /** 会话列表展示标题，由首条用户消息生成，后续可扩展为模型自动摘要。 */
    private String title;

    /** 最后一条消息时间，用于会话列表倒序展示。 */
    private LocalDateTime lastMessageTime;
}

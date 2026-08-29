package com.fly.ai.chat.model;

import java.time.LocalDateTime;

/**
 * 会话列表项。
 *
 * @param conversationId 会话标识
 * @param title 会话标题
 * @param lastMessageTime 最后消息时间
 * @param createTime 创建时间
 * @author lxs
 * @date 2026-08-28
 */
public record AiConversationSummary(String conversationId, String title, LocalDateTime lastMessageTime,
        LocalDateTime createTime) {
}

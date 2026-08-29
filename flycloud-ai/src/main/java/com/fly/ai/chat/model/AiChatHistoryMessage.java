package com.fly.ai.chat.model;

import com.fly.ai.common.model.AiPermission;
import com.fly.ai.common.model.AiUsage;
import com.fly.ai.common.knowledge.model.AiKnowledgeHit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对前端展示的历史消息。
 *
 * @param id 消息标识
 * @param role 消息角色
 * @param content 消息文本
 * @param usage Token 用量
 * @param permission 工具授权结果
 * @param toolNames 实际调用工具
 * @param knowledgeReferences 实际检索到的知识库片段
 * @param status 消息状态
 * @param createTime 创建时间
 * @author lxs
 * @date 2026-08-28
 */
public record AiChatHistoryMessage(String id, String role, String content, AiUsage usage,
        AiPermission permission, List<String> toolNames, List<AiKnowledgeHit> knowledgeReferences,
        String status, LocalDateTime createTime) {
}

package com.fly.ai.chat.model;

import com.fly.ai.common.model.AiPermission;
import com.fly.ai.common.model.AiUsage;
import com.fly.ai.common.knowledge.model.AiKnowledgeHit;

import java.util.List;

/**
 * 正式统一聊天响应。
 *
 * @param conversationId 本次所属会话标识
 * @param responseId 模型响应标识
 * @param model 实际调用模型
 * @param content 模型输出
 * @param usage Token 用量
 * @param permission 工具调用授权结果
 * @param toolNames 实际调用的工具名称
 * @param knowledgeReferences 实际命中的知识库片段
 * @author lxs
 * @date 2026-08-28
 */
public record AiUnifiedChatResponse(String conversationId, String responseId, String model, String content,
        AiUsage usage, AiPermission permission, List<String> toolNames, List<AiKnowledgeHit> knowledgeReferences) {
}

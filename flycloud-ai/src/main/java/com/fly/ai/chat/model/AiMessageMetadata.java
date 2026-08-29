package com.fly.ai.chat.model;

import com.fly.ai.common.model.AiPermission;
import com.fly.ai.common.knowledge.model.AiKnowledgeHit;

import java.util.List;

/**
 * 当前阶段写入消息扩展字段的结构。
 * <p>
 * 后续 RAG 引用、Agent 步骤和 MCP 调用轨迹均可扩展到同一 JSON 字段，避免提前固化大量专用列。
 *
 * @param permission 工具授权结果
 * @param toolNames 实际调用工具名称
 * @param knowledgeReferences 实际命中的知识库片段
 * @author lxs
 * @date 2026-08-28
 */
public record AiMessageMetadata(AiPermission permission, List<String> toolNames,
        List<AiKnowledgeHit> knowledgeReferences) {
}

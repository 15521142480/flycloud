package com.fly.ai.common.rag.model;

import com.fly.ai.common.knowledge.model.AiKnowledgeHit;
import com.fly.ai.common.model.AiChatResponse;

import java.util.List;

/**
 * RAG 回答与其实际引用的 Qdrant 检索结果。
 *
 * @param answer 模型回答
 * @param references 检索到的知识片段
 * @author lxs
 * @date 2026-08-28
 */
public record AiRagChatResponse(AiChatResponse answer, List<AiKnowledgeHit> references) {
}

package com.fly.ai.common.rag.model;

import com.fly.ai.common.knowledge.model.AiKnowledgeHit;

import java.util.List;

/**
 * 一次 RAG 检索生成的受控模型上下文。
 *
 * @param systemPrompt 注入模型的知识库上下文；没有命中时为空
 * @param references 实际命中的知识片段，用于接口返回与消息审计
 * @author lxs
 * @date 2026-08-28
 */
public record AiRagContext(String systemPrompt, List<AiKnowledgeHit> references) {
}

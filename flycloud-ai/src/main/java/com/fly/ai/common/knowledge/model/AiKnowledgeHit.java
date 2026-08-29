package com.fly.ai.common.knowledge.model;

import java.util.Map;

/**
 * Qdrant 检索命中的知识片段。
 *
 * @param id Chunk 标识
 * @param content 片段文本
 * @param score 相似度分数
 * @param metadata 文档元数据
 * @author lxs
 * @date 2026-08-28
 */
public record AiKnowledgeHit(String id, String content, Double score, Map<String, Object> metadata) {
}

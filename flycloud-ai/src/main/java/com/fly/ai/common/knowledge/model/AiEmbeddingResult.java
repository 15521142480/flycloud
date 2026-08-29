package com.fly.ai.common.knowledge.model;

/**
 * 文本向量化结果。
 *
 * @param text 原始文本
 * @param dimensions 向量维度
 * @param vector 真实 Embedding 向量
 * @author lxs
 * @date 2026-08-28
 */
public record AiEmbeddingResult(String text, int dimensions, float[] vector) {
}

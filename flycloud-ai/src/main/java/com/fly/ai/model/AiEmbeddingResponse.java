package com.fly.ai.model;

import java.util.List;

/**
 * 文本向量化响应。
 *
 * @param model 实际模型
 * @param embedding 向量数据
 * @param usage Token 用量
 */
public record AiEmbeddingResponse(String model, List<Float> embedding, EmbeddingUsage usage) {

    /**
     * Embedding 接口返回的 Token 用量。
     */
    public record EmbeddingUsage(long promptTokens, long totalTokens) {
    }

}

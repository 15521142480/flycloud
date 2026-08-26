package com.fly.ai.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 文本向量化请求。
 *
 * @param input 待向量化文本
 * @param model 可选向量模型名称，未传时使用 Nacos 默认配置
 */
public record AiEmbeddingRequest(
        @NotBlank(message = "向量化文本不能为空") @Size(max = 20_000, message = "向量化文本不能超过 20000 个字符") String input,
        @Size(max = 100, message = "模型名称不能超过 100 个字符") String model) {
}

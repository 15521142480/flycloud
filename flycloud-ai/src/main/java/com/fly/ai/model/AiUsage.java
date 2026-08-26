package com.fly.ai.model;

/**
 * 模型调用 Token 用量。
 *
 * @param inputTokens 输入 Token 数
 * @param outputTokens 输出 Token 数
 * @param totalTokens 总 Token 数
 */
public record AiUsage(long inputTokens, long outputTokens, long totalTokens) {
}

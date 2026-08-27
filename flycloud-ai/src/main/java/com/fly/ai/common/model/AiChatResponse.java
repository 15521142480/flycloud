package com.fly.ai.common.model;

/**
 * 聊天响应。
 *
 * @param responseId 供应商响应 ID
 * @param model 实际模型
 * @param content 模型文本输出
 * @param usage Token 用量
 */
public record AiChatResponse(String responseId, String model, String content, AiUsage usage) {
}

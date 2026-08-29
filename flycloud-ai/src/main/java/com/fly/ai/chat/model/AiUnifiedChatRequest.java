package com.fly.ai.chat.model;

import com.fly.ai.common.model.AiChatRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 正式统一聊天请求。
 *
 * @param conversationId 可选会话标识，未传时由服务端创建新会话
 * @param message 用户消息
 * @param model 可选模型名称
 * @param maxOutputTokens 可选最大输出 Token
 * @author lxs
 * @date 2026-08-28
 */
public record AiUnifiedChatRequest(
        @Size(max = 36, message = "conversationId 长度不能超过 36") String conversationId,
        @NotBlank(message = "聊天消息不能为空") @Size(max = 20_000, message = "聊天消息不能超过 20000 个字符") String message,
        @Size(max = 100, message = "模型名称不能超过 100 个字符") String model,
        @Min(value = 1, message = "maxOutputTokens 必须大于 0") @Max(value = 16_384, message = "maxOutputTokens 不能超过 16384") Integer maxOutputTokens) {

    /**
     * 转换为现有模型调用层复用的聊天请求。
     *
     * @return 原有统一聊天请求
     */
    public AiChatRequest toChatRequest() {
        return new AiChatRequest(message, model, maxOutputTokens);
    }
}

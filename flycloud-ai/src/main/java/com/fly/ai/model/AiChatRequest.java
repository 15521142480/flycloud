package com.fly.ai.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 聊天请求。
 *
 * @param message 用户输入
 * @param model 可选模型名称，未传时使用 Nacos 默认配置
 * @param maxOutputTokens 可选最大输出 Token，未传时使用 Nacos 默认配置
 */
public record AiChatRequest(
        @NotBlank(message = "聊天消息不能为空") @Size(max = 20_000, message = "聊天消息不能超过 20000 个字符") String message,
        @Size(max = 100, message = "模型名称不能超过 100 个字符") String model,
        @Min(value = 1, message = "maxOutputTokens 必须大于 0") @Max(value = 16_384, message = "maxOutputTokens 不能超过 16384") Integer maxOutputTokens) {
}

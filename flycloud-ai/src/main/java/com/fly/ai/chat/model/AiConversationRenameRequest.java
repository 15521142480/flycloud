package com.fly.ai.chat.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * AI 会话重命名请求。
 *
 * @author lxs
 * @date 2026-09-04
 */
public record AiConversationRenameRequest(
        @NotBlank(message = "会话名称不能为空")
        @Size(max = 40, message = "会话名称不能超过 40 个字符")
        String title) {
}

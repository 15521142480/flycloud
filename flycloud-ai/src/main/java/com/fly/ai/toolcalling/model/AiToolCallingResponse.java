package com.fly.ai.toolcalling.model;

import com.fly.ai.common.model.AiPermission;
import com.fly.ai.common.model.AiUsage;

import java.util.List;

/**
 * Tool Calling 聊天响应。
 *
 * @param responseId 模型响应编号
 * @param model 实际模型名称
 * @param content 最终展示文本；权限说明由 {@code permission} 字段单独承载
 * @param usage Token 用量
 * @param permission 工具调用权限结果，未调用工具时为 {@code null}
 * @param toolNames 本次实际调用的工具名称
 * @author lxs
 * @date 2026-08-27
 */
public record AiToolCallingResponse(
        String responseId,
        String model,
        String content,
        AiUsage usage,
        AiPermission permission,
        List<String> toolNames) {
}

package com.fly.ai.common.agent.model;

import com.fly.ai.common.knowledge.model.AiKnowledgeHit;
import com.fly.ai.common.tool.model.AiToolCallingResponse;

import java.util.List;

/**
 * Agent 执行结果。
 *
 * @param response 复用 Tool Calling 的模型、权限、Token 与工具调用结果
 * @param knowledgeReferences 本轮真实检索到的知识库片段
 * @author lxs
 * @date 2026-08-28
 */
public record AiAgentResponse(AiToolCallingResponse response, List<AiKnowledgeHit> knowledgeReferences) {
}

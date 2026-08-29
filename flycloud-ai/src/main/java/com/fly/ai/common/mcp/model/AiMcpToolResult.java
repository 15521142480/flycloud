package com.fly.ai.common.mcp.model;

/**
 * MCP Client 实际调用结果。
 *
 * @param toolName MCP 工具名称
 * @param content MCP Server 返回的文本内容
 * @param error MCP Server 是否标记本次工具调用为错误
 * @author lxs
 * @date 2026-08-28
 */
public record AiMcpToolResult(String toolName, String content, boolean error) {
}

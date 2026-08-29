package com.fly.ai.common.mcp.service;

import com.fly.ai.common.mcp.model.AiMcpToolResult;
import com.fly.common.exception.AiProviderException;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 内部 MCP Client 调用服务。
 * <p>
 * 使用 Spring AI 自动装配的 {@link McpSyncClient} 通过真实 Streamable HTTP 协议调用 MCP Server；不直接
 * 调用 {@code AiBusinessTools}，从而便于观察完整的 Client → Server → Tool 链路。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AiMcpClientService {

    private static final String USER_TOOL = "query_system_user_by_id";

    private static final String ORDER_TOOL = "query_mall_order_by_id_or_no";

    private final List<McpSyncClient> mcpSyncClients;

    /**
     * 通过 MCP 查询系统用户公开信息。
     *
     * @param userId 用户编号
     * @return MCP 实际调用结果
     */
    public AiMcpToolResult querySystemUserById(Long userId) {
        return call(USER_TOOL, Map.of("userId", userId));
    }

    /**
     * 通过 MCP 查询订单摘要。
     *
     * @param idOrNo 订单数据库主键或流水号
     * @return MCP 实际调用结果
     */
    public AiMcpToolResult queryMallOrderByIdOrNo(String idOrNo) {
        return call(ORDER_TOOL, Map.of("idOrNo", idOrNo));
    }

    /**
     * 调用已配置的首个内部 MCP Server。
     *
     * @param toolName 工具名称
     * @param arguments 工具参数
     * @return MCP 调用结果
     */
    private AiMcpToolResult call(String toolName, Map<String, Object> arguments) {
        McpSyncClient client = mcpSyncClients.stream().findFirst()
                .orElseThrow(() -> new AiProviderException(503, "未配置可用的 AI MCP Client"));
        try {
            synchronized (client) {
                if (!client.isInitialized()) {
                    client.initialize();
                }
            }
            McpSchema.CallToolResult result = client.callTool(new McpSchema.CallToolRequest(toolName, arguments));
            log.info("AI MCP Client 调用完成，tool={}, error={}", toolName, result.isError());
            return new AiMcpToolResult(toolName, result.content().stream().map(this::contentText)
                    .reduce((left, right) -> left + "\n" + right).orElse(""), Boolean.TRUE.equals(result.isError()));
        } catch (RuntimeException exception) {
            throw new AiProviderException(502, "AI MCP Client 调用失败", exception);
        }
    }

    /**
     * 读取 MCP 文本内容；非文本内容保留其字符串表示，避免静默丢失 Server 响应。
     *
     * @param content MCP 内容块
     * @return 可展示文本
     */
    private String contentText(McpSchema.Content content) {
        return content instanceof McpSchema.TextContent textContent ? textContent.text() : String.valueOf(content);
    }
}

package com.fly.ai.common.mcp.server;

import com.fly.ai.common.tool.model.AiToolAuthorizationTrace;
import com.fly.ai.common.tool.tool.AiBusinessTools;
import com.fly.ai.common.utils.AiSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 对外暴露的内部 MCP Server 工具。
 * <p>
 * 本类只做 MCP 协议适配，业务查询和资源鉴权全部复用 {@link AiBusinessTools}。MCP 调用的身份由
 * {@link AiMcpConfiguration} 转发的 Bearer Token 经 Spring Security 解析，绝不信任 MCP 参数中的身份。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Component
@RequiredArgsConstructor
public class AiMcpBusinessTools {

    private final AiBusinessTools businessTools;

    /**
     * 通过 MCP 查询系统用户公开信息。
     *
     * @param userId 用户编号
     * @return 脱敏用户信息
     */
    @McpTool(name = "query_system_user_by_id", description = "根据用户ID查询系统用户的公共信息，不返回密码、手机号、邮箱等敏感字段。")
    public Object querySystemUserById(Long userId) {
        return businessTools.querySystemUserById(userId, toolContext());
    }

    /**
     * 通过 MCP 查询商城订单摘要。
     *
     * @param idOrNo 订单数据库主键或流水号
     * @return 订单摘要或无权限说明
     */
    @McpTool(name = "query_mall_order_by_id_or_no", description = "根据商城订单数据库主键或订单流水号查询订单摘要。仅当前订单用户或超级管理员可查看。")
    public Object queryMallOrderByIdOrNo(String idOrNo) {
        return businessTools.queryMallOrderByIdOrNo(idOrNo, toolContext());
    }

    /**
     * 构建 MCP Server 调用既有业务工具所需的服务端上下文。
     *
     * @return 只包含当前认证用户和本次授权轨迹的工具上下文
     */
    private ToolContext toolContext() {
        return new ToolContext(Map.of(
                AiBusinessTools.LOGIN_USER_ID_CONTEXT_KEY, AiSecurityUtils.requiredLoginUserId("AI MCP 工具"),
                AiBusinessTools.AUTHORIZATION_TRACE_CONTEXT_KEY, new AiToolAuthorizationTrace()));
    }
}

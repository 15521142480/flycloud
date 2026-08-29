package com.fly.ai.demo.mcp.controller;

import com.fly.ai.common.mcp.model.AiMcpToolResult;
import com.fly.ai.common.mcp.service.AiMcpClientService;
import com.fly.ai.common.utils.AiSecurityUtils;
import com.fly.common.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第 9 步 MCP 学习测试控制器。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Tag(name = "AI MCP 测试")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/demo/mcp")
public class AiMcpDemoController {

    private final AiMcpClientService mcpClientService;

    /**
     * 通过 MCP Client 调用用户查询工具。
     *
     * @param userId 用户编号
     * @return MCP Server 工具结果
     */
    @Operation(summary = "MCP 用户查询测试")
    @GetMapping("/user")
    public R<AiMcpToolResult> queryUser(@Positive @RequestParam Long userId) {
        AiSecurityUtils.requiredLoginUserId("AI MCP");
        return R.ok(mcpClientService.querySystemUserById(userId));
    }

    /**
     * 通过 MCP Client 调用订单查询工具。
     *
     * @param idOrNo 订单数据库主键或流水号
     * @return MCP Server 工具结果
     */
    @Operation(summary = "MCP 订单查询测试")
    @GetMapping("/order")
    public R<AiMcpToolResult> queryOrder(@NotBlank @RequestParam String idOrNo) {
        AiSecurityUtils.requiredLoginUserId("AI MCP");
        return R.ok(mcpClientService.queryMallOrderByIdOrNo(idOrNo));
    }
}

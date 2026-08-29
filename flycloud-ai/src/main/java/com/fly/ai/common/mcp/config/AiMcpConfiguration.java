package com.fly.ai.common.mcp.config;

import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * MCP 内部客户端配置。
 * <p>
 * 内部 MCP Client 调用时原样转发当前请求的 Bearer Token，MCP Server 因而可复用项目既有 Spring Security
 * 认证和订单资源二次鉴权；不允许由模型或请求参数传入 userId、角色等身份信息。
 *
 * @author lxs
 * @date 2026-08-28
 */
@Configuration(proxyBeanMethods = false)
public class AiMcpConfiguration {

    /**
     * 为同进程或跨服务的 HTTP MCP 调用转发当前认证头。
     *
     * @return MCP HTTP 请求定制器
     */
    @Bean
    public McpSyncHttpClientRequestCustomizer mcpSyncHttpClientRequestCustomizer() {
        return (builder, method, uri, body, context) -> {
            if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
                return;
            }
            HttpServletRequest request = attributes.getRequest();
            String authorization = request.getHeader("Authorization");
            if (authorization != null && !authorization.isBlank()) {
                builder.header("Authorization", authorization);
            }
        };
    }
}

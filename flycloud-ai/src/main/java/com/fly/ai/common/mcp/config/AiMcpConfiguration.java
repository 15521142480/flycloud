package com.fly.ai.common.mcp.config;

import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import io.modelcontextprotocol.common.McpTransportContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

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

    private static final String AUTHORIZATION_CONTEXT_KEY = "authorization";

    /**
     * 在 MCP 同步客户端每次操作开始时捕获当前请求认证信息。
     * <p>
     * MCP HTTP 传输会在异步链路中实际发包，不能在请求定制器中直接读取 {@link RequestContextHolder}；认证头必须先
     * 由 SDK 的 {@link McpTransportContext} 带入传输层，才能保证初始化、工具调用等全部 MCP 请求均携带当前用户身份。
     *
     * @return MCP 同步客户端定制器
     */
    @Bean
    public McpSyncClientCustomizer mcpSyncClientAuthenticationContextCustomizer() {
        return (name, spec) -> spec.transportContextProvider(this::currentRequestTransportContext);
    }

    /**
     * 为同进程或跨服务的 HTTP MCP 调用转发已捕获的认证头。
     *
     * @return MCP HTTP 请求定制器
     */
    @Bean
    public McpSyncHttpClientRequestCustomizer mcpSyncHttpClientRequestCustomizer() {
        return (builder, method, uri, body, context) -> {
            Object authorizationValue = context.get(AUTHORIZATION_CONTEXT_KEY);
            String authorization = authorizationValue instanceof String value ? value : null;
            if (authorization != null && !authorization.isBlank()) {
                builder.header("Authorization", authorization);
            }
        };
    }

    /**
     * 从当前 Web 请求提取认证头，并转换为 MCP 传输上下文。
     *
     * @return 当前请求的 MCP 传输上下文；非 Web 请求时为空上下文
     */
    private McpTransportContext currentRequestTransportContext() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return McpTransportContext.EMPTY;
        }
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");
        return authorization == null || authorization.isBlank() ? McpTransportContext.EMPTY
                : McpTransportContext.create(Map.of(AUTHORIZATION_CONTEXT_KEY, authorization));
    }
}

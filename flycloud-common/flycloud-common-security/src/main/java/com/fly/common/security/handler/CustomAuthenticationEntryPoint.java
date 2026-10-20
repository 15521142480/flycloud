package com.fly.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.common.domain.model.ApiCode;
import com.fly.common.domain.model.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 资源服务未认证响应入口，统一返回 401 和明确的令牌失效提示。
 *
 * @author lxs
 * @date 2026/5/4
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 输出未认证响应，供客户端结束失效会话并重新登录。
     *
     * @param request 当前 HTTP 请求
     * @param response 当前 HTTP 响应
     * @param authException 当前认证失败原因
     * @throws IOException 写入认证失败响应时抛出
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(),
                R.failed(ApiCode.INVALID_TOKEN, ApiCode.INVALID_TOKEN.getMessage()));
    }
}

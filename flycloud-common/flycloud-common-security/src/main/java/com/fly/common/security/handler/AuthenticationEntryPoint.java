package com.fly.common.security.handler;

import com.fly.common.model.R;
import com.fly.common.utils.ResponseUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 认证不合法
 */
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
		int status = HttpServletResponse.SC_UNAUTHORIZED;
		ResponseUtils.responseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, R.failed(status, "访问令牌不合法"));
	}
}

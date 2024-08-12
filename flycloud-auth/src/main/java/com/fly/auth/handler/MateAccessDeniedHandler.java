package com.fly.auth.handler;

import com.fly.common.model.R;
import com.fly.common.utils.ResponseUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拒接访问-无权限
 */
public class MateAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

		ResponseUtils.responseWriter(httpServletResponse, MediaType.APPLICATION_JSON_VALUE, HttpServletResponse.SC_FORBIDDEN, R.failed("没有访问权限"));
	}
}

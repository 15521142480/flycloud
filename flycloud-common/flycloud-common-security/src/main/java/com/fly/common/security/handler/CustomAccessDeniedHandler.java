package com.fly.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.common.model.ApiCode;
import com.fly.common.model.R;
import com.fly.common.utils.ResponseUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义无权限异常
 *
 * @author lxs
 * @date 2023/5/4
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ResponseUtils.responseWriter(response, MediaType.APPLICATION_JSON_VALUE, HttpServletResponse.SC_FORBIDDEN, R.failed("没有访问权限"));

//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(response.getOutputStream(), R.failed(ApiCode.ACCESS_UNAUTHORIZED));
    }

}

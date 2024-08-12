package com.fly.common.security.handler;


import com.fly.common.enums.ResultCodeEnum;
import com.fly.common.model.R;
import com.fly.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败/认证成功/退出登录 - 处理器
 *
 * @author: lxs
 * @date: 2024/8/7
 */
@Slf4j
@Component
public class AuthServerAccessDeniedHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler, LogoutSuccessHandler {


//    旧版写法： 实现ServerAccessDeniedHandler类重写handler方法
//    /**
//     * 自定义响应
//     */
//    @Override
//    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
//
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatus.FORBIDDEN);
//        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        response.getHeaders().set("Access-Control-Allow-Origin", "*");
//        response.getHeaders().set("Cache-Control", "no-cache");
//        String body = JsonUtils.toJsonString(R.failed(ResultCodeEnum.NO_PERMISSION));
//        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
//        return response.writeWith(Mono.just(buffer)).doFinally(s -> {
//            DataBufferUtils.release(buffer);
//        });
//    }


    /**
     * 认证失败
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        log.error("=====认证失败：{}", e.getMessage());

//        response.reset();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        String contentStr = JsonUtils.toJsonString(R.failed(ResultCodeEnum.NO_PERMISSION));

        response.getWriter().write(contentStr);
    }



    /**
     * 认证失败
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }



    /**
     * 认证失败
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }



}

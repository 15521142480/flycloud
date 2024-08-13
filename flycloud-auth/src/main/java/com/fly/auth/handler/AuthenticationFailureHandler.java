package com.fly.auth.handler;

import com.fly.common.enums.ResultCodeEnum;
import com.fly.common.model.R;
import com.fly.common.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败的回调
 * 
 */
@Slf4j
public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
    
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
      
        R<?> result = null;
        String username = request.getParameter("username");
        if (exception instanceof AccountExpiredException) {
            // 账号过期
            log.info("[登录失败] - 用户[{}]账号过期", username);
            result = R.failed(ResultCodeEnum.USER_ACCOUNT_EXPIRED);

        } else if (exception instanceof BadCredentialsException) {
            // 密码错误
            log.info("[登录失败] - 用户[{}]密码错误", username);
            result = R.failed(ResultCodeEnum.INVALID_USERNAME_PASSWORD);

        } else if (exception instanceof CredentialsExpiredException) {
            // 密码过期
            log.info("[登录失败] - 用户[{}]密码过期", username);
            result = R.failed(ResultCodeEnum.USER_PASSWORD_EXPIRED);

        } else if (exception instanceof DisabledException) {
            // 用户被禁用
            log.info("[登录失败] - 用户[{}]被禁用", username);
            result = R.failed(ResultCodeEnum.DISABLED_ACCOUNT);

        } else if (exception instanceof LockedException) {
            // 用户被锁定
            log.info("[登录失败] - 用户[{}]被锁定", username);
            result = R.failed(ResultCodeEnum.PASSWORD_TRY_MAX_ERROR);

        } else if (exception instanceof InternalAuthenticationServiceException) {
            // 内部错误
            log.error(String.format("[登录失败] - [%s]内部错误", username));
            result = R.failed(ResultCodeEnum.USER_LOGIN_FAIL);

        } else {
            // 其他错误
            log.error(String.format("[登录失败] - [%s]其他错误", username), exception);
            result = R.failed(ResultCodeEnum.USER_LOGIN_FAIL);
        }

        ResponseUtils.responseWriter(response, "UTF-8", HttpStatus.UNAUTHORIZED.value(), result);
    }
}

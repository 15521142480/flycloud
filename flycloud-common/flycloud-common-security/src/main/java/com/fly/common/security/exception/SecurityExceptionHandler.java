package com.fly.common.security.exception;

import com.fly.common.domain.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 安全异常捕获处理
 *
 * @author lxs
 * @date 2024/9/4
 */
@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler {


    /**
     * 无权限异常处理
     *
     * @param e the e
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
            AccessDeniedException.class,
            AuthorizationDeniedException.class
    })
    public R handleAccessDeniedException(AccessDeniedException e) {

        log.error("无权限异常==>exception:{}", e.getMessage());
        return R.failed("无权限访问！");
    }


}

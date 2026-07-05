package com.fly.common.handler;

import com.fly.common.constant.CommonConstants;
import com.fly.common.constant.WebConstants;
import com.fly.common.exception.TokenException;
import com.fly.common.domain.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获处理
 *
 * @author lxs
 * @date 2026/5/3
 */
@Slf4j
@RestControllerAdvice // 等于@ResponseBody和 + ControllerAdvice；默认拦截所有controller (可指定包，如basePackages = "com.xxx.controller"))
public class GlobalExceptionHandler {

    // 注：安全异常信息在security包下的SecurityExceptionHandler拦截


    /**
     * 全局异常处理
     *
     * @param e the e
     */
    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleGlobalException(Exception e) throws Exception {

        log.error("=====全局异常信息: {}", e.getMessage(), e);
        return R.builder()
                .msg(e.getLocalizedMessage())
                .code(CommonConstants.FAIL)
                .build();
    }


    /**
     * 登录授权相关异常处理
     *
     * @param e 所属异常
     */
    @ExceptionHandler(TokenException.class)
    public R<?> handleTokenException(HttpServletRequest request, TokenException e) {

        log.error("授权相关异常==>errorCode:{}, exception:{}", HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return R.failed(WebConstants.Status.UNAUTHORIZED.getCode(), e.getMessage());
    }


    /**
     * 请求参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null
                ? fieldError.getDefaultMessage()
                : "请求参数校验失败";

        log.error("=====请求参数校验异常: {}", message);

        return R.failed(message);
    }


}

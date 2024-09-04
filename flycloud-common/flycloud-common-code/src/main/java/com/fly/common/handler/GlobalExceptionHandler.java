package com.fly.common.handler;

import com.fly.common.constant.CommonConstants;
import com.fly.common.constant.WebConstants;
import com.fly.common.exception.TokenException;
import com.fly.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获处理
 *
 * @author lxs
 * @date 2023/5/3
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
//    @ExceptionHandler(TokenException.class)
////    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public R handleTokenException(TokenException e) {
//
//        log.error("=====授权相关异常: {}", e.getMessage(), e);
//        return R.failed(e.getMessage());
//    }

    @ExceptionHandler(TokenException.class)
    public R<?> handleTokenException(HttpServletRequest request, TokenException e) {

        log.error("授权相关异常==>errorCode:{}, exception:{}", HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return R.failed(WebConstants.Status.UNAUTHORIZED.getCode(), e.getMessage());
    }


    /**
     * 接口权限相关异常处理
     *
     * @param e 所属异常
     */
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public R handleAuthException(Exception e) {
//
//        log.error("=====xxx: {}", e.getMessage(), e);
//        return R.failed(e.getMessage());
//    }



}

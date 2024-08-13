package com.fly.common.handler;

import com.fly.common.constant.CommonConstants;
import com.fly.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获处理
 *
 * @author lxs
 * @date 2023/5/3
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 全局异常
     *
     * @param e the e
     * @return R
     */
    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleGlobalException(Exception e) {

        log.error("=====全局异常信息: {}", e.getMessage(), e);
        return R.builder()
                .msg(e.getLocalizedMessage())
                .code(CommonConstants.FAIL)
                .build();
    }



}

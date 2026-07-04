package com.fly.common.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * 业务异常
 *
 * @author: lxs
 * @date: 2026/03/03
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    // 错误码
    private Integer code;


    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException setCode(Integer code) {
        this.code = code;
        return this;
    }

}

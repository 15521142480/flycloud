package com.fly.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 授权异常
 *
 * @author: lxs
 * @date: 2025/11/23
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class AuthException extends RuntimeException {

    // 错误码
    private Integer code;


    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AuthException setCode(Integer code) {
        this.code = code;
        return this;
    }

}

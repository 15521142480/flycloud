package com.fly.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 授权异常
 *
 * @author: lxs
 * @date: 2024/11/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthException extends RuntimeException {

    /**
     * 业务错误码
     *
     */
    @Getter
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public AuthException() {
    }


    public AuthException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public AuthException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public AuthException setMessage(String message) {
        this.message = message;
        return this;
    }

}

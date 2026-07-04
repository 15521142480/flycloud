package com.fly.common.exception.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 用户信息异常类
 *
 * @author lxs
 * @date 2026/5/2
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class UserException extends RuntimeException {


    // 错误码
    private Integer code;


    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public UserException setCode(Integer code) {
        this.code = code;
        return this;
    }
}

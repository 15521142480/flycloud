package com.fly.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author lxs
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public final class ServiceException extends RuntimeException {

    // 错误码
    private Integer code;


    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException setCode(Integer code) {
        this.code = code;
        return this;
    }
}


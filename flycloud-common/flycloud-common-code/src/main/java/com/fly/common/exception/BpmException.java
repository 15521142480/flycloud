package com.fly.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * bpm异常
 *
 * @author: lxs
 * @date: 2026/03/03
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class BpmException extends RuntimeException {

    // 错误码
    private Integer code;


    public BpmException() {
    }

    public BpmException(String message) {
        super(message);
    }

    public BpmException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BpmException setCode(Integer code) {
        this.code = code;
        return this;
    }


}

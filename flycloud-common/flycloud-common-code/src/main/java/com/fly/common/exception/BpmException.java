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
@Data
@EqualsAndHashCode(callSuper = true)
public class BpmException extends RuntimeException {

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
    public BpmException() {
    }


    public BpmException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BpmException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BpmException setMessage(String message) {
        this.message = message;
        return this;
    }

}

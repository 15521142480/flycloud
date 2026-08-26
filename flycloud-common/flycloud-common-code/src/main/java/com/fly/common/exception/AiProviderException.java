package com.fly.common.exception;

import lombok.Getter;

/**
 * 模型供应商调用失败。
 *
 * @author lxs
 * @date 2026-08-25
 */
@Getter
public class AiProviderException extends RuntimeException {

    private final int statusCode;

    public AiProviderException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public AiProviderException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

}

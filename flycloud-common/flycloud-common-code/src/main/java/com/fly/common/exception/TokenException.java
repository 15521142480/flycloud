package com.fly.common.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Token处理异常
 *
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class TokenException extends RuntimeException {

	// 错误码
	private Integer code;


	public TokenException() {
	}

	public TokenException(String message) {
		super(message);
	}

	public TokenException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	public TokenException setCode(Integer code) {
		this.code = code;
		return this;
	}
}

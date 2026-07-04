package com.fly.common.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 验证码异常
 *
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class CaptchaException extends RuntimeException {

//	private static final long serialVersionUID = -6550886498142636261L;

	// 错误码
	private Integer code;


	public CaptchaException() {
	}

	public CaptchaException(String message) {
		super(message);
	}

	public CaptchaException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	public CaptchaException setCode(Integer code) {
		this.code = code;
		return this;
	}
}

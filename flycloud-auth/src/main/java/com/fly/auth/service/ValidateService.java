package com.fly.auth.service;

import com.fly.common.exception.CaptchaException;
import com.fly.common.model.R;

/**
 * 验证码业务类
 *
 */
public interface ValidateService {


	/**
	 * 获取验证码
	 *
	 * @return Result
	 */
	R<?> getCode();

	/**
	 * 获取短信验证码
	 *
	 * @param mobile 手机号码
	 * @return Result
	 */
	R<?> getSmsCode(String mobile);

	/**
	 * 校验验证码
	 *
	 * @param key  　KEY
	 * @param code 验证码
	 * @throws CaptchaException 验证码异常
	 */
	void check(String key, String code) throws CaptchaException;
}

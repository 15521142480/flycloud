package com.fly.auth.service;

import com.fly.auth.domain.bo.ImageTextClickCaptchaVerifyBo;
import com.fly.auth.domain.vo.ImageTextClickCaptchaVerifyVo;
import com.fly.auth.domain.vo.ImageTextClickCaptchaVo;
import com.fly.common.exception.CaptchaException;
import com.fly.common.domain.model.R;

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
	 * 获取图文点选验证码‌
	 *
	 */
	ImageTextClickCaptchaVo createImageTextClickCaptcha();


	/**
	 * 校验图文点选验证码‌
	 *
	 */
	ImageTextClickCaptchaVerifyVo verifyImageTextClickCaptcha(ImageTextClickCaptchaVerifyBo bo);


	/**
	 * 消费_图文点选验证码‌_证码‌信息
	 *
	 * 为了使用接口（如登录）不需要再做验证题目、坐标、复杂验证码等步骤，此接口是一次性消费验证，更干净、解耦
	 *
	*/
	void consumeImageTextClickCaptchaVerify(String captchaVerification);

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

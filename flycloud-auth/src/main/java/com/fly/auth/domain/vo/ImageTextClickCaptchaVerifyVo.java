package com.fly.auth.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 图文点选验证码校验结果
 *
 * @author: lxs
 * @date: 2026/6/27
 */
@Schema(description = "点选验证码校验结果")
@Data
public class ImageTextClickCaptchaVerifyVo {

    @Schema(description = "一次性验证码通过凭证")
    private String captchaVerification;

    @Schema(description = "凭证过期秒数")
    private Integer expiresInSeconds;
}

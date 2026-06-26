package com.fly.auth.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 图文点选验证码‌-vo
 *
 * @author: lxs
 * @date: 2026/6/27
 */
@Schema(description = "图文点选验证码")
@Data
public class ImageTextClickCaptchaVo {

    @Schema(description = "验证码题目ID")
    private String captchaId;

    @Schema(description = "验证码图片Base64")
    private String imageBase64;

    @Schema(description = "需要按顺序点击的文字")
    private String targetText;

    @Schema(description = "题目过期秒数")
    private Integer expiresInSeconds;
}

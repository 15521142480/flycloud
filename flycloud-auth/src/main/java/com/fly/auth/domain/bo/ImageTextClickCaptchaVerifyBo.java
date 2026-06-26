package com.fly.auth.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 图文点选验证码校验参数
 *
 * @author: lxs
 * @date: 2026/6/27
 */
@Schema(description = "图文点选验证码校验参数")
@Data
public class ImageTextClickCaptchaVerifyBo {

    @Schema(description = "验证码题目ID")
    @NotBlank(message = "验证码ID不能为空")
    private String captchaId;

    @Schema(description = "按提示顺序点击的3个坐标")
    @Valid
    @NotEmpty(message = "验证码坐标不能为空")
    @Size(min = 3, max = 3, message = "请依次点击3个文字")
    private List<ImageTextClickCaptchaPointBo> points;
}

package com.fly.auth.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 图文点选验证码‌坐标
 *
*/
@Schema(description = "图文点选验证码坐标")
@Data
public class ImageTextClickCaptchaPointBo {

    @Schema(description = "点击点在原图中的 x 坐标", example = "120")
    @Min(value = 0, message = "x坐标不能小于0")
    private Integer x;

    @Schema(description = "点击点在原图中的 y 坐标", example = "80")
    @Min(value = 0, message = "y坐标不能小于0")
    private Integer y;

}

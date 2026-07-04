package com.fly.auth.controller.captcha;

import com.fly.auth.domain.bo.ImageTextClickCaptchaVerifyBo;
import com.fly.auth.domain.vo.ImageTextClickCaptchaVerifyVo;
import com.fly.auth.domain.vo.ImageTextClickCaptchaVo;
import com.fly.auth.service.CaptchaService;
import com.fly.common.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码-控制层
 *
 * @author lxs
 * @date 2026/08/17
 */
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
@Slf4j
public class CaptchaController {

    private final CaptchaService captchaService;



    /**
     * 验证码获取
     */
    @GetMapping("/getCode")
    @Operation(summary = "验证码获取", description = "验证码获取")
    @Parameters({
            @Parameter(name = "Authorization", required = true,  description = "授权类型", in = ParameterIn.QUERY)
    })
    public R<?> authCode() {
        return captchaService.getCode();
    }


    /**
     * 获取图文点选验证码‌
     */
    @Operation(summary = "获取图文点选验证码")
    @PostMapping("/getImageTextClickCaptcha")
    public R<ImageTextClickCaptchaVo> createClickCaptcha() {
        return R.ok(captchaService.createImageTextClickCaptcha());
    }

    /**
     * 校验图文点选验证码‌
     */
    @Operation(summary = "校验图文点选验证码")
    @PostMapping("/checkGetImageTextClickCaptcha")
    public R<ImageTextClickCaptchaVerifyVo> verifyClickCaptcha(@Valid @RequestBody ImageTextClickCaptchaVerifyBo verifyDto) {
        return R.ok(captchaService.verifyImageTextClickCaptcha(verifyDto));
    }


}

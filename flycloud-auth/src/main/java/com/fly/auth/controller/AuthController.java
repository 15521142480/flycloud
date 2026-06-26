package com.fly.auth.controller;

import com.fly.auth.domain.bo.ImageTextClickCaptchaVerifyBo;
import com.fly.auth.domain.vo.ImageTextClickCaptchaVerifyVo;
import com.fly.auth.domain.vo.ImageTextClickCaptchaVo;
import com.fly.auth.service.ValidateService;
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
 * 认证中心-控制层
 *
 * @author lxs
 * @date 2024/08/17
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final ValidateService validateService;



    /**
     * 验证码获取
     */
    @GetMapping("/code")
    @Operation(summary = "验证码获取", description = "验证码获取")
    @Parameters({
            @Parameter(name = "Authorization", required = true,  description = "授权类型", in = ParameterIn.QUERY)
    })
    public R<?> authCode() {
        return validateService.getCode();
    }


    /**
     * 获取图文点选验证码‌
     */
    @Operation(summary = "获取图文点选验证码")
    @PostMapping("/getImageTextClickCaptcha")
    public R<ImageTextClickCaptchaVo> createClickCaptcha() {
        return R.ok(validateService.createImageTextClickCaptcha());
    }

    /**
     * 校验图文点选验证码‌
     */
    @Operation(summary = "校验图文点选验证码")
    @PostMapping("/checkGetImageTextClickCaptcha")
    public R<ImageTextClickCaptchaVerifyVo> verifyClickCaptcha(@Valid @RequestBody ImageTextClickCaptchaVerifyBo verifyDto) {
        return R.ok(validateService.verifyImageTextClickCaptcha(verifyDto));
    }


}

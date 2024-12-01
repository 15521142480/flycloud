package com.fly.auth.controller;

import com.fly.auth.service.ValidateService;
import com.fly.common.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

}

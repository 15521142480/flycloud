package com.fly.auth.controller.admin;

import com.fly.auth.domain.vo.AuthLoginRespVo;
import com.fly.auth.service.AuthService;
import com.fly.common.domain.model.R;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 后台授权中心-控制层
 *
 * @author lxs
 * @date 2026/7/4
 */
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 登录
     *
     * <p>
     * 除普通参数之外，header还需传Authorization，也就是oauth的客户端，格式是：Basic 客户端key:客户端secret，如 Basic Zmx5OmZseV9zZWNyZXQ=
     */
    @PostMapping("/token")
    public R<AuthLoginRespVo> getAccessToken(@RequestParam Map<String, String> loginParam) {
        return R.ok(authService.token(loginParam));
    }

    /**
     * 登录
     *
     * <p>
     * 除普通参数之外，header还需传Authorization，也就是oauth的客户端，格式是：Basic 客户端key:客户端secret，如 Basic Zmx5OmZseV9zZWNyZXQ=
    */
    @PostMapping("/token")
    @Parameters({
            @Parameter(name = "grant_type", required = true,  description = "授权类型", in = ParameterIn.QUERY),
            @Parameter(name = "username", required = true,  description = "用户名", in = ParameterIn.QUERY),
            @Parameter(name = "password", required = true,  description = "密码", in = ParameterIn.QUERY),
            @Parameter(name = "scope", required = true,  description = "使用范围", in = ParameterIn.QUERY),
            @Parameter(name = "captchaCode", required = false,  description = "验证码（授权类型使用验证码时必填）", in = ParameterIn.QUERY),
    })
    public R<AuthLoginRespVo> postAccessToken(@RequestParam Map<String, String> requestParam,
                                              @RequestBody(required = false) Map<String, String> requestBody) {
        Map<String, String> loginParam = new LinkedHashMap<>(requestParam);
        if (requestBody != null) {
            loginParam.putAll(requestBody);
        }
        return R.ok(authService.token(loginParam));
    }


    /**
     * 登出
     */
    @PostMapping("/logOut")
    @Operation(summary = "登出", description = "登出")
    @Parameters({
            @Parameter(name = "Authorization", required = true,  description = "授权码", in = ParameterIn.QUERY)
    })
    public R<?> logout(HttpServletRequest request) {

        if (StringUtils.isNotBlank(SecurityUtils.getHeaderToken(request))) {
            authService.revokeToken(SecurityUtils.getToken(request));
        }

        return R.ok(null, "登出成功");
    }

}

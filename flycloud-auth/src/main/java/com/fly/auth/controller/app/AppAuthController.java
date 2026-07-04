package com.fly.auth.controller.app;

import com.fly.auth.domain.vo.AppAuthLoginReqVo;
import com.fly.auth.domain.vo.AppAuthLoginRespVo;
import com.fly.auth.service.AppAuthService;
import com.fly.common.domain.model.R;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.auth.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 后台授权中心-控制层
 *
 * @author lxs
 * @date 2026/7/4
 */
@RestController
@RequestMapping("/app/auth")
@RequiredArgsConstructor
@Slf4j
public class AppAuthController {


    private final AppAuthService appAuthService;


    /**
     * 登录：手机 + 密码
     *
     */
    @PostMapping("/token")
    @Operation(summary = "使用手机 + 密码登录")
    public R<AppAuthLoginRespVo> getAccessToken(AppAuthLoginReqVo appAuthLoginReqVo) {
        return R.ok(appAuthService.login(appAuthLoginReqVo));
    }


    /**
     * 刷新令牌
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    @PermitAll
    public R<AppAuthLoginRespVo> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return R.ok(appAuthService.refreshToken(refreshToken));
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
            appAuthService.logout(SecurityUtils.getToken(request));
        }

        return R.ok(null, "登出成功");
    }

    // ========================================== 短信登录 start

//    @PostMapping("/sms-login")
//    @Operation(summary = "使用手机 + 验证码登录")
//    @PermitAll
//    public R<AppAuthLoginRespVo> smsLogin(@RequestBody @Valid AppAuthSmsLoginReqVo reqVo) {
//        return R.ok(appAuthService.smsLogin(reqVo));
//    }
//
//    @PostMapping("/send-sms-code")
//    @Operation(summary = "发送手机验证码")
//    @PermitAll
//    public R<Boolean> sendSmsCode(@RequestBody @Valid AppAuthSmsSendReqVo reqVo) {
//        appAuthService.sendSmsCode(UserUtils.getCurUserId(), reqVo);
//        return R.ok(true);
//    }
//
//    @PostMapping("/check-sms-code")
//    @Operation(summary = "校验手机验证码")
//    @PermitAll
//    public R<Boolean> checkSmsCode(@RequestBody @Valid AppAuthSmsValidateReqVo reqVo) {
//        appAuthService.checkSmsCode(UserUtils.getCurUserId(), reqVo);
//        return R.ok(true);
//    }

    // ========================================== 短信登录 end


}

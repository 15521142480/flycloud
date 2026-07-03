package com.fly.member.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.member.service.IMemberAuthService;
import com.fly.system.api.member.domain.bo.AppAuthLoginReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSmsLoginReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSmsSendReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSmsValidateReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSocialLoginReqBo;
import com.fly.system.api.member.domain.bo.AppAuthWeixinMiniAppLoginReqBo;
import com.fly.system.api.member.domain.vo.AppAuthLoginRespVo;
import com.fly.system.api.member.domain.vo.AuthWeixinJsapiSignatureRespVo;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 会员认证控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/app/member/auth", "/member/auth"})
public class AppAuthController {

    private final IMemberAuthService memberAuthService;

    /**
     * 手机号和密码登录。
     */
    @PostMapping("/login")
    @PermitAll
    public R<AppAuthLoginRespVo> login(@Valid @RequestBody AppAuthLoginReqBo reqBo) {
        return R.ok(memberAuthService.login(reqBo));
    }

    /**
     * 退出登录。
     */
    @PostMapping("/logout")
    @PermitAll
    public R<Boolean> logout(@RequestHeader(value = "Authorization", required = false) String authorization,
                             HttpServletRequest request) {
        String token = authorization == null ? request.getParameter("token") : authorization;
        memberAuthService.logout(token);
        return R.result(Boolean.TRUE);
    }

    /**
     * 刷新访问令牌。
     */
    @PostMapping("/refresh-token")
    @PermitAll
    public R<AppAuthLoginRespVo> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return R.ok(memberAuthService.refreshToken(refreshToken));
    }

    /**
     * 手机号和验证码登录。
     */
    @PostMapping("/sms-login")
    @PermitAll
    public R<AppAuthLoginRespVo> smsLogin(@Valid @RequestBody AppAuthSmsLoginReqBo reqBo) {
        return R.ok(memberAuthService.smsLogin(reqBo));
    }

    /**
     * 发送手机验证码。
     */
    @PostMapping("/send-sms-code")
    @PermitAll
    public R<Boolean> sendSmsCode(@Valid @RequestBody AppAuthSmsSendReqBo reqBo) {
        memberAuthService.sendSmsCode(UserUtils.getCurUserId(), reqBo);
        return R.result(Boolean.TRUE);
    }

    /**
     * 校验手机验证码。
     */
    @PostMapping("/validate-sms-code")
    @PermitAll
    public R<Boolean> validateSmsCode(@Valid @RequestBody AppAuthSmsValidateReqBo reqBo) {
        memberAuthService.validateSmsCode(UserUtils.getCurUserId(), reqBo);
        return R.result(Boolean.TRUE);
    }

    /**
     * 获取社交授权地址。
     */
    @GetMapping("/social-auth-redirect")
    @PermitAll
    public R<String> socialAuthRedirect(@RequestParam("type") Integer type,
                                        @RequestParam("redirectUri") String redirectUri) {
        return R.ok(memberAuthService.getSocialAuthorizeUrl(type, redirectUri));
    }

    /**
     * 社交快捷登录。
     */
    @PostMapping("/social-login")
    @PermitAll
    public R<AppAuthLoginRespVo> socialLogin(@Valid @RequestBody AppAuthSocialLoginReqBo reqBo) {
        return R.ok(memberAuthService.socialLogin(reqBo));
    }

    /**
     * 微信小程序一键登录。
     */
    @PostMapping("/weixin-mini-app-login")
    @PermitAll
    public R<AppAuthLoginRespVo> weixinMiniAppLogin(@Valid @RequestBody AppAuthWeixinMiniAppLoginReqBo reqBo) {
        return R.ok(memberAuthService.weixinMiniAppLogin(reqBo));
    }

    /**
     * 创建微信 JS SDK 初始化签名。
     */
    @PostMapping("/create-weixin-jsapi-signature")
    @PermitAll
    public R<AuthWeixinJsapiSignatureRespVo> createWeixinMpJsapiSignature(@RequestParam("url") String url) {
        return R.ok(new AuthWeixinJsapiSignatureRespVo());
    }

}

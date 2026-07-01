package com.fly.member.service;

import com.fly.system.api.member.domain.bo.AppAuthLoginReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSmsLoginReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSmsSendReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSmsValidateReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSocialLoginReqBo;
import com.fly.system.api.member.domain.bo.AppAuthWeixinMiniAppLoginReqBo;
import com.fly.system.api.member.domain.vo.AppAuthLoginRespVo;

/**
 * 会员认证 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IMemberAuthService {

    /**
     * 手机号和密码登录。
     */
    AppAuthLoginRespVo login(AppAuthLoginReqBo reqBo);

    /**
     * 退出登录。
     */
    void logout(String token);

    /**
     * 短信验证码登录。
     */
    AppAuthLoginRespVo smsLogin(AppAuthSmsLoginReqBo reqBo);

    /**
     * 社交登录。
     */
    AppAuthLoginRespVo socialLogin(AppAuthSocialLoginReqBo reqBo);

    /**
     * 微信小程序登录。
     */
    AppAuthLoginRespVo weixinMiniAppLogin(AppAuthWeixinMiniAppLoginReqBo reqBo);

    /**
     * 获取社交授权地址。
     */
    String getSocialAuthorizeUrl(Integer type, String redirectUri);

    /**
     * 发送短信验证码。
     */
    void sendSmsCode(Long userId, AppAuthSmsSendReqBo reqBo);

    /**
     * 校验短信验证码。
     */
    void validateSmsCode(Long userId, AppAuthSmsValidateReqBo reqBo);

    /**
     * 刷新访问令牌。
     */
    AppAuthLoginRespVo refreshToken(String refreshToken);

}

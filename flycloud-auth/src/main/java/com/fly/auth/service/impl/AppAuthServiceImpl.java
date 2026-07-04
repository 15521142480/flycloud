package com.fly.auth.service.impl;

import com.fly.auth.domain.vo.AppAuthLoginReqVo;
import com.fly.auth.domain.vo.AppAuthLoginRespVo;
import com.fly.auth.domain.vo.AuthTokenClaimsVo;
import com.fly.auth.service.AppAuthService;
import com.fly.auth.service.CaptchaService;
import com.fly.auth.utils.JwtUtils;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.config.properties.RsaProperties;
import com.fly.common.constant.AuthConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.enums.StatusEnum;
import com.fly.common.enums.auth.LoginTypeEnum;
import com.fly.common.enums.user.UserTypeEnum;
import com.fly.common.exception.AuthException;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.security.user.FlyUser;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.crypto.RsaUtils;
import com.fly.common.utils.json.JsonUtils;
import com.fly.system.api.im.enums.CommonStatusEnum;
import com.fly.system.api.member.domain.vo.MemberUserVo;
import com.fly.system.api.member.feign.IMemberUserApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * App认证服务
 *
 * @author: lxs
 * @date: 2026/7/4
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AppAuthServiceImpl implements AppAuthService {


    private final PasswordEncoder passwordEncoder;
    private final RsaProperties rsaProperties;
    private final CaptchaService captchaService;

    private final RedisUtils redisService;
    private final AuthProperties authProperties;

    private final IMemberUserApi memberUserApi;


    @Override
    public AppAuthLoginRespVo login(AppAuthLoginReqVo reqVo) {

        // 校验验证码
        captchaService.consumeImageTextClickCaptchaVerify(reqVo.getCaptchaCode());

        // 校验用户
        MemberUserVo user = this.getAndCheckUserByMobileAndPassword(reqVo.getMobile(), reqVo.getPassword());

        // 如果 socialType 非空，说明需要绑定社交用户
        String openId = null;
//        if (reqVo.getSocialType() != null) {
//            openId = socialUserApi.bindSocialUser(
//                    new SocialUserBindReqBo(
//                            user.getId(),
//                            UserTypeEnum.MEMBER.getValue(),
//                            reqVo.getSocialType(),
//                            reqVo.getSocialCode(),
//                            reqVo.getSocialState())
//            ).getCheckedData();
//        }

        // 创建 Token 令牌，记录登录日志
        return createToken(user);
    }




    @Override
    public AppAuthLoginRespVo refreshToken(String refreshToken) {
        return null;
    }


    @Override
    public void logout(String token) {

    }

    /**
     * 根据手机+密码获取用户信息
     *
     * @param mobile
     * @param password
     */
    public MemberUserVo getAndCheckUserByMobileAndPassword(String mobile, String password) {

        // 是否账号存在
        MemberUserVo user = memberUserApi.getMemberUserByMobile(mobile).getCheckedData();
        if (user == null) {
            log.error("登录失败!原因:{},手机:{},登录类型{}", Oauth2Constants.USER_IS_NULL_ERROR, mobile, LoginTypeEnum.APP_MOBILE_PASSWORD.getSource());
            throw new AuthException(Oauth2Constants.NAME_OR_PSD_ERROR); // 不要展示给用户提示不存在
        }

        // 是否密码正确
        String originalPassword = RsaUtils.decrypt(
                password,
                rsaProperties.getFlyCloudLoginPassword().getPrivateKey()
        );
        if (!passwordEncoder.matches(originalPassword, user.getPassword())) {
            throw new AuthException(Oauth2Constants.NAME_OR_PSD_ERROR);
        }

        // 是否禁用
        if (CommonStatusEnum.isDisable(user.getStatus())) {
            throw new AuthException(Oauth2Constants.USER_DISABLED_ERROR);
        }

        return user;
    }


    // =========================================================================== 公共方法


    /**
     * 创建token
     */
    private AppAuthLoginRespVo createToken(MemberUserVo user) {

        // 三个参数
        // 当前认证用户：自定义build
        // 登录凭证：已自定义验证（getAndCheckUserByMobileAndPassword），设置null，不需要spring security执行authenticationManager.authenticate验证
        // 权限：app端目前不涉及权限
        new UsernamePasswordAuthenticationToken(
                buildFlyUser(user),
                null,
                null
        );

        log.info("用户id【{}】登录系统;手机号:{},用户类型{}", user.getId(), user.getMobile(), UserTypeEnum.MEMBER.getValue());


        AuthTokenClaimsVo authTokenClaimsVo = buildTokenClaims(user);
        JwtUtils.TokenJwtInfo accessTokenInfo = JwtUtils.buildTokenJwt(authTokenClaimsVo, authProperties.getToken().getLoginTimeout());
        JwtUtils.TokenJwtInfo refreshTokenInfo = JwtUtils.buildTokenJwt(authTokenClaimsVo, authProperties.getToken().getLoginTimeout());
        String accessToken = accessTokenInfo.token();
        String refreshToken = refreshTokenInfo.token();
        String accessId = refreshTokenInfo.accessId();

        redisService.set(AuthConstants.ACCESS_TOKEN_KEY + accessToken, JsonUtils.toJsonString(authTokenClaimsVo), authProperties.getToken().getLoginTimeout());
        redisService.set(AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + accessToken, JsonUtils.toJsonString(authTokenClaimsVo), authProperties.getToken().getLoginTimeout());
        redisService.set(AuthConstants.REFRESH_TOKEN_KEY + refreshToken, JsonUtils.toJsonString(authTokenClaimsVo), authProperties.getToken().getRefreshTokenTimeout());


        AppAuthLoginRespVo respVo = new AppAuthLoginRespVo();
        respVo.setUserId(user.getId());
        respVo.setAccessToken(accessToken);
        respVo.setRefreshToken(refreshToken);
        respVo.setJti(accessId);
        respVo.setExpiresTime(LocalDateTime.now()
                .plusSeconds(authProperties.getToken().getLoginTimeoutSeconds()));

        return respVo;
    }



    private AuthTokenClaimsVo buildTokenClaims(MemberUserVo user) {

        AuthTokenClaimsVo authTokenClaimsVo = new AuthTokenClaimsVo();
        authTokenClaimsVo.setUserId(user.getId());
        authTokenClaimsVo.setUserName(StringUtils.isNotBlank(user.getNickname()) ? user.getNickname() : user.getName());
        authTokenClaimsVo.setUserType(UserTypeEnum.MEMBER.getValue());
        authTokenClaimsVo.setLoginType(LoginTypeEnum.APP_MOBILE_PASSWORD.getType());
        authTokenClaimsVo.setRoleIds(null);
        authTokenClaimsVo.setDeptId(null);
        return authTokenClaimsVo;
    }


    private FlyUser buildFlyUser(MemberUserVo user) {

        return new FlyUser(
                user.getId()
                , UserTypeEnum.MEMBER.getValue()
                , LoginTypeEnum.APP_MOBILE_PASSWORD.getType()
                , null
                , user.getMobile()
                , user.getAvatar()
                , null
                , user.getNickname()
                , user.getPassword()
                , Objects.equals(StatusEnum.ENABLE.getStatus(), user.getStatus()),
                true
                , true
                , true
                , null
        );
    }

}

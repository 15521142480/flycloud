package com.fly.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fly.auth.domain.vo.AuthLoginRespVo;
import com.fly.auth.domain.vo.AuthTokenClaimsVo;
import com.fly.auth.service.AuthService;
import com.fly.auth.service.CaptchaService;
import com.fly.auth.sms.SmsCodeAuthenticationToken;
import com.fly.auth.social.SocialAuthenticationToken;
import com.fly.auth.utils.JwtUtils;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.config.properties.RsaProperties;
import com.fly.common.constant.AuthConstants;
import com.fly.common.constant.CommonConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.enums.auth.LoginTypeEnum;
import com.fly.common.enums.user.UserTypeEnum;
import com.fly.common.exception.AuthException;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.security.user.FlyUser;
import com.fly.common.utils.crypto.RsaUtils;
import com.fly.common.utils.json.JsonUtils;
import com.xkcoding.justauth.AuthRequestFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 基于 Spring Security 6 的 Token 认证实现。
 *
 * @author lxs
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final AuthenticationManager authenticationManager;

    private final RedisUtils redisService;

    private final ObjectProvider<AuthRequestFactory> factoryProvider;

    private final AuthProperties authProperties;

    private final CaptchaService captchaService;

    private final RsaProperties rsaProperties;


    @Override
    public AuthLoginRespVo token(Map<String, String> loginParam) {

        AuthLoginRespVo authLoginRespVo = new AuthLoginRespVo();
        String grantType = loginParam.get(Oauth2Constants.GRANT_TYPE);
        if (StrUtil.isBlank(grantType)) {
            grantType = Oauth2Constants.PASSWORD;
        }

        if (Oauth2Constants.REFRESH_TOKEN.equals(grantType)) {
            return handleRefreshToken(loginParam.get(Oauth2Constants.REFRESH_TOKEN));
        }

        Authentication authentication = switch (grantType) {
            case Oauth2Constants.PASSWORD -> authenticatePassword(loginParam);
            case AuthConstants.GRANT_TYPE_CAPTCHA -> authenticateCaptcha(loginParam);
            case AuthConstants.GRANT_TYPE_SMS -> authenticateSms(loginParam);
            case AuthConstants.GRANT_TYPE_SOCIAL -> authenticateSocial(loginParam);
            default -> throw new AuthException(CommonConstants.FAIL, "不支持该认证类型");
        };

        issueToken(authLoginRespVo, authentication);

        return authLoginRespVo;
    }



    @Override
    public AuthLoginRespVo refreshToken(String refreshToken) {
        return handleRefreshToken(refreshToken);
    }



    @Override
    public void revokeToken(String token) {

        if (StrUtil.isBlank(token)) {
            return;
        }
        // 只清理redis的token即可；
        // 无需清理spring-security的上下文， SecurityContextHolder 因为后端是无状态请求，只在当前请求线程里有效，请求结束后就没了
        redisService.del(AuthConstants.ACCESS_TOKEN_KEY + token, AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + token);
    }


    /**
     * NOTE 帐号秘密授权方式
     *
     * @param loginParam
    */
    private Authentication authenticatePassword(Map<String, String> loginParam) {

        String username = loginParam.get("username");
//        String password = loginParam.get("password");
        // rsa解密
        String originalPassword = RsaUtils.decrypt(
                loginParam.get("password"),
                rsaProperties.getFlyCloudLoginPassword().getPrivateKey()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, originalPassword);
        return doAuthenticate(authentication, loginParam, username);
    }


    /**
     * NOTE 验证码授权方式_字母数字验证
     *
     * @param loginParam
     * @param request
    */
//    private Authentication authenticateCaptcha(Map<String, String> loginParam, HttpServletRequest request) {
//
//        String key = firstNotBlank(request.getHeader(Oauth2Constants.CAPTCHA_HEADER_KEY),
//                loginParam.get(Oauth2Constants.VALIDATE_CODE_KEY));
//        String code = firstNotBlank(request.getHeader(Oauth2Constants.CAPTCHA_HEADER_CODE),
//                loginParam.get(Oauth2Constants.VALIDATE_CODE_CODE));
//        Object codeFromRedis = redisService.get(Oauth2Constants.CAPTCHA_KEY + key);
//
//        if (StrUtil.isBlank(code)) {
//            throw new AuthException(CommonConstants.FAIL, "请输入验证码");
//        }
//        if (codeFromRedis == null) {
//            throw new AuthException(CommonConstants.FAIL, "验证码已过期");
//        }
//        if (!StrUtil.equalsIgnoreCase(code, codeFromRedis.toString())) {
//            throw new AuthException(CommonConstants.FAIL, "验证码不正确");
//        }
//
//        redisService.del(Oauth2Constants.CAPTCHA_KEY + key);
//        return authenticatePassword(loginParam);
//    }


    /**
     * NOTE 验证码授权方式_图文点选验证
     *
     * @param loginParam
    */
    private Authentication authenticateCaptcha(Map<String, String> loginParam) {

        String successValue = loginParam.get(Oauth2Constants.VALIDATE_CAPTCHA_CODE);
        captchaService.consumeImageTextClickCaptchaVerify(successValue);

        return authenticatePassword(loginParam);
    }


    /**
     * NOTE 短信授权方式
     *
     * @param loginParam
    */
    private Authentication authenticateSms(Map<String, String> loginParam) {
        String mobile = loginParam.get(Oauth2Constants.DEFAULT_PARAMETER_NAME_MOBILE);
        String captchaCode = loginParam.get(Oauth2Constants.VALIDATE_CAPTCHA_CODE);
        Object codeFromRedis = redisService.get(Oauth2Constants.SMS_CODE_KEY + mobile);

        if (StrUtil.isBlank(captchaCode)) {
            throw new AuthException(CommonConstants.FAIL, "请输入验证码");
        }
        if (codeFromRedis == null) {
            throw new AuthException(CommonConstants.FAIL, "验证码已过期");
        }
        if (!StrUtil.equalsIgnoreCase(captchaCode, codeFromRedis.toString())) {
            throw new AuthException(CommonConstants.FAIL, "验证码不正确");
        }

        redisService.del(Oauth2Constants.SMS_CODE_KEY + mobile);
        Authentication authentication = new SmsCodeAuthenticationToken(mobile);
        return doAuthenticate(authentication, loginParam, mobile);
    }


    /**
     * NOTE 第三方授权方式
     *
     * @param loginParam
    */
    private Authentication authenticateSocial(Map<String, String> loginParam) {
        String code = loginParam.get("code");
        String state = loginParam.get("state");
        Object codeFromRedis = redisService.get(AuthConstants.SOCIAL_STATE_PREFIX + state);

        if (StrUtil.isBlank(code)) {
            throw new AuthException(CommonConstants.FAIL, "未传入请求参数");
        }
        if (codeFromRedis == null) {
            throw new AuthException(CommonConstants.FAIL, "openId已过期,请重新发起授权请求");
        }

        String[] codeParts = code.split("-", 2);
        if (codeParts.length != 2) {
            throw new AuthException(CommonConstants.FAIL, "社交登录参数格式不正确");
        }

        AuthRequestFactory factory = factoryProvider.getIfAvailable();
        if (factory == null) {
            throw new AuthException(CommonConstants.FAIL, "未配置第三方登录");
        }
        AuthRequest authRequest = factory.get(codeParts[0]);
        AuthCallback authCallback = AuthCallback.builder().code(codeParts[1]).state(state).build();
        AuthResponse response = authRequest.login(authCallback);
        log.info("【response】= {}", JSON.toJSON(response));

        AuthUser authUser = null;
        if (response.getCode() == AuthResponseStatus.SUCCESS.getCode()) {
            authUser = (AuthUser) response.getData();
        }
        if (authUser == null) {
            throw new AuthException(CommonConstants.FAIL, "社交登录失败");
        }

        Authentication authentication = new SocialAuthenticationToken(authUser);
        return doAuthenticate(authentication, loginParam, authUser.getUsername());
    }

    private Authentication doAuthenticate(Authentication authentication, Map<String, String> details, String principal) {
        if (authentication instanceof AbstractAuthenticationToken token) {
            token.setDetails(details);
        }
        try {
            Authentication authenticated = authenticationManager.authenticate(authentication);
            if (authenticated == null || !authenticated.isAuthenticated()) {
                throw new AuthException(CommonConstants.FAIL, "认证失败: " + principal);
            }
            return authenticated;
        } catch (AccountStatusException | BadCredentialsException e) {
            log.error("登录失败:", e);
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("Bad credentials")) {
                throw new AuthException(CommonConstants.FAIL, Oauth2Constants.NAME_OR_PSD_ERROR);
            }
            if (errorMsg != null && errorMsg.contains("User is disabled")) {
                throw new AuthException(CommonConstants.FAIL, Oauth2Constants.USER_DISABLED_ERROR);
            }
            throw new AuthException(CommonConstants.FAIL, errorMsg);
        }
    }

    private void issueToken(AuthLoginRespVo authLoginRespVo, Authentication authentication) {
        FlyUser user = (FlyUser) authentication.getPrincipal();
        buildAuthLoginUserInfo(authLoginRespVo, user);
        buildTokenResponse(authLoginRespVo);
    }



    public AuthLoginRespVo handleRefreshToken(String refreshToken) {

        if (StrUtil.isBlank(refreshToken)) {
            throw new AuthException(CommonConstants.FAIL, "refresh token不能为空");
        }
        AuthLoginRespVo nextClaims = cachedClaims(refreshToken);
        if (nextClaims == null) {
            throw new AuthException(CommonConstants.FAIL, "refresh token无效或已过期");
        }

        buildTokenResponse(nextClaims);

        return nextClaims;
    }



    private AuthLoginRespVo cachedClaims(String refreshToken) {

        Object cached = redisService.get(AuthConstants.REFRESH_TOKEN_KEY + refreshToken);
        if (cached == null) {
            return null;
        }
        if (cached instanceof Map<?, ?> map) {
            Map<String, Object> nextClaims = new LinkedHashMap<>();
            map.forEach((key, value) -> nextClaims.put(String.valueOf(key), value));
            return JSON.parseObject(JsonUtils.toJsonString(nextClaims), AuthLoginRespVo.class);
        }

        // 兼容升级前已签发的 refresh token。
        Claims claims = Jwts.parser()
                .verifyWith(JwtUtils.signingKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        return JSON.parseObject(JsonUtils.toJsonString(claims), AuthLoginRespVo.class);
    }


    private void buildTokenResponse(AuthLoginRespVo authLoginRespVo) {

        AuthTokenClaimsVo authTokenClaimsVo = buildTokenClaims(authLoginRespVo);
        JwtUtils.TokenJwtInfo accessTokenInfo = JwtUtils.buildTokenJwt(authTokenClaimsVo, authProperties.getToken().getLoginTimeout());
        JwtUtils.TokenJwtInfo refreshTokenInfo = JwtUtils.buildTokenJwt(authTokenClaimsVo, authProperties.getToken().getLoginTimeout());
        String accessToken = accessTokenInfo.token();
        String refreshToken = refreshTokenInfo.token();
        String accessId = refreshTokenInfo.accessId();

        redisService.set(AuthConstants.ACCESS_TOKEN_KEY + accessToken, authLoginRespVo, authProperties.getToken().getLoginTimeout());
        redisService.set(AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + accessToken, authLoginRespVo, authProperties.getToken().getLoginTimeout());
        redisService.set(AuthConstants.REFRESH_TOKEN_KEY + refreshToken, authLoginRespVo, authProperties.getToken().getRefreshTokenTimeout());

        authLoginRespVo.setAccessToken(accessToken);
        authLoginRespVo.setRefreshToken(refreshToken);
        authLoginRespVo.setJti(accessId);
        authLoginRespVo.setExpiresTime(authProperties.getToken().getLoginTimeoutSeconds());
    }



    private AuthTokenClaimsVo buildTokenClaims(AuthLoginRespVo authLoginRespVo) {

        AuthTokenClaimsVo authTokenClaimsVo = new AuthTokenClaimsVo();
        authTokenClaimsVo.setUserId(authLoginRespVo.getUserId());
        authTokenClaimsVo.setUserName(authLoginRespVo.getUserName());
        authTokenClaimsVo.setUserType(UserTypeEnum.ADMIN.getValue());
        authTokenClaimsVo.setLoginType(LoginTypeEnum.ADMIN_ACCOUNT_PASSWORD.getType());
        authTokenClaimsVo.setRoleIds(authLoginRespVo.getRoleIds());
        authTokenClaimsVo.setDeptId(authLoginRespVo.getDeptId());
        return authTokenClaimsVo;
    }


    private void buildAuthLoginUserInfo(AuthLoginRespVo authLoginRespVo, FlyUser user) {

        authLoginRespVo.setUserId(user.getId());
        authLoginRespVo.setMobile(user.getMobile());
        authLoginRespVo.setUserName(user.getUsername());
        authLoginRespVo.setUserType(UserTypeEnum.ADMIN.getValue());
        authLoginRespVo.setLoginType(LoginTypeEnum.ADMIN_ACCOUNT_PASSWORD.getType());
        authLoginRespVo.setRoleIds(user.getRoleIds());
        authLoginRespVo.setAvatar(user.getAvatar());
        authLoginRespVo.setDeptId(user.getDeptId());
        authLoginRespVo.setAuthorities(user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
    }


}

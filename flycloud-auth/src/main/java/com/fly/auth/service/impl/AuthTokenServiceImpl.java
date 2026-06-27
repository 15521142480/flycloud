package com.fly.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fly.auth.service.AuthTokenService;
import com.fly.auth.service.ValidateService;
import com.fly.auth.sms.SmsCodeAuthenticationToken;
import com.fly.auth.social.SocialAuthenticationToken;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.constant.AuthConstants;
import com.fly.common.constant.CommonConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.exception.AuthException;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.security.user.FlyUser;
import com.xkcoding.justauth.AuthRequestFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
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

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 基于 Spring Security 6 的 Token 认证实现。
 *
 * @author lxs
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {


    private final AuthenticationManager authenticationManager;

    private final RedisUtils redisService;

    private final ObjectProvider<AuthRequestFactory> factoryProvider;

    private final AuthProperties authTokenProperties;

    private final ValidateService validateService;


    @Override
    public Map<String, Object> createToken(Map<String, String> loginParam, HttpServletRequest request) {

        String grantType = loginParam.get(Oauth2Constants.GRANT_TYPE);
        if (StrUtil.isBlank(grantType)) {
            grantType = Oauth2Constants.PASSWORD;
        }

        if (Oauth2Constants.REFRESH_TOKEN.equals(grantType)) {
            return refreshToken(loginParam);
        }

        Authentication authentication = switch (grantType) {
            case Oauth2Constants.PASSWORD -> authenticatePassword(loginParam);
            case AuthConstants.GRANT_TYPE_CAPTCHA -> authenticateCaptcha(loginParam, request);
            case AuthConstants.GRANT_TYPE_SMS -> authenticateSms(loginParam);
            case AuthConstants.GRANT_TYPE_SOCIAL -> authenticateSocial(loginParam);
            default -> throw new AuthException(CommonConstants.FAIL, "不支持该认证类型");
        };

        return issueToken(authentication);
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
        String password = loginParam.get("password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
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
     * @param request
    */
    private Authentication authenticateCaptcha(Map<String, String> loginParam, HttpServletRequest request) {

        String successValue = firstNotBlank(
                request.getHeader(Oauth2Constants.IMAGE_TEXT_CLICK_CAPTCHA_SUCCESS_VALUE),
                loginParam.get(Oauth2Constants.IMAGE_TEXT_CLICK_CAPTCHA_SUCCESS_VALUE)
        );
        validateService.consumeImageTextClickCaptchaVerify(successValue);

        return authenticatePassword(loginParam);
    }


    /**
     * NOTE 短信授权方式
     *
     * @param loginParam
    */
    private Authentication authenticateSms(Map<String, String> loginParam) {
        String mobile = loginParam.get(Oauth2Constants.DEFAULT_PARAMETER_NAME_MOBILE);
        String code = loginParam.get(Oauth2Constants.VALIDATE_CODE_CODE);
        Object codeFromRedis = redisService.get(Oauth2Constants.SMS_CODE_KEY + mobile);

        if (StrUtil.isBlank(code)) {
            throw new AuthException(CommonConstants.FAIL, "请输入验证码");
        }
        if (codeFromRedis == null) {
            throw new AuthException(CommonConstants.FAIL, "验证码已过期");
        }
        if (!StrUtil.equalsIgnoreCase(code, codeFromRedis.toString())) {
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

    private Map<String, Object> issueToken(Authentication authentication) {
        FlyUser user = (FlyUser) authentication.getPrincipal();
        Map<String, Object> claims = userClaims(user);
        return buildTokenResponse(claims);
    }

    private Map<String, Object> refreshToken(Map<String, String> loginParam) {
        String refreshToken = loginParam.get(Oauth2Constants.REFRESH_TOKEN);
        if (StrUtil.isBlank(refreshToken)) {
            throw new AuthException(CommonConstants.FAIL, "refresh token不能为空");
        }
        Map<String, Object> nextClaims = cachedClaims(refreshToken);
        if (nextClaims == null) {
            throw new AuthException(CommonConstants.FAIL, "refresh token无效或已过期");
        }

        return buildTokenResponse(nextClaims);
    }

    private Map<String, Object> cachedClaims(String refreshToken) {

        Object cached = redisService.get(AuthConstants.REFRESH_TOKEN_KEY + refreshToken);
        if (cached == null) {
            return null;
        }
        if (cached instanceof Map<?, ?> map) {
            Map<String, Object> nextClaims = new LinkedHashMap<>();
            map.forEach((key, value) -> nextClaims.put(String.valueOf(key), value));
            return nextClaims;
        }

        // 兼容升级前已签发的 refresh token。
        Claims claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

        Map<String, Object> nextClaims = new LinkedHashMap<>();
        nextClaims.put(Oauth2Constants.USER_ID, claims.get(Oauth2Constants.USER_ID));
        nextClaims.put(Oauth2Constants.USER_NAME, claims.get(Oauth2Constants.USER_NAME));
        nextClaims.put(Oauth2Constants.USER_TYPE, claims.get(Oauth2Constants.USER_TYPE));
        nextClaims.put(Oauth2Constants.LOGIN_TYPE, claims.get(Oauth2Constants.LOGIN_TYPE));
        nextClaims.put(Oauth2Constants.ROLE_IDS, claims.get(Oauth2Constants.ROLE_IDS));
        nextClaims.put(Oauth2Constants.AVATAR, claims.get(Oauth2Constants.AVATAR));
        nextClaims.put(AuthConstants.DEPT_ID, claims.get(AuthConstants.DEPT_ID));
        nextClaims.put(AuthConstants.PHONE, claims.get(AuthConstants.PHONE));
        nextClaims.put(AuthConstants.AUTHORITIES, claims.get(AuthConstants.AUTHORITIES));
        return nextClaims;
    }

    private Map<String, Object> buildTokenResponse(Map<String, Object> claims) {
        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();
//        String accessToken = jwt(claims, accessJti, Duration.ofHours(2));
        String accessToken = jwt(tokenClaims(claims), accessJti, authTokenProperties.getToken().getLoginTimeout());
        String refreshToken = jwt(tokenClaims(claims), refreshJti, authTokenProperties.getToken().getRefreshTokenTimeout());

        redisService.set(AuthConstants.ACCESS_TOKEN_KEY + accessToken, claims, authTokenProperties.getToken().getLoginTimeout());
        redisService.set(AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + accessToken, claims, authTokenProperties.getToken().getLoginTimeout());
        redisService.set(AuthConstants.REFRESH_TOKEN_KEY + refreshToken, claims, authTokenProperties.getToken().getRefreshTokenTimeout());

        Map<String, Object> resultData = new LinkedHashMap<>(claims);
        resultData.put("accessToken", accessToken);
        resultData.put("refreshToken", refreshToken);
        resultData.put("jti", accessJti);
        resultData.put("expiresIn", authTokenProperties.getToken().getLoginTimeoutSeconds());
        return resultData;
    }

    private Map<String, Object> tokenClaims(Map<String, Object> claims) {

        Map<String, Object> tokenClaims = new LinkedHashMap<>();
        tokenClaims.put(Oauth2Constants.USER_ID, claims.get(Oauth2Constants.USER_ID));
        tokenClaims.put(Oauth2Constants.USER_NAME, claims.get(Oauth2Constants.USER_NAME));
        tokenClaims.put(Oauth2Constants.USER_TYPE, claims.get(Oauth2Constants.USER_TYPE));
        tokenClaims.put(Oauth2Constants.LOGIN_TYPE, claims.get(Oauth2Constants.LOGIN_TYPE));
        tokenClaims.put(Oauth2Constants.ROLE_IDS, claims.get(Oauth2Constants.ROLE_IDS));
        tokenClaims.put(AuthConstants.DEPT_ID, claims.get(AuthConstants.DEPT_ID));
        return tokenClaims;
    }

    private Map<String, Object> userClaims(FlyUser user) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put(Oauth2Constants.USER_ID, user.getId());
        claims.put(Oauth2Constants.USER_NAME, user.getUsername());
        claims.put(Oauth2Constants.USER_TYPE, user.getUserType());
        claims.put(Oauth2Constants.LOGIN_TYPE, user.getLoginType());
        claims.put(Oauth2Constants.ROLE_IDS, user.getRoleIds());
        claims.put(Oauth2Constants.AVATAR, user.getAvatar());
        claims.put(AuthConstants.DEPT_ID, user.getDeptId());
        claims.put(AuthConstants.PHONE, user.getPhone());
        claims.put(AuthConstants.AUTHORITIES, user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return claims;
    }

    private String jwt(Map<String, Object> claims, String jti, Duration ttl) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .id(jti)
                .subject(String.valueOf(claims.get(Oauth2Constants.USER_NAME)))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(ttl)))
                .signWith(signingKey())
                .compact();
    }

    private SecretKey signingKey() {
        byte[] source = Oauth2Constants.SIGN_KEY.getBytes(StandardCharsets.UTF_8);
        byte[] key = new byte[32];
        for (int i = 0; i < key.length; i++) {
            key[i] = source[i % source.length];
        }
        return Keys.hmacShaKeyFor(key);
    }

    private String firstNotBlank(String first, String second) {
        return StrUtil.isNotBlank(first) ? first : second;
    }
}

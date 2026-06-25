package com.fly.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fly.auth.service.AuthTokenService;
import com.fly.auth.sms.SmsCodeAuthenticationToken;
import com.fly.auth.social.SocialAuthenticationToken;
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

    private static final String GRANT_TYPE_CAPTCHA = "captcha";

    private static final String GRANT_TYPE_SMS = "sms";

    private static final String GRANT_TYPE_SOCIAL = "social";

    private static final String ACCESS_TOKEN_KEY = "fly.oauth.access.";

    private static final String REFRESH_TOKEN_KEY = "fly.oauth.refresh.";

    private static final String SOCIAL_STATE_PREFIX = "SOCIAL::STATE::";

    private static final Duration ACCESS_TOKEN_TTL = Duration.ofHours(2);

    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(30);

    private final AuthenticationManager authenticationManager;

    private final RedisUtils redisService;

    private final ObjectProvider<AuthRequestFactory> factoryProvider;

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
            case GRANT_TYPE_CAPTCHA -> authenticateCaptcha(loginParam, request);
            case GRANT_TYPE_SMS -> authenticateSms(loginParam);
            case GRANT_TYPE_SOCIAL -> authenticateSocial(loginParam);
            default -> throw new AuthException(CommonConstants.FAIL, "不支持该认证类型");
        };

        return issueToken(authentication);
    }

    @Override
    public void revokeToken(String token) {
        if (StrUtil.isBlank(token)) {
            return;
        }
        redisService.del(ACCESS_TOKEN_KEY + token);
    }

    private Authentication authenticatePassword(Map<String, String> loginParam) {
        String username = loginParam.get("username");
        String password = loginParam.get("password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        return doAuthenticate(authentication, loginParam, username);
    }

    private Authentication authenticateCaptcha(Map<String, String> loginParam, HttpServletRequest request) {
        String key = firstNotBlank(request.getHeader(Oauth2Constants.CAPTCHA_HEADER_KEY),
                loginParam.get(Oauth2Constants.VALIDATE_CODE_KEY));
        String code = firstNotBlank(request.getHeader(Oauth2Constants.CAPTCHA_HEADER_CODE),
                loginParam.get(Oauth2Constants.VALIDATE_CODE_CODE));
        Object codeFromRedis = redisService.get(Oauth2Constants.CAPTCHA_KEY + key);

        if (StrUtil.isBlank(code)) {
            throw new AuthException(CommonConstants.FAIL, "请输入验证码");
        }
        if (codeFromRedis == null) {
            throw new AuthException(CommonConstants.FAIL, "验证码已过期");
        }
        if (!StrUtil.equalsIgnoreCase(code, codeFromRedis.toString())) {
            throw new AuthException(CommonConstants.FAIL, "验证码不正确");
        }

        redisService.del(Oauth2Constants.CAPTCHA_KEY + key);
        return authenticatePassword(loginParam);
    }

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

    private Authentication authenticateSocial(Map<String, String> loginParam) {
        String code = loginParam.get("code");
        String state = loginParam.get("state");
        Object codeFromRedis = redisService.get(SOCIAL_STATE_PREFIX + state);

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
        Object cached = redisService.get(REFRESH_TOKEN_KEY + refreshToken);
        if (cached == null) {
            throw new AuthException(CommonConstants.FAIL, "refresh token无效或已过期");
        }

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
        return buildTokenResponse(nextClaims);
    }

    private Map<String, Object> buildTokenResponse(Map<String, Object> claims) {
        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();
        String accessToken = jwt(claims, accessJti, ACCESS_TOKEN_TTL);
        String refreshToken = jwt(claims, refreshJti, REFRESH_TOKEN_TTL);

        redisService.set(ACCESS_TOKEN_KEY + accessToken, claims, ACCESS_TOKEN_TTL);
        redisService.set(REFRESH_TOKEN_KEY + refreshToken, claims, REFRESH_TOKEN_TTL);

        Map<String, Object> resultData = new LinkedHashMap<>(claims);
        resultData.put("accessToken", accessToken);
        resultData.put("refreshToken", refreshToken);
        resultData.put("jti", accessJti);
        resultData.put("expiresIn", ACCESS_TOKEN_TTL.toSeconds());
        return resultData;
    }

    private Map<String, Object> userClaims(FlyUser user) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put(Oauth2Constants.USER_ID, user.getId());
        claims.put(Oauth2Constants.USER_NAME, user.getUsername());
        claims.put(Oauth2Constants.USER_TYPE, user.getUserType());
        claims.put(Oauth2Constants.LOGIN_TYPE, user.getLoginType());
        claims.put(Oauth2Constants.ROLE_IDS, user.getRoleIds());
        claims.put(Oauth2Constants.AVATAR, user.getAvatar());
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

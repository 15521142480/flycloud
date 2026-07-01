package com.fly.member.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.constant.AuthConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.exception.ServiceException;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.utils.StringUtils;
import com.fly.member.mapper.MemberUserMapper;
import com.fly.member.service.IMemberAuthService;
import com.fly.member.service.ISocialUserService;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.system.api.member.domain.SocialUser;
import com.fly.system.api.member.domain.bo.AppAuthLoginReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSmsLoginReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSmsSendReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSmsValidateReqBo;
import com.fly.system.api.member.domain.bo.AppAuthSocialLoginReqBo;
import com.fly.system.api.member.domain.bo.AppAuthWeixinMiniAppLoginReqBo;
import com.fly.system.api.member.domain.bo.AppSocialUserBindReqBo;
import com.fly.system.api.member.domain.vo.AppAuthLoginRespVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 会员认证 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MemberAuthServiceImpl implements IMemberAuthService {

    private static final int USER_TYPE_MEMBER = 2;

    private static final int LOGIN_TYPE_PASSWORD = 1;

    private static final int LOGIN_TYPE_SMS = 2;

    private static final int STATUS_DISABLE = 1;

    private final MemberUserMapper memberUserMapper;
    private final RedisUtils redisUtils;
    private final AuthProperties authProperties;
    private final ISocialUserService socialUserService;

    /**
     * 手机号和密码登录。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppAuthLoginRespVo login(AppAuthLoginReqBo reqBo) {
        MemberUser user = selectUserByMobile(reqBo.getMobile());
        if (user == null || !isPasswordMatch(reqBo.getPassword(), user.getPassword())) {
            throw new ServiceException("手机号或密码不正确");
        }
        validateUserEnabled(user);
        return createTokenAfterLoginSuccess(user, LOGIN_TYPE_PASSWORD, null);
    }

    /**
     * 退出登录。
     */
    @Override
    public void logout(String token) {
        if (StringUtils.isBlank(token)) {
            return;
        }
        String accessToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        redisUtils.del(AuthConstants.ACCESS_TOKEN_KEY + accessToken, AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + accessToken);
    }

    /**
     * 短信验证码登录。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppAuthLoginRespVo smsLogin(AppAuthSmsLoginReqBo reqBo) {
        validateSmsCode(null, smsValidateBo(reqBo));
        MemberUser user = selectUserByMobile(reqBo.getMobile());
        if (user == null) {
            user = createMemberUser(reqBo.getMobile());
        }
        validateUserEnabled(user);
        return createTokenAfterLoginSuccess(user, LOGIN_TYPE_SMS, null);
    }

    /**
     * 社交登录。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppAuthLoginRespVo socialLogin(AppAuthSocialLoginReqBo reqBo) {
        SocialUser socialUser = socialUserService.getSocialUserByCode(USER_TYPE_MEMBER, reqBo.getType(), reqBo.getCode(), reqBo.getState());
        Object bindUserId = socialUser.getParams().get("bindUserId");
        MemberUser user = bindUserId == null ? null : memberUserMapper.selectById(Long.valueOf(String.valueOf(bindUserId)));
        if (user == null) {
            user = createSocialMemberUser(socialUser);
            AppSocialUserBindReqBo bindReqBo = new AppSocialUserBindReqBo();
            bindReqBo.setType(reqBo.getType());
            bindReqBo.setCode(reqBo.getCode());
            bindReqBo.setState(reqBo.getState());
            socialUserService.bindSocialUser(user.getId(), USER_TYPE_MEMBER, bindReqBo);
        }
        validateUserEnabled(user);
        return createTokenAfterLoginSuccess(user, LOGIN_TYPE_PASSWORD, socialUser.getOpenid());
    }

    /**
     * 微信小程序登录。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppAuthLoginRespVo weixinMiniAppLogin(AppAuthWeixinMiniAppLoginReqBo reqBo) {
        String mobile = reqBo.getPhoneCode();
        MemberUser user = selectUserByMobile(mobile);
        if (user == null) {
            user = createMemberUser(mobile);
        }
        AppSocialUserBindReqBo bindReqBo = new AppSocialUserBindReqBo();
        bindReqBo.setType(34);
        bindReqBo.setCode(reqBo.getLoginCode());
        bindReqBo.setState(reqBo.getState());
        String openid = socialUserService.bindSocialUser(user.getId(), USER_TYPE_MEMBER, bindReqBo);
        validateUserEnabled(user);
        return createTokenAfterLoginSuccess(user, LOGIN_TYPE_SMS, openid);
    }

    /**
     * 获取社交授权地址。
     */
    @Override
    public String getSocialAuthorizeUrl(Integer type, String redirectUri) {
        if (StringUtils.isBlank(redirectUri)) {
            return "";
        }
        String separator = redirectUri.contains("?") ? "&" : "?";
        return redirectUri + separator + "type=" + type;
    }

    /**
     * 发送短信验证码。当前复用已有 Redis 短信验证码约定，未接短信网关时写入固定验证码便于本地联调。
     */
    @Override
    public void sendSmsCode(Long userId, AppAuthSmsSendReqBo reqBo) {
        if (StringUtils.isBlank(reqBo.getMobile()) && userId != null) {
            MemberUser user = memberUserMapper.selectById(userId);
            reqBo.setMobile(user == null ? null : user.getMobile());
        }
        if (StringUtils.isBlank(reqBo.getMobile())) {
            throw new ServiceException("手机号不能为空");
        }
        redisUtils.set(Oauth2Constants.SMS_CODE_KEY + reqBo.getMobile(), "1234", java.time.Duration.ofMinutes(5));
    }

    /**
     * 校验短信验证码。
     */
    @Override
    public void validateSmsCode(Long userId, AppAuthSmsValidateReqBo reqBo) {
        if (StringUtils.isBlank(reqBo.getMobile()) && userId != null) {
            MemberUser user = memberUserMapper.selectById(userId);
            reqBo.setMobile(user == null ? null : user.getMobile());
        }
        if (StringUtils.isBlank(reqBo.getMobile())) {
            throw new ServiceException("手机号不能为空");
        }
        Object code = redisUtils.get(Oauth2Constants.SMS_CODE_KEY + reqBo.getMobile());
        if (code == null) {
            throw new ServiceException("验证码已过期");
        }
        if (!String.valueOf(code).equalsIgnoreCase(reqBo.getCode())) {
            throw new ServiceException("验证码不正确");
        }
        redisUtils.del(Oauth2Constants.SMS_CODE_KEY + reqBo.getMobile());
    }

    /**
     * 刷新访问令牌。
     */
    @Override
    public AppAuthLoginRespVo refreshToken(String refreshToken) {
        Object cached = redisUtils.get(AuthConstants.REFRESH_TOKEN_KEY + refreshToken);
        if (cached == null) {
            throw new ServiceException("refresh token 无效或已过期");
        }
        Map<String, Object> claims = toClaimsMap(cached);
        return buildTokenResponse(claims, null);
    }

    private AppAuthSmsValidateReqBo smsValidateBo(AppAuthSmsLoginReqBo reqBo) {
        AppAuthSmsValidateReqBo validateReqBo = new AppAuthSmsValidateReqBo();
        validateReqBo.setMobile(reqBo.getMobile());
        validateReqBo.setCode(reqBo.getCode());
        validateReqBo.setScene(1);
        return validateReqBo;
    }

    private MemberUser selectUserByMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return null;
        }
        return memberUserMapper.selectOne(Wrappers.<MemberUser>lambdaQuery()
                .eq(MemberUser::getIsDeleted, false)
                .eq(MemberUser::getMobile, mobile)
                .last("LIMIT 1"));
    }

    private MemberUser createMemberUser(String mobile) {
        LocalDateTime now = LocalDateTime.now();
        MemberUser user = new MemberUser();
        user.setMobile(mobile);
        user.setStatus(0);
        user.setNickname("会员" + mobile.substring(Math.max(0, mobile.length() - 4)));
        user.setPoint(0);
        user.setExperience(0);
        user.setRegisterTerminal(20);
        user.setRegisterIp("");
        user.setIsDeleted(false);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        memberUserMapper.insert(user);
        return user;
    }

    private MemberUser createSocialMemberUser(SocialUser socialUser) {
        LocalDateTime now = LocalDateTime.now();
        MemberUser user = new MemberUser();
        user.setMobile("social_" + socialUser.getOpenid().substring(Math.max(0, socialUser.getOpenid().length() - 24)));
        user.setStatus(0);
        user.setNickname(StringUtils.isBlank(socialUser.getNickname()) ? "社交会员" : socialUser.getNickname());
        user.setAvatar(socialUser.getAvatar());
        user.setPoint(0);
        user.setExperience(0);
        user.setRegisterTerminal(20);
        user.setRegisterIp("");
        user.setIsDeleted(false);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        memberUserMapper.insert(user);
        return user;
    }

    private boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        if (StringUtils.isBlank(rawPassword) || StringUtils.isBlank(encodedPassword)) {
            return false;
        }
        if (rawPassword.equals(encodedPassword)) {
            return true;
        }
        try {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            return passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (Exception ex) {
            log.warn("[isPasswordMatch][会员密码格式不兼容，按不匹配处理]");
            return false;
        }
    }

    private void validateUserEnabled(MemberUser user) {
        if (STATUS_DISABLE == (user.getStatus() == null ? 0 : user.getStatus())) {
            throw new ServiceException("会员账号已被禁用");
        }
    }

    private AppAuthLoginRespVo createTokenAfterLoginSuccess(MemberUser user, int loginType, String openid) {
        MemberUser updateUser = new MemberUser();
        updateUser.setId(user.getId());
        updateUser.setLoginDate(LocalDateTime.now());
        updateUser.setUpdateTime(LocalDateTime.now());
        memberUserMapper.updateById(updateUser);

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put(Oauth2Constants.USER_ID, user.getId());
        claims.put(Oauth2Constants.USER_NAME, user.getMobile());
        claims.put(Oauth2Constants.USER_TYPE, USER_TYPE_MEMBER);
        claims.put(Oauth2Constants.LOGIN_TYPE, loginType);
        claims.put(Oauth2Constants.ROLE_IDS, "");
        claims.put(Oauth2Constants.AVATAR, user.getAvatar());
        claims.put(AuthConstants.DEPT_ID, "");
        claims.put(AuthConstants.PHONE, user.getMobile());
        claims.put(AuthConstants.AUTHORITIES, List.of());
        return buildTokenResponse(claims, openid);
    }

    private AppAuthLoginRespVo buildTokenResponse(Map<String, Object> claims, String openid) {
        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();
        String accessToken = jwt(tokenClaims(claims), accessJti, authProperties.getToken().getLoginTimeout());
        String refreshToken = jwt(tokenClaims(claims), refreshJti, authProperties.getToken().getRefreshTokenTimeout());
        redisUtils.set(AuthConstants.ACCESS_TOKEN_KEY + accessToken, claims, authProperties.getToken().getLoginTimeout());
        redisUtils.set(AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + accessToken, claims, authProperties.getToken().getLoginTimeout());
        redisUtils.set(AuthConstants.REFRESH_TOKEN_KEY + refreshToken, claims, authProperties.getToken().getRefreshTokenTimeout());

        AppAuthLoginRespVo respVo = new AppAuthLoginRespVo();
        respVo.setUserId(Long.valueOf(String.valueOf(claims.get(Oauth2Constants.USER_ID))));
        respVo.setAccessToken(accessToken);
        respVo.setRefreshToken(refreshToken);
        respVo.setExpiresTime(LocalDateTime.now().plus(authProperties.getToken().getLoginTimeout()));
        respVo.setExpiresIn(authProperties.getToken().getLoginTimeoutSeconds());
        respVo.setOpenid(openid);
        return respVo;
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

    private String jwt(Map<String, Object> claims, String jti, java.time.Duration ttl) {
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

    private Map<String, Object> toClaimsMap(Object cached) {
        if (cached instanceof Map<?, ?> map) {
            Map<String, Object> result = new LinkedHashMap<>();
            map.forEach((key, value) -> result.put(String.valueOf(key), value));
            return result;
        }
        Claims claims = Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(String.valueOf(cached))
                .getPayload();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(Oauth2Constants.USER_ID, claims.get(Oauth2Constants.USER_ID));
        result.put(Oauth2Constants.USER_NAME, claims.get(Oauth2Constants.USER_NAME));
        result.put(Oauth2Constants.USER_TYPE, claims.get(Oauth2Constants.USER_TYPE));
        result.put(Oauth2Constants.LOGIN_TYPE, claims.get(Oauth2Constants.LOGIN_TYPE));
        result.put(Oauth2Constants.ROLE_IDS, claims.get(Oauth2Constants.ROLE_IDS));
        result.put(Oauth2Constants.AVATAR, claims.get(Oauth2Constants.AVATAR));
        result.put(AuthConstants.DEPT_ID, claims.get(AuthConstants.DEPT_ID));
        result.put(AuthConstants.PHONE, claims.get(AuthConstants.PHONE));
        result.put(AuthConstants.AUTHORITIES, claims.get(AuthConstants.AUTHORITIES));
        return result;
    }

}

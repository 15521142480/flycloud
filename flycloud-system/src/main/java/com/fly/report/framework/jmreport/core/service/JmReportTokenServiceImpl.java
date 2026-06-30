package com.fly.report.framework.jmreport.core.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.constant.AuthConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.security.user.FlyUser;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.utils.auth.TokenUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 积木报表 Token 校验服务。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
public class JmReportTokenServiceImpl implements JmReportTokenServiceI {

    private static final String JM_TOKEN_HEADER = "X-Access-Token";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final RedisUtils redisUtils;

    private final AuthProperties authProperties;

    @Override
    public HttpHeaders customApiHeader() {
        HttpHeaders headers = new HttpHeaders();
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return headers;
        }
        String token = request.getHeader(JM_TOKEN_HEADER);
        if (StrUtil.isNotBlank(token)) {
            headers.add(AUTHORIZATION_HEADER, token.startsWith("Bearer ") ? token : "Bearer " + token);
        }
        return headers;
    }

    @Override
    public Boolean verifyToken(String token) {
        if (UserUtils.getCurUserId() != null) {
            return true;
        }
        return buildLoginUserByToken(token) != null;
    }

    @Override
    public String getUsername(String token) {
        Long userId = UserUtils.getCurUserId();
        if (userId != null) {
            return String.valueOf(userId);
        }
        FlyUser user = buildLoginUserByToken(token);
        return user == null ? null : String.valueOf(user.getId());
    }

    @Override
    public String[] getRoles(String token) {
        FlyUser user = resolveLoginUser(token);
        if (user == null) {
            return null;
        }
        return new String[]{"admin"};
    }

    @Override
    public String getTenantId() {
        FlyUser user = UserUtils.getCurUser();
        return user == null ? null : user.getDeptId();
    }

    @Override
    public String[] getPermissions(String token) {
        return resolveLoginUser(token) == null ? null : new String[]{"*:*:*"};
    }

    /**
     * 根据 JmReport 传入的 token 构建登录用户。
     */
    private FlyUser buildLoginUserByToken(String token) {
        String accessToken = TokenUtils.getToken(token);
        if (StrUtil.isBlank(accessToken)) {
            return null;
        }
        Claims tokenClaims = SecurityUtils.getClaims(accessToken);
        Map<String, Object> claims = cachedClaims(accessToken);
        if (tokenClaims == null || claims == null) {
            return null;
        }
        refreshTokenExpire(accessToken);
        FlyUser user = buildPrincipal(claims);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                AuthorityUtils.createAuthorityList(authorities(claims))
        );
        HttpServletRequest request = currentRequest();
        if (request != null) {
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return user;
    }

    /**
     * 获取当前登录用户；没有上下文时尝试使用 JmReport token 恢复。
     */
    private FlyUser resolveLoginUser(String token) {
        FlyUser user = UserUtils.getCurUser();
        return user != null ? user : buildLoginUserByToken(token);
    }

    /**
     * 获取 Redis 中缓存的登录声明。
     */
    private Map<String, Object> cachedClaims(String token) {
        Object cached = redisUtils.get(AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + token);
        if (cached == null) {
            cached = redisUtils.get(AuthConstants.ACCESS_TOKEN_KEY + token);
        }
        if (cached instanceof Map<?, ?> map) {
            Map<String, Object> claims = new LinkedHashMap<>();
            map.forEach((key, value) -> claims.put(String.valueOf(key), value));
            return claims;
        }
        return null;
    }

    /**
     * 刷新访问令牌有效期。
     */
    private void refreshTokenExpire(String token) {
        long timeout = authProperties.getToken().getLoginTimeoutSeconds();
        redisUtils.expire(AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + token, timeout);
        redisUtils.expire(AuthConstants.ACCESS_TOKEN_KEY + token, timeout);
    }

    /**
     * 根据缓存声明构建 flycloud 登录用户。
     */
    private FlyUser buildPrincipal(Map<String, Object> claims) {
        return new FlyUser(
                Convert.toLong(claims.get(Oauth2Constants.USER_ID)),
                Convert.toInt(claims.get(Oauth2Constants.USER_TYPE), 0),
                Convert.toInt(claims.get(Oauth2Constants.LOGIN_TYPE), 0),
                Convert.toStr(claims.get(AuthConstants.DEPT_ID), ""),
                Convert.toStr(claims.get(AuthConstants.PHONE), ""),
                Convert.toStr(claims.get(Oauth2Constants.AVATAR), ""),
                Convert.toStr(claims.get(Oauth2Constants.ROLE_IDS), ""),
                Convert.toStr(claims.get(Oauth2Constants.USER_NAME), ""),
                "",
                true,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList(authorities(claims))
        );
    }

    /**
     * 解析权限标识。
     */
    private String[] authorities(Map<String, Object> claims) {
        Object value = claims.get(AuthConstants.AUTHORITIES);
        if (value instanceof Collection<?> collection) {
            return collection.stream()
                    .map(Convert::toStr)
                    .filter(StrUtil::isNotBlank)
                    .toArray(String[]::new);
        }
        if (value instanceof String[] array) {
            return array;
        }
        if (value instanceof String text) {
            return StrUtil.splitToArray(text, ',');
        }
        if (value instanceof List<?> list) {
            return list.stream()
                    .map(Convert::toStr)
                    .filter(StrUtil::isNotBlank)
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    /**
     * 获取当前请求。
     */
    private HttpServletRequest currentRequest() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        return attributes.getRequest();
    }

}

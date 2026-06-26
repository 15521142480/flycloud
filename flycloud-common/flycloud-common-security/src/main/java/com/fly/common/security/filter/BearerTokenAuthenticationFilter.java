package com.fly.common.security.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fly.common.config.properties.AuthTokenProperties;
import com.fly.common.constant.AuthConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.security.user.FlyUser;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.utils.auth.TokenUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Spring Security 6 资源服务 Bearer token 认证过滤器。
 */
@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private final RedisUtils redisUtils;

    private final AuthTokenProperties authTokenProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String headerToken = request.getHeader(Oauth2Constants.HEADER_TOKEN_KEY);
        String token = TokenUtils.getToken(headerToken);
        if (StrUtil.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims tokenClaims = SecurityUtils.getClaims(token);
        Map<String, Object> claims = cachedClaims(token);
        if (tokenClaims == null || claims == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 每次请求刷新token
        refreshTokenExpire(token);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                buildPrincipal(claims),
                null,
                AuthorityUtils.createAuthorityList(authorities(claims))
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 把当前用户信息放进spring-security的上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

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

    private void refreshTokenExpire(String token) {
        long timeout = authTokenProperties.getLoginTimeoutSeconds();
        redisUtils.expire(AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + token, timeout);
        redisUtils.expire(AuthConstants.ACCESS_TOKEN_KEY + token, timeout);
    }

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
}

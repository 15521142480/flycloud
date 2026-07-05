package com.fly.common.security.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.constant.AuthConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.security.user.FlyUser;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.utils.auth.TokenUtils;
import com.fly.common.utils.json.JsonUtils;
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

    private final AuthProperties authProperties;


    /**
     * 接口拦截
     */
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
        Map<String, Object> claims = getTokenClaims(token);
        if (tokenClaims == null || claims == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // todo 目前版本只刷新redis的token，但是JWT的exp（过期时间）无法刷新（JwtUtils的buildTokenJwt的Jwts.builder()）
        //  但是不给 JWT 设置 exp， JWT 自身失去过期控制
        //  所以后续需把refreshToken接口和前端无感知刷新功能完善，over后需把此处的refreshTokenExpire(token)删除了
        refreshTokenExpire(token); // 每次请求刷新token

        String[] authoritiesInfo = this.getAuthoritiesList(claims.get(AuthConstants.AUTHORITIES));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                buildPrincipal(claims, authoritiesInfo),
                null,
                AuthorityUtils.createAuthorityList(authoritiesInfo)
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 把当前用户信息放进spring-security的上下文
        // 所以每次请求都能从spring-security的上下文获取当前用户信息：SecurityContextHolder.getContext().getAuthentication()
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }



    /**
     * 获取tokenClaims信息（登录是存放的用户信息体）
     */
    private Map<String, Object> getTokenClaims(String token) {

        Object cached = redisUtils.get(
                AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + token
        );

        if (cached == null) {
            cached = redisUtils.get(
                    AuthConstants.ACCESS_TOKEN_KEY + token
            );
        }

        if (cached == null) {
            return null;
        }

        if (cached instanceof String json) {
            return JsonUtils.parseObject(
                    json,
                    new TypeReference<Map<String, Object>>() {}
            );
        }

        if (cached instanceof Map<?, ?> map) {
            Map<String, Object> claims = new LinkedHashMap<>();
            map.forEach(
                    (key, value) -> claims.put(String.valueOf(key), value)
            );
            return claims;
        }

        return null;
    }


    /**
     * 刷新token时间
     *
     * @param token
    */
    private void refreshTokenExpire(String token) {

        long timeout = authProperties
                .getToken()
                .getLoginTimeoutSeconds();

        redisUtils.expire(
                AuthConstants.GATEWAY_ACCESS_TOKEN_KEY + token,
                timeout
        );

        redisUtils.expire(
                AuthConstants.ACCESS_TOKEN_KEY + token,
                timeout
        );
    }


    /**
     * 构建自定义spring security的用户信息
     *
     * @param claims
     * @param authorities
    */
    private FlyUser buildPrincipal(

            Map<String, Object> claims,
            String[] authorities) {

        return new FlyUser(
                Convert.toLong(claims.get(Oauth2Constants.USER_ID)),
                Convert.toInt(claims.get(Oauth2Constants.USER_TYPE), 0),
                Convert.toInt(claims.get(Oauth2Constants.LOGIN_TYPE), 0),
                Convert.toStr(claims.get(AuthConstants.DEPT_ID), ""),
                Convert.toStr(claims.get(AuthConstants.MOBILE), ""),
                Convert.toStr(claims.get(Oauth2Constants.AVATAR), ""),
                Convert.toStr(claims.get(Oauth2Constants.ROLE_IDS), ""),
                Convert.toStr(claims.get(Oauth2Constants.USER_NAME), ""),
                "",
                true,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList(authorities)
        );
    }



    /**
     * 获取授权的权限组（登录存放的AuthConstants.AUTHORITIES）
     *
     * @param authorities
    */
    private String[] getAuthoritiesList(Object authorities) {

        // 包含List、Map、Set等
        if (authorities instanceof Collection<?> collection) {
            return collection.stream()
                    .map(Convert::toStr)
                    .filter(StrUtil::isNotBlank)
                    .toArray(String[]::new);
        }

        if (authorities instanceof String[] array) {
            return array;
        }

        if (authorities instanceof String text) {
            return StrUtil.splitToArray(text, ',');
        }

        return new String[0];
    }

}

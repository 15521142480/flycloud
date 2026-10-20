package com.fly.common.security.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.constant.AuthConstants;
import com.fly.common.constant.Oauth2Constants;
import com.fly.common.domain.model.R;
import com.fly.common.exception.TokenException;
import com.fly.common.redis.utils.RedisUtils;
import com.fly.common.security.handler.CustomAuthenticationEntryPoint;
import com.fly.common.security.user.FlyUser;
import com.fly.common.utils.ResponseUtils;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.utils.auth.TokenResolveUtils;
import com.fly.common.utils.feign.FeignSignatureUtils;
import com.fly.common.utils.json.JsonUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Spring Security 6 资源服务认证过滤器，统一处理Feign内部签名和Bearer Token认证。
 *
 * @author lxs
 */
@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

    /** 用户登录态缓存访问工具。 */
    private final RedisUtils redisUtils;

    /** 用户令牌与内部接口签名配置。 */
    private final AuthProperties authProperties;

    /** 统一输出未认证响应，供客户端清理失效登录态。 */
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;


    /**
     * 认证用户令牌或内部接口签名；无效令牌返回 401，避免解析异常变成 500。
     *
     * @param request 当前 HTTP 请求
     * @param response 当前 HTTP 响应
     * @param filterChain 后续过滤器链
     * @throws ServletException 后续过滤器处理失败时抛出
     * @throws IOException 读取请求或写入响应失败时抛出
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = TokenResolveUtils.resolve(request);
        String path = FeignSignatureUtils.normalizePath(request.getRequestURI());
        if (FeignSignatureUtils.isFeignPath(path) && authProperties.getFeign().isEnabled()) {
            if (!verifyFeignSignature(request, path)) {
                ResponseUtils.responseWriter(
                        response,
                        "application/json;charset=UTF-8",
                        HttpServletResponse.SC_UNAUTHORIZED,
                        R.failed("Feign内部接口签名无效")
                );
                return;
            }
            if (StrUtil.isBlank(token)) {
                setFeignAuthentication(request);
                filterChain.doFilter(request, response);
                return;
            }
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (StrUtil.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims tokenClaims;
        try {
            tokenClaims = SecurityUtils.getClaims(token);
        } catch (TokenException exception) {
            // 当前过滤器位于安全异常转换过滤器之前，必须在此转换令牌解析异常。
            authenticationEntryPoint.commence(request, response,
                    new BadCredentialsException("token无效或已过期", exception));
            return;
        }
        if (tokenClaims == null) {
            authenticationEntryPoint.commence(request, response,
                    new BadCredentialsException("token无效或已过期"));
            return;
        }
        Map<String, Object> claims = getTokenClaims(token);
        if (claims == null) {
            // 携带了令牌却查不到登录态时统一要求重新登录，公开接口也不能把失效令牌当作有效会话。
            authenticationEntryPoint.commence(request, response,
                    new BadCredentialsException("token无效或已过期"));
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
     * 校验Feign内部接口签名。
     *
     * @param request 请求对象
     * @param path 规范化后的请求路径
     * @return 是否校验通过
     */
    private boolean verifyFeignSignature(HttpServletRequest request, String path) {
        return FeignSignatureUtils.verify(
                authProperties.getFeign().getSecret(),
                request.getMethod(),
                path,
                request.getHeader(FeignSignatureUtils.HEADER_TIMESTAMP),
                request.getHeader(FeignSignatureUtils.HEADER_NONCE),
                request.getHeader(FeignSignatureUtils.HEADER_SIGNATURE),
                authProperties.getFeign().getExpireSeconds());
    }


    /**
     * 为未携带用户Token的Feign请求设置内部服务认证信息。
     *
     * @param request 请求对象
     */
    private void setFeignAuthentication(HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "feign-internal",
                null,
                AuthorityUtils.NO_AUTHORITIES);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
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
            cached = redisUtils.get(
                    AuthConstants.REFRESH_TOKEN_KEY + token
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

        redisUtils.expire(
                AuthConstants.REFRESH_TOKEN_KEY + token,
                authProperties.getToken().getRefreshTokenTimeoutSeconds()
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

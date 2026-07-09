package com.fly.common.security.filter;

import com.fly.common.config.properties.AuthProperties;
import com.fly.common.domain.model.R;
import com.fly.common.utils.ResponseUtils;
import com.fly.common.utils.feign.FeignSignatureUtils;
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

/**
 * Feign内部接口签名认证过滤器。
 *
 * @author lxs
 * @date 2026/7/9
 */
@RequiredArgsConstructor
public class FeignSignatureAuthenticationFilter extends OncePerRequestFilter {

    private final AuthProperties authProperties;

    /**
     * 校验/feign开头的内部接口签名，校验成功后写入Spring Security认证上下文。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = FeignSignatureUtils.normalizePath(request.getRequestURI());
        if (!FeignSignatureUtils.isFeignPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!authProperties.getFeign().isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!verify(request, path)) {
            ResponseUtils.responseWriter(
                    response,
                    "application/json;charset=UTF-8",
                    HttpServletResponse.SC_UNAUTHORIZED,
                    R.failed("Feign内部接口签名无效")
            );
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "feign-internal",
                null,
                AuthorityUtils.NO_AUTHORITIES);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    /**
     * 验证当前请求的Feign签名。
     *
     * @param request 请求对象
     * @param path 请求路径
     * @return 是否验证通过
     */
    private boolean verify(HttpServletRequest request, String path) {
        return FeignSignatureUtils.verify(
                authProperties.getFeign().getSecret(),
                request.getMethod(),
                path,
                request.getHeader(FeignSignatureUtils.HEADER_TIMESTAMP),
                request.getHeader(FeignSignatureUtils.HEADER_NONCE),
                request.getHeader(FeignSignatureUtils.HEADER_SIGNATURE),
                authProperties.getFeign().getExpireSeconds());
    }
}

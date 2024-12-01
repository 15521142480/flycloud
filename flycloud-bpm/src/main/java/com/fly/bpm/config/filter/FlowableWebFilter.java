package com.fly.bpm.config.filter;

import com.fly.bpm.flowable.utils.FlowableUtils;
import com.fly.common.security.util.UserUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Flowable Web 过滤器，将 userId 设置到 {@link org.flowable.common.engine.impl.identity.Authentication} 中
 *
 * @author lxs
 */
public class FlowableWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {

            // 设置工作流的用户
            Long userId = UserUtils.getCurUserId();
            if (userId != null) {
                FlowableUtils.setAuthenticatedUserId(userId);
            }
            // 过滤
            chain.doFilter(request, response);

        } finally {

            // 清理
            FlowableUtils.clearAuthenticatedUserId();
        }
    }
}

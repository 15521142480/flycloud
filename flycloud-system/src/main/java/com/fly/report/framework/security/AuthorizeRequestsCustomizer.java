package com.fly.report.framework.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * 报表模块安全请求自定义器。
 *
 * @author lxs
 * @date 2026-07-01
 */
public interface AuthorizeRequestsCustomizer {

    /**
     * 自定义报表模块需要放行或保护的请求路径。
     */
    void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry);

}

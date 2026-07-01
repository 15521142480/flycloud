package com.fly.im.framework.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * 授权请求定制适配。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface AuthorizeRequestsCustomizer {

    void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry);

}

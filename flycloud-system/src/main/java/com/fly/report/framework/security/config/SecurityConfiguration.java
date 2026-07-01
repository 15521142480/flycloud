package com.fly.report.framework.security.config;

import com.fly.report.framework.security.AuthorizeRequestsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * 报表模块 Security 适配配置。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Configuration(value = "reportSecurityConfiguration", proxyBeanMethods = false)
public class SecurityConfiguration {

    /**
     * 放行积木报表、积木仪表盘等内置入口。
     */
    @Bean("reportAuthorizeRequestsCustomizer")
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {

            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                registry.requestMatchers("/jmreport/**").permitAll();
                registry.requestMatchers("/drag/**").permitAll();
                registry.requestMatchers("/jimubi/**").permitAll();
            }

        };
    }

}

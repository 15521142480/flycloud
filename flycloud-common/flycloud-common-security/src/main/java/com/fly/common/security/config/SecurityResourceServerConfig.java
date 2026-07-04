package com.fly.common.security.config;

import cn.hutool.core.convert.Convert;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.security.component.PermissionService;
import com.fly.common.security.handler.CustomAccessDeniedHandler;
import com.fly.common.security.handler.CustomAuthenticationEntryPoint;
import com.fly.common.security.handler.CustomAuthenticationFailureHandler;
import com.fly.common.security.handler.CustomAuthenticationSuccessHandler;
import com.fly.common.security.filter.BearerTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * security 资源服务配置
 *
 * @author lxs
 * @date 2026/4/28
 */
@Order(5)
@AutoConfiguration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // 激活方法上的@PreAuthorize注解
//@EnableConfigurationProperties({ServerResourceSecurityProperties.class})
@Import({
        PermissionService.class,
        BearerTokenAuthenticationFilter.class,
        CustomAccessDeniedHandler.class,
        CustomAuthenticationEntryPoint.class,
        CustomAuthenticationFailureHandler.class,
        CustomAuthenticationSuccessHandler.class
})
@RequiredArgsConstructor
public class SecurityResourceServerConfig {


//    private final SecurityAuthorizationProperties securityAuthorizationProperties;
    private final AuthProperties authProperties;


    /**
     * 放行和认证规则
     */
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity httpSecurity,
                                                                 CustomAuthenticationEntryPoint authenticationEntryPoint,
                                                                 CustomAccessDeniedHandler accessDeniedHandler,
                                                                 BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter) throws Exception {

        String[] ignoreUrls = Convert.toStrArray(authProperties.getIgnoreUrls());
        return httpSecurity
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(bearerTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(ignoreUrls).permitAll()
                        .requestMatchers(
                                "/test/**",
                                "/css/**",
                                "/swagger-ui/index.html",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs"
                        ).permitAll()
                        .anyRequest().authenticated())
                .build();
    }


    /**
     * 自定义授权相关处理器
     * todo 2024-9-4: 无论是这里重写还是自己注入bean，貌似都会被全局异常拦截，目前的做法是新建对于该异常的拦截器：SecurityExceptionHandler
     *
     */
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//                .accessDeniedHandler(new CustomAccessDeniedHandler());
//    }

}

package com.fly.common.security.config;

import cn.hutool.core.convert.Convert;
import com.fly.common.config.properties.AuthProperties;
import com.fly.common.security.component.PermissionService;
import com.fly.common.security.filter.FeignSignatureAuthenticationFilter;
import com.fly.common.security.handler.CustomAccessDeniedHandler;
import com.fly.common.security.handler.CustomAuthenticationEntryPoint;
import com.fly.common.security.handler.CustomAuthenticationFailureHandler;
import com.fly.common.security.handler.CustomAuthenticationSuccessHandler;
import com.fly.common.security.filter.BearerTokenAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        FeignSignatureAuthenticationFilter.class,
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

    private final ApplicationContext applicationContext;


    /**
     * 放行和认证规则
     */
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity httpSecurity,
                                                                 CustomAuthenticationEntryPoint authenticationEntryPoint,
                                                                 CustomAccessDeniedHandler accessDeniedHandler,
                                                                 FeignSignatureAuthenticationFilter feignSignatureAuthenticationFilter,
                                                                 BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter) throws Exception {

        String[] ignoreUrls = Convert.toStrArray(authProperties.getIgnoreUrls());
        Map<RequestMethod, Set<String>> permitAllUrls = getPermitAllUrlsFromAnnotations();
        return httpSecurity
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(feignSignatureAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(bearerTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(registry -> {
                    // SseEmitter 写入和完成时会触发容器 ASYNC 分派；初始请求已完成认证，异步分派无需重复认证。
                    registry.dispatcherTypeMatchers(DispatcherType.ASYNC, DispatcherType.ERROR).permitAll();
                    // 公开接口统一通过 @PermitAll 显式声明，未声明的业务接口默认要求登录。
                    permitAllUrls.forEach((method, urls) -> registry
                            .requestMatchers(HttpMethod.valueOf(method.name()), toArray(urls)).permitAll());
                    registry.requestMatchers(ignoreUrls).permitAll();
                    registry.anyRequest().authenticated();
                })
                .build();
    }

    /**
     * 收集 Controller 中使用 {@link PermitAll} 声明的公开接口。
     */
    private Map<RequestMethod, Set<String>> getPermitAllUrlsFromAnnotations() {
        Map<RequestMethod, Set<String>> result = new EnumMap<>(RequestMethod.class);
        RequestMappingHandlerMapping mapping = applicationContext.getBean(
                "requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : mapping.getHandlerMethods().entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(PermitAll.class)
                    && !handlerMethod.getBeanType().isAnnotationPresent(PermitAll.class)) {
                continue;
            }
            Set<RequestMethod> requestMethods = entry.getKey().getMethodsCondition().getMethods();
            if (requestMethods.isEmpty()) {
                requestMethods = Set.of(RequestMethod.values());
            }
            for (RequestMethod requestMethod : requestMethods) {
                result.computeIfAbsent(requestMethod, key -> new HashSet<>())
                        .addAll(entry.getKey().getPatternValues());
            }
        }
        return result;
    }

    /**
     * 将公开接口地址集合转换为 Spring Security 所需的数组。
     */
    private String[] toArray(Set<String> urls) {
        return urls == null ? new String[0] : urls.toArray(String[]::new);
    }


    /**
     * 密码加密模式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new BCryptPasswordEncoder();
    }


}

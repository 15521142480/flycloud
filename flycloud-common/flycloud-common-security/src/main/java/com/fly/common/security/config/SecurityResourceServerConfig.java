package com.fly.common.security.config;

import cn.hutool.core.convert.Convert;
import com.fly.common.security.config.properties.ServerResourceSecurityProperties;
import com.fly.common.security.handler.AccessDeniedHandler;
import com.fly.common.security.handler.AuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;


/**
 * security 资源服务配置
 *
 * @author lxs
 * @date 2023/4/28
 */
@Order(5)
@EnableResourceServer // 开启资源服务器校验
//@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 激活方法上的@PreAuthorize注解
@RequiredArgsConstructor
public class SecurityResourceServerConfig extends ResourceServerConfigurerAdapter { // todo ResourceServerConfigurerAdapter 资源服务端的接口切面配置; 用于保护oauth要开放的资源，同时主要作用于client端以及token的认证(Bearer auth)


//    private final SecurityAuthorizationProperties securityAuthorizationProperties;
    private final ServerResourceSecurityProperties serverResourceSecurityProperties;

    private final RedisConnectionFactory redisConnectionFactory;



    /**
     * 放行和认证规则
     */
    @Override
    @SneakyThrows
    public void configure(HttpSecurity httpSecurity) {

        httpSecurity.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();

        // 放行白名单接口
        registry.antMatchers(Convert.toStrArray(serverResourceSecurityProperties.getIgnoreUrls())).permitAll();

        // 放行静态资源
        registry.antMatchers(
                "/temp/**",
                "/css/**",
                "/swagger-ui/index.html",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/v3/api-docs"
        ).permitAll();

        // 自定义授权错误后响应的内容
//        httpSecurity.oauth2ResourceServer()
//                .authenticationEntryPoint(authenticationEntryPoint);

        registry.anyRequest().authenticated().and().csrf().disable();
    }


    /**
     * 自定义授权相关处理器
     *
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(new AuthenticationEntryPoint())
                .accessDeniedHandler(new AccessDeniedHandler());
    }


    // =============================================================================

    /**
     * 配置token存储到redis中
     */
    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

}

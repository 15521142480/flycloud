package com.fly.common.security.config;

import cn.hutool.core.convert.Convert;
import com.fly.common.security.config.properties.SecurityAuthorizationProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * security 资源服务配置
 *
 * @author lxs
 * @date 2023/4/28
 */
@Order(5)
//@Configuration
@EnableResourceServer // 开启资源服务器校验
//@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 激活方法上的@PreAuthorize注解
@RequiredArgsConstructor
public class SecurityResourceServerConfig extends ResourceServerConfigurerAdapter { // todo ResourceServerConfigurerAdapter 资源服务端的接口切面配置; 用于保护oauth要开放的资源，同时主要作用于client端以及token的认证(Bearer auth)

    private final RedisConnectionFactory redisConnectionFactory;
    private final SecurityAuthorizationProperties securityAuthorizationProperties;
//    private final AuthenticationEntryPoint authenticationEntryPoint;



    /**
     * 放行和认证规则
     */
    @Override
    @SneakyThrows
    public void configure(HttpSecurity httpSecurity) {

        httpSecurity.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();

        // 放行白名单接口
//        securityAuthorizationProperties.getIgnoreUrls().forEach(
//                url -> registry.antMatchers(url).permitAll()
//        );
        registry.antMatchers(Convert.toStrArray(securityAuthorizationProperties.getIgnoreUrls())).permitAll();

        // 放行静态资源
        registry.antMatchers(
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
     * 公钥解析器配置
     *
     */

    @Resource
    TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
//                .resourceId(RESOURCE_ID)//资源 id
                .tokenStore(tokenStore)
                .stateless(true);
    }

//    @Bean
//    public JwtTokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
//        return new JwtTokenStore(jwtAccessTokenConverter);
//    }
//
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setVerifierKey(getPubKey());
//        return converter;
//    }
//
//
//    // 公钥位置
//    private static final String PUBLIC_KEY = "public.key";
//
//    private String getPubKey() {
//        Resource resource = new ClassPathResource(PUBLIC_KEY);
//        try {
//            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
//            BufferedReader br = new BufferedReader(inputStreamReader);
//            return br.lines().collect(Collectors.joining("\n"));
//        } catch (IOException ioe) {
//            return null;
//        }
//    }


    /**
     * 初始化 RedisTokenStore 用于将 token 存储至 Redis
     *
     */
//    @Bean
//    public RedisTokenStore redisTokenStore() {
//
//        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
//        redisTokenStore.setPrefix(SecurityConstants.FLY_PREFIX + SecurityConstants.OAUTH_PREFIX); // 设置key的层级前缀，方便查询
//        return redisTokenStore;
//    }



}

package com.fly.auth.config;

import cn.hutool.core.convert.Convert;
import com.fly.auth.config.properties.SecurityAuthorizationProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * web安全配置
 *
 * @author lxs
 * @date 2023/5/3
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@Order(-1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { // todo WebSecurityConfigurerAdapter web端的接口切面配置; 用于保护oauth相关的endpoints，同时主要作用于用户的登录(form login,Basic auth)


    private final SecurityAuthorizationProperties securityAuthorizationProperties;

    /**
     * 放行和认证规则
     */
    @Override
    @SneakyThrows
    public void configure(HttpSecurity httpSecurity) {

        httpSecurity.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();

//        securityAuthorizationProperties.getIgnoreUrls().forEach(
//                url -> registry.antMatchers(url).permitAll()
//        );
        registry.antMatchers(Convert.toStrArray(securityAuthorizationProperties.getIgnoreUrls())).permitAll();


        registry.anyRequest().authenticated().and().csrf().disable();
    }


    /**
     * 不拦截静态资源
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {

        web.ignoring().antMatchers(
                "/css/**",
                "/swagger-ui/index.html",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/v3/api-docs"
        );
    }


    /**
     * 认证管理对象
     *
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /***
     * 采用BCryptPasswordEncoder对密码进行编码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}

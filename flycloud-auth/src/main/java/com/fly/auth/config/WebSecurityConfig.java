package com.fly.auth.config;

import cn.hutool.core.convert.Convert;
import com.fly.auth.service.impl.UserDetailsServiceImpl;
import com.fly.common.security.config.properties.ServerResourceSecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * web安全配置
 *
 * <p>
 * WebSecurityConfigurerAdapter web端的接口切面配置; 用于保护oauth相关的endpoints，同时主要作用于用户的登录(form login,Basic auth)
 * todo 但springboot2.7版本后弃用WebSecurityConfigurerAdapter，改用配置注入Bean【SecurityFilterChain】的方式，后续再调整
 *
 * @author lxs
 * @date 2023/5/3
 */
@Order(4)
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { // todo WebSecurityConfigurerAdapter web端的接口切面配置; 用于保护oauth相关的endpoints，同时主要作用于用户的登录(form login,Basic auth)


    private final ServerResourceSecurityProperties serverResourceSecurityProperties;


    /**
     * 放行和认证规则
     *
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    @SneakyThrows
    public void configure(HttpSecurity httpSecurity) {

        httpSecurity.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();

//        securityAuthorizationProperties.getIgnoreUrls().forEach(
//                url -> registry.antMatchers(url).permitAll()
//        );
        registry.antMatchers(Convert.toStrArray(serverResourceSecurityProperties.getIgnoreUrls())).permitAll();


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
     * 用户自定义和管理身份验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }


    /**
     * 重写用户实现
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }


    // ==================================================================================



    /**
     * 密码加密模式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new BCryptPasswordEncoder();
    }

//    public static void main(String[] args) {
//
////        BCryptPasswordEncoder brcy = new BCryptPasswordEncoder();
////        String password = brcy.encode("admin123");
////        System.out.println("password:" + password);
////
////        boolean isTure = brcy.matches("admin123456", password);
////        System.out.println("isTure:" + isTure);
//
//        String md5str = CryptoUtils.encodeMD5("admin123");
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        String password = encoder.encode(md5str);
//        System.out.println("md5str:" + md5str);
//        System.out.println("password:" + password);
//
//        boolean isTure = encoder.matches(CryptoUtils.encodeMD5("admin123"), password);
//        System.out.println("isTure:" + isTure);
//    }


    /**
     * 认证管理对象（必须要定义，否则不支持grant_type=password模式）
     *
     */
    @Bean
    @Override
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }



}

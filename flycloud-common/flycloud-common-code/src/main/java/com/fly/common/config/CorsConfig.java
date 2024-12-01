package com.fly.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 *
 * @author lxs
 * @date 2023/3/22
 */
@Configuration
public class CorsConfig {


    /**
     * 解决跨域问题
     */
//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedOriginPattern("*");
//        corsConfiguration.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(source);
//    }

    private static final long MAX_AGE = 3600 * 24;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    /**
     * 创建 CorsFilter Bean，解决跨域问题
     */
//    @Bean
//    public FilterRegistrationBean<CorsFilter> corsFilterBean() {
//
//        // 创建 CorsConfiguration 对象
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOriginPattern("*"); // 设置访问源地址
//        config.addAllowedHeader("*"); // 设置访问源请求头
//        config.addAllowedMethod("*"); // 设置访问源请求方法
//        // 创建 UrlBasedCorsConfigurationSource 对象
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config); // 对接口配置跨域设置
//
//        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
//        bean.setOrder(Integer.MAX_VALUE);
//
//        return bean;
//    }
}

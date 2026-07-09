package com.fly.common.feign.config;

import com.fly.common.config.properties.AuthProperties;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Feign 自动配置
 *
 * @author lxs
 */
@AutoConfiguration
public class FeignAutoConfiguration {

    @Bean
    public RequestInterceptor tokenFeignClientInterceptor(AuthProperties authProperties) {
        return new TokenFeignClientInterceptor(authProperties);
    }
}

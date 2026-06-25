package com.fly.common.feign.config;

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
    public RequestInterceptor tokenFeignClientInterceptor() {
        return new TokenFeignClientInterceptor();
    }
}

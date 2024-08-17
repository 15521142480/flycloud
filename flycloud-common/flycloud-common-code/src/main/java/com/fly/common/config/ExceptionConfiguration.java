package com.fly.common.config;

import com.fly.common.factory.YamlPropertySourceFactory;
import com.fly.common.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 统一异常处理配置
 * @author xuzhanfu
 */
//@AutoConfiguration
@Configuration
@ComponentScan(value="com.fly.common.handler")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:common-error.yml")
public class ExceptionConfiguration {


    /**
     * 全局异常处理bean
     */
    @Bean
    public GlobalExceptionHandler baseExceptionHandler(){
        return new GlobalExceptionHandler();
    }
}
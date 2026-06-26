package com.fly.auth;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.fly.common.security.config.properties.AuthTokenProperties;
import com.fly.common.database.config.MybatisPlusConfig;
import com.fly.common.doc.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 授权服务
 *
 * @author lxs
 * @date 2023/4/28
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class,
        DynamicDataSourceAutoConfiguration.class,
        MybatisPlusConfig.class
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.fly.system.api.**")
@EnableSwaggerDoc
@EnableConfigurationProperties(AuthTokenProperties.class)
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}

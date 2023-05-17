package com.fly.auth;

import com.fly.common.doc.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 授权服务
 *
 * @author lxs
 * @date 2023/4/28
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.fly.api")
@EnableDiscoveryClient
@EnableSwaggerDoc
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}

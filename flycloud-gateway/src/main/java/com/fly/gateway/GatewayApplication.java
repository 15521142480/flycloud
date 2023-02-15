package com.fly.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关服务
 *
 * @author: lxs
 * @date: 2023/2/10
 */
@SpringBootApplication
@EnableDiscoveryClient // 添加开启服务注册的注解，向nacos发送注册信息，成为一个服务
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
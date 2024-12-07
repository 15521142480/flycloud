package com.fly.bpm;

import com.fly.common.doc.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 工作流-系统启动类
 *
 * @author lxs
 * @date 2024/11/24
 */
@SpringBootApplication(scanBasePackages = { "com.fly.**" }) // , "com.fly.common.security.*"
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fly.**"})
@EnableSwaggerDoc
public class BpmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpmApplication.class, args);
        System.out.println("bpm,Hello world!");
    }
}
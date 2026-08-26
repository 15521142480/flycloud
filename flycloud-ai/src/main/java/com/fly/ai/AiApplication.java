package com.fly.ai;

import com.fly.common.doc.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * AI 服务启动类。
 *
 * @author lxs
 * @date 2026-08-25
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = { "com.fly.**" })
@EnableSwaggerDoc
public class AiApplication {

    /**
     * 启动 AI 微服务。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }

}

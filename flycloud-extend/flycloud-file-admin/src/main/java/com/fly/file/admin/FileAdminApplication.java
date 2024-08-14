package com.fly.file.admin;

import com.fly.common.doc.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 文件服务后台 启动类
 *
 * @author lxs
 * @date 2023/4/24
 */
@SpringBootApplication(scanBasePackages = { "com.fly", "com.fly.common.security.*" })
@EnableFeignClients
@EnableDiscoveryClient
@EnableSwaggerDoc
public class FileAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileAdminApplication.class, args);
    }
}

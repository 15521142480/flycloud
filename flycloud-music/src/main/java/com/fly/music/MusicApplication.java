package com.fly.music;

import com.fly.common.doc.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 音乐启动类
 *
 * @author lxs
 * @date 2023/4/23
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableSwaggerDoc
public class MusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class, args);
    }
}

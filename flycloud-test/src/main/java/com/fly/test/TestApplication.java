package com.fly.test;

import com.fly.common.doc.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 商城启动类
 *
 * @author lxs
 * @date 2023/2/13
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.fly.api")
@EnableDiscoveryClient
@EnableSwaggerDoc
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}
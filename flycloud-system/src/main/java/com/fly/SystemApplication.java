package com.fly;

import com.fly.common.doc.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 系统启动类
 *
 * @author lxs
 * @date 2026/2/13
 */
@SpringBootApplication(scanBasePackages = { "com.fly.**" }) // , "com.fly.common.security.*"
@EnableDiscoveryClient
//@EnableFeignClients(basePackages = {"com.fly.system.api.*"})
@EnableFeignClients(basePackages = {"com.fly.**"})
@EnableSwaggerDoc
@EnableAsync
@EnableScheduling
public class SystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

}

package com.fly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统启动类
 *
 * @author lxs
 * @date 2023/2/13
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

}
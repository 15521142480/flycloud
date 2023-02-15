package com.fly.mall;

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
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallApplication.class, args);
	}

}
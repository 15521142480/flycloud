package com.fly.bpm.config;

import com.fly.system.api.feign.ISysDeptApi;
import com.fly.system.api.feign.ISysUserApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = {ISysUserApi.class, ISysDeptApi.class})
public class RpcConfiguration {


}

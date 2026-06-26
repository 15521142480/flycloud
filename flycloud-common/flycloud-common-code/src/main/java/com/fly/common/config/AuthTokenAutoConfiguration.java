package com.fly.common.config;

import com.fly.common.config.properties.AuthProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 登录 Token配置文件
 *
 * @author: lxs
 * @date: 2026/6/26
 */
@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthTokenAutoConfiguration {

}

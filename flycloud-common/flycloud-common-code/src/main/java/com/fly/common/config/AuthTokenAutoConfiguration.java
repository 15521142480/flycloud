package com.fly.common.config;

import com.fly.common.config.properties.AuthTokenProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 登录 Token配置文件
 *
 * @author: lxs
 * @date: 2026/6/26
 */
@AutoConfiguration
@EnableConfigurationProperties(AuthTokenProperties.class)
public class AuthTokenAutoConfiguration {

}

package com.fly.common.redis.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 配置属性
 *
 * @author lxs
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {


    private String host;

    private int port;

    /**
     * Netty线程池数量,默认值 = 当前处理核数量 * 2
     */
    private String password;

    /**
     * 单机服务配置
     */
    private int timeout;

    /**
     * 集群服务配置
     */
    private int database;

}

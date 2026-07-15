package com.fly.common.elasticsearch.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Elasticsearch 公共能力自动配置。
 *
 * <p>连接参数只使用 Spring Boot 官方 {@code spring.elasticsearch.*}；本模块不创建客户端，
 * 仅在官方 {@code ElasticsearchClient} 已自动配置后注册 Flycloud 的治理能力。</p>
 */
@AutoConfiguration(afterName = "org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration")
@EnableConfigurationProperties(FlycloudElasticsearchProperties.class)
@ConditionalOnProperty(prefix = "flycloud.elasticsearch", name = "enabled", havingValue = "true")
public class ElasticsearchAutoConfiguration {
}

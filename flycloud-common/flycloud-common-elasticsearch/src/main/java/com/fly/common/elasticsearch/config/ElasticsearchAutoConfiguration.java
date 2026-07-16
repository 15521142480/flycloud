package com.fly.common.elasticsearch.config;

import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.common.elasticsearch.config.properties.ElasticsearchProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Elasticsearch 公共能力自动配置。
 *
 * <p>连接参数只使用 Spring Boot 官方 {@code spring.elasticsearch.*}。本模块复用 Spring 的
 * {@link ObjectMapper} 创建官方 Java Client 的 JSONP 映射器，确保 {@code LocalDateTime} 等
 * Java 时间类型按项目统一规则序列化；客户端本身仍由 Spring Boot 自动配置。</p>
 */
@AutoConfiguration(beforeName = "org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration")
@EnableConfigurationProperties(ElasticsearchProperties.class)
@ConditionalOnProperty(prefix = "flycloud.elasticsearch", name = "enabled", havingValue = "true")
public class ElasticsearchAutoConfiguration {

    /**
     * 使用 Spring 管理的 ObjectMapper 创建 Elasticsearch 官方 Java Client 的 JSONP 映射器。
     *
     * <p>必须早于 Spring Boot 的 ElasticsearchClientAutoConfiguration 注册。否则 Boot 会创建
     * 不含 JavaTimeModule 的默认映射器，Bulk 写入带 LocalDateTime 字段的 Document 时会失败。</p>
     *
     * @param objectMapper 已注册 JavaTimeModule 的项目统一 JSON 映射器
     * @return Elasticsearch 官方 Java Client 使用的 JSONP 映射器
     */
    @Bean
    @ConditionalOnMissingBean(JsonpMapper.class)
    public JsonpMapper elasticsearchJsonpMapper(ObjectMapper objectMapper) {
        return new JacksonJsonpMapper(objectMapper);
    }
}

package com.fly.commom.elasticsearch.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Elasticsearch 配置字段属性
 *
 * @author: lxs
 * @date: 2025/8/21
 */
@Data
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchProperties {

    private Rest rest;

    @Data
    @NoArgsConstructor
    public static class Rest {

//        private List<String> uris = new ArrayList<>();

        /**
         * ip端口 （多个用,隔开）
         */
        private String uris;

        /**
         * 协议
         */
        private String protocol;

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;

    }
}

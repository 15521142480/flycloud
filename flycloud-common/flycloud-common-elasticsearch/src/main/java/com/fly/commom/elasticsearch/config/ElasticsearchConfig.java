package com.fly.commom.elasticsearch.config;

import com.fly.commom.elasticsearch.config.properties.ElasticsearchProperties;
import com.fly.common.factory.YamlPropertySourceFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * elasticsearch 配置
 *
 * @author: lxs
 * @date: 2025/8/21
 */
@Configuration
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:mate-elasticsearch.yml")
@EnableConfigurationProperties(ElasticsearchProperties.class)
@AllArgsConstructor
//@RequiredArgsConstructor
@Slf4j
public class ElasticsearchConfig {


    private final ElasticsearchProperties elasticsearchProperties;



    /**
     * 重写RestHighLevelClient配置
     *
     * RestHighLevelClient 在7.15以上标记为过时，在8.x上为废弃，应使用RestClient
     *
    */
    @Bean
    public RestHighLevelClient restHighLevelClient() {

        final String[] urlList = elasticsearchProperties.getRest().getUris().split(",");
        final HttpHost[] httpHosts = new HttpHost[urlList.length];
        for (int i = 0; i < urlList.length; i++) {
            String[] nodeInfo = urlList[i].split(":");
            httpHosts[i] = new HttpHost(nodeInfo[0].trim(), Integer.parseInt(nodeInfo[1].trim()), elasticsearchProperties.getRest().getProtocol());
        }

        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }



}

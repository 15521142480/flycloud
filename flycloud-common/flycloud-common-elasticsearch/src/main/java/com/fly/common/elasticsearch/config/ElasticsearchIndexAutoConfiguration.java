package com.fly.common.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fly.common.elasticsearch.index.ElasticsearchAliasService;
import com.fly.common.elasticsearch.index.ElasticsearchIndexService;
import com.fly.common.elasticsearch.index.ElasticsearchMappingService;
import com.fly.common.elasticsearch.bulk.ElasticsearchBulkService;
import com.fly.common.elasticsearch.page.ElasticsearchPageExecutor;
import com.fly.common.elasticsearch.projection.ElasticsearchProjectionWriter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * ES 索引、别名、分页、投影等公共能力自动配置。
 *
 * <p>所有 Bean 均允许业务侧按需替换，避免公共模块反向限制业务实现。</p>
 */
@AutoConfiguration(after = ElasticsearchAutoConfiguration.class)
@ConditionalOnBean(ElasticsearchClient.class)
public class ElasticsearchIndexAutoConfiguration {

    /** 注册真实索引与 Alias 原子切换服务。 */
    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchAliasService elasticsearchAliasService(ElasticsearchClient client, FlycloudElasticsearchProperties properties) {
        return new ElasticsearchAliasService(client, properties);
    }

    /** 注册 classpath JSON Mapping 加载服务。 */
    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchMappingService elasticsearchMappingService() {
        return new ElasticsearchMappingService();
    }

    /** 注册真实索引创建、刷新和安全删除服务。 */
    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchIndexService elasticsearchIndexService(ElasticsearchClient client) {
        return new ElasticsearchIndexService(client);
    }

    /** 注册带失败汇总和有限重试的 Bulk 写入服务。 */
    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchBulkService elasticsearchBulkService(ElasticsearchClient client, FlycloudElasticsearchProperties properties) {
        return new ElasticsearchBulkService(client, properties);
    }

    /** 注册排序字段白名单和深分页阈值保护执行器。 */
    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchPageExecutor elasticsearchPageExecutor(ElasticsearchClient client, FlycloudElasticsearchProperties properties) {
        return new ElasticsearchPageExecutor(client, properties);
    }

    /** 注册索引升级窗口中的双写投影服务。 */
    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchProjectionWriter elasticsearchProjectionWriter(ElasticsearchClient client, ElasticsearchAliasService aliasService) {
        return new ElasticsearchProjectionWriter(client, aliasService);
    }
}

package com.fly.common.elasticsearch.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Elasticsearch 客户端配置。
 *
 * <p>配置由 Nacos 的 {@code application-common.yaml} 中 {@code flycloud.elasticsearch} 提供；业务服务只引用该公共模块，
 * 不自行创建客户端。</p>
 */
@Data
@ConfigurationProperties(prefix = "flycloud.elasticsearch")
public class ElasticsearchProperties {

    /**
     * 是否启用 ES 基础设施。
     */
     private boolean enabled = false;

    /**
     * 默认分页大小。
     */
     private int defaultPageSize = 10;

    /**
     * 单页允许的最大结果数。
     */
     private int maxPageSize = 100;

    /**
     * Bulk 单批最大文档数。
     */
     private int bulkSize = 500;

    /**
     * 网络级 Bulk 请求最大重试次数。
     */
     private int bulkRetryTimes = 3;

    /**
     * Bulk 网络重试间隔。
     */
     private Duration bulkRetryInterval = Duration.ofSeconds(1);

    /**
     * 旧版本索引建议保留天数。
     */
     private int indexRetentionDays = 7;

    /**
     * 是否强制一个 Alias 仅指向一个真实写索引。
     */
     private boolean aliasRequireSingleIndex = true;

    /**
     * 超过此偏移量时必须改用 search_after。
     */
     private int searchAfterThreshold = 10_000;

    /**
     * 全量同步并发度；当前会员实现保持单游标串行。
     */
     private int fullSyncConcurrency = 1;

    /**
     * Bulk 完成后是否主动刷新索引使数据可见。
     */
     private boolean refreshAfterBulk = true;
}

package com.fly.common.enums.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Elasticsearch 真实版本索引的生命周期状态。
 */
@Getter
@AllArgsConstructor
public enum ElasticsearchIndexVersionStatusEnum {

    /** 真实索引仍保留在 Elasticsearch 集群中。 */
    AVAILABLE("AVAILABLE", "可用"),

    /** 真实索引已从 Elasticsearch 集群删除。 */
    DELETED("DELETED", "已删除");

    /** 持久化编码。 */
    private final String code;

    /** 中文名称。 */
    private final String name;
}

package com.fly.common.elasticsearch.bulk.model;

/** 待批量写入文档；documentId 必须是稳定业务主键。 */
public record ElasticsearchBulkDocument<T>(String documentId, T document) {
}

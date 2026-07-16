package com.fly.common.elasticsearch.bulk.model;

/**
 * 待批量写入文档；documentId 必须是稳定业务主键。
 *
 * @param documentId ES 文档 ID，由稳定业务主键转换而来
 * @param document 待写入的业务 Document，不允许直接使用数据库 Entity
 */
public record ElasticsearchBulkDocument<T>(String documentId, T document) {
}

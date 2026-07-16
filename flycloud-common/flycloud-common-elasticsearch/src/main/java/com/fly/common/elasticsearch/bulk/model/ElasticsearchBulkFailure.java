package com.fly.common.elasticsearch.bulk.model;

/**
 * Bulk 单文档失败信息。
 *
 * @param documentId 写入失败的 ES 文档 ID
 * @param reason Elasticsearch 返回的失败原因
 */
public record ElasticsearchBulkFailure(String documentId, String reason) {
}

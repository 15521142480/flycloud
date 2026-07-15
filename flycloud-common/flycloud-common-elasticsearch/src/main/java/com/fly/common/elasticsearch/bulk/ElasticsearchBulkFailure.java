package com.fly.common.elasticsearch.bulk;

/** Bulk 单文档失败信息。 */
public record ElasticsearchBulkFailure(String documentId, String reason) {
}

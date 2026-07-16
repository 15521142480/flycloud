package com.fly.common.elasticsearch.bulk.model;

import java.time.Duration;
import java.util.List;

/**
 * Bulk 同步统计结果。
 *
 * @param successCount 成功写入的文档数
 * @param failedCount 写入失败的文档数
 * @param elapsed 本次 Bulk 操作总耗时
 * @param failures 单文档失败详情
 */
public record ElasticsearchBulkResult(long successCount, long failedCount, Duration elapsed,
                                      List<ElasticsearchBulkFailure> failures) {
}

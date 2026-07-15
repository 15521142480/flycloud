package com.fly.common.elasticsearch.bulk.model;

import java.time.Duration;
import java.util.List;

/**
 * Bulk 同步统计结果。
 */
public record ElasticsearchBulkResult(long successCount, long failedCount, Duration elapsed,
                                      List<ElasticsearchBulkFailure> failures) {
}

package com.fly.common.elasticsearch.bulk.model;

import com.fly.common.elasticsearch.exception.ElasticsearchSyncException;

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

    /**
     * 校验 Bulk 写入结果，任意文档写入失败时终止后续同步或索引切换。
     *
     * <p>该规则属于所有业务索引共用的基础设施约束：Bulk 请求 HTTP 成功不代表其中每个
     * 文档都成功写入，调用方不得将存在 item 级失败的结果视为成功。</p>
     *
     * @param target 目标真实索引名称
     */
    public void assertNoFailures(String target) {
        if (failedCount <= 0) {
            return;
        }
        String firstReason = failures == null || failures.isEmpty() ? "未返回失败详情" : failures.getFirst().reason();
        throw new ElasticsearchSyncException(target, new IllegalStateException(
                "Bulk 写入存在失败文档，failedCount=" + failedCount + "，firstReason=" + firstReason));
    }
}

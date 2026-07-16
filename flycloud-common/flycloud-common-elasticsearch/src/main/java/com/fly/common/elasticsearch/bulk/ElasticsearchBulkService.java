package com.fly.common.elasticsearch.bulk;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.fly.common.elasticsearch.bulk.model.ElasticsearchBulkDocument;
import com.fly.common.elasticsearch.bulk.model.ElasticsearchBulkFailure;
import com.fly.common.elasticsearch.bulk.model.ElasticsearchBulkResult;
import com.fly.common.elasticsearch.config.properties.ElasticsearchProperties;
import com.fly.common.elasticsearch.exception.ElasticsearchSyncException;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用 Bulk 写入、失败聚合、重试与刷新能力。
 */
@RequiredArgsConstructor
public class ElasticsearchBulkService {

    private final ElasticsearchClient client;

    private final ElasticsearchProperties properties;

    /**
     * 按配置批次执行文档索引写入，聚合全部 item 失败信息并在完成后按配置刷新索引。
     *
     * @param index 目标真实索引
     * @param documents 待写入的稳定业务主键文档
     * @return 成功、失败、耗时和失败详情
     */
    public <T> ElasticsearchBulkResult index(String index, List<ElasticsearchBulkDocument<T>> documents) {
        Instant started = Instant.now();
        List<ElasticsearchBulkFailure> failures = new ArrayList<>();
        long success = 0;
        if (documents.isEmpty()) {
            return new ElasticsearchBulkResult(0, 0, Duration.ZERO, List.of());
        }
        for (int start = 0; start < documents.size(); start += properties.getBulkSize()) {
            List<ElasticsearchBulkDocument<T>> batch = documents.subList(
                    start, Math.min(documents.size(), start + properties.getBulkSize()));
            BulkResponse response = execute(index, batch);
            for (int i = 0; i < response.items().size(); i++) {
                var item = response.items().get(i);
                if (item.error() == null) {
                    success++;
                } else {
                    failures.add(new ElasticsearchBulkFailure(batch.get(i).documentId(), item.error().reason()));
                }
            }
        }
        if (properties.isRefreshAfterBulk()) refresh(index);
        return new ElasticsearchBulkResult(success, failures.size(), Duration.between(started, Instant.now()), List.copyOf(failures));
    }

    /**
     * 使用 Bulk API 提交单个批次，并对网络级异常执行有限重试。
     */
     private <T> BulkResponse execute(String index, List<ElasticsearchBulkDocument<T>> documents) {
        Exception last = null;
        for (int attempt = 0; attempt <= properties.getBulkRetryTimes(); attempt++) {
            try {
                List<BulkOperation> operations = documents.stream().map(document -> new BulkOperation.Builder()
                        .index(operation -> operation.index(index).id(document.documentId()).document(document.document())).build()).toList();
                return client.bulk(request -> request.operations(operations));
            } catch (Exception exception) {
                last = exception;
                if (attempt < properties.getBulkRetryTimes()) {
                    sleep();
                }
            }
        }
        throw new ElasticsearchSyncException(index, last);
    }

    /**
     * 在批量导入完成后刷新目标索引。
     */
     private void refresh(String index) {
        try {
            client.indices().refresh(request -> request.index(index));
        } catch (Exception exception) {
            throw new ElasticsearchSyncException(index, exception);
        }
    }

    /**
     * 按配置等待下一次 Bulk 重试。
     */
     private void sleep() {
        try {
            Thread.sleep(properties.getBulkRetryInterval().toMillis());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ElasticsearchSyncException("bulk-retry", exception);
        }
    }
}

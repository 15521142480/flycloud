package com.fly.common.elasticsearch.exception;

/**
 * ES 同步或批量写入异常。
 */
public class ElasticsearchSyncException extends ElasticsearchOperationException {

    /**
     * 创建数据同步异常。
     */
     public ElasticsearchSyncException(String target, Throwable cause) {
        super("sync", target, cause);
    }
}

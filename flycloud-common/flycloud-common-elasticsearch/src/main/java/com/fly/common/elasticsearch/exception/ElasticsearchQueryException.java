package com.fly.common.elasticsearch.exception;

/**
 * ES 查询异常。
 */
public class ElasticsearchQueryException extends ElasticsearchOperationException {

    /**
     * 创建查询异常。
     */
     public ElasticsearchQueryException(String target, Throwable cause) {
        super("query", target, cause);
    }
}

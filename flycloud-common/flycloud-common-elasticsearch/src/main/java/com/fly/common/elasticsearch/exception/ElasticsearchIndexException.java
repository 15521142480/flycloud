package com.fly.common.elasticsearch.exception;

/** ES 索引或别名治理异常。 */
public class ElasticsearchIndexException extends ElasticsearchOperationException {

    /** 创建索引或别名操作异常。 */
    public ElasticsearchIndexException(String operation, String target, Throwable cause) {
        super(operation, target, cause);
    }

    /** 创建带业务说明的索引治理异常。 */
    public ElasticsearchIndexException(String message) {
        super("index", message, new IllegalStateException(message));
    }
}

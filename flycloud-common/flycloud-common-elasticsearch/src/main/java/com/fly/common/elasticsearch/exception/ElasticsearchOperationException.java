package com.fly.common.elasticsearch.exception;

/** ES 基础设施操作异常，统一携带操作上下文与根因。 */
public class ElasticsearchOperationException extends RuntimeException {

    /**
     * 创建带操作与目标索引上下文的异常。
     *
     * @param operation ES 操作名称
     * @param target 操作目标（索引、别名或文档）
     * @param cause 原始异常
     */
    public ElasticsearchOperationException(String operation, String target, Throwable cause) {
        super("Elasticsearch " + operation + " 失败，target=" + target + "，原因：" + cause.getMessage(), cause);
    }
}

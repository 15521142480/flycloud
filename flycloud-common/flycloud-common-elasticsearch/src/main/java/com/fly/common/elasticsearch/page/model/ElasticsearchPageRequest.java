package com.fly.common.elasticsearch.page.model;

import co.elastic.clients.elasticsearch._types.FieldValue;

import java.util.List;
import java.util.Map;

/**
 * 强类型 ES 分页请求。
 *
 * <p>{@code searchAfter} 非空时采用深分页游标；排序字段只能来自 {@code allowedSortFields} 白名单。</p>
 *
 * @param pageNum 当前页码，从 1 开始
 * @param pageSize 每页记录数
 * @param sortField 前端请求的排序字段
 * @param sortOrder 排序方向：ASC 或 DESC
 * @param allowedSortFields 允许使用的排序字段白名单
 * @param searchAfter 深分页游标；浅分页时为 {@code null}
 */
public record ElasticsearchPageRequest(int pageNum, int pageSize, String sortField, String sortOrder,
                                       Map<String, String> allowedSortFields, List<FieldValue> searchAfter) {
}

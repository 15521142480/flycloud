package com.fly.common.elasticsearch.page;

import co.elastic.clients.elasticsearch._types.FieldValue;

import java.util.List;
import java.util.Map;

/**
 * 强类型 ES 分页请求。
 *
 * <p>{@code searchAfter} 非空时采用深分页游标；排序字段只能来自 {@code allowedSortFields} 白名单。</p>
 */
public record ElasticsearchPageRequest(int pageNum, int pageSize, String sortField, String sortOrder,
                                       Map<String, String> allowedSortFields, List<FieldValue> searchAfter) {
}

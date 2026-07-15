package com.fly.common.elasticsearch.page;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.elasticsearch.config.properties.ElasticsearchProperties;
import com.fly.common.elasticsearch.exception.ElasticsearchQueryException;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.function.Function;

/** 安全浅分页与 search_after 深分页统一执行器。 */
@RequiredArgsConstructor
public class ElasticsearchPageExecutor {

    private final ElasticsearchClient client;
    private final ElasticsearchProperties properties;

    /**
     * 以排序白名单执行浅分页或 search_after 深分页，并转换为项目统一 PageVo。
     *
     * @param alias 业务索引别名
     * @param request 强类型分页和排序请求
     * @param query 已由业务查询构造器生成的 ES Query
     * @param documentType 文档类型
     * @param converter 文档到业务 VO 的显式转换器
     */
    public <D, V> PageVo<V> search(String alias, ElasticsearchPageRequest request, Query query,
                                    Class<D> documentType, Function<D, V> converter) {
        int pageNum = Math.max(1, request.pageNum());
        int pageSize = Math.min(properties.getMaxPageSize(), Math.max(1, request.pageSize() <= 0 ? properties.getDefaultPageSize() : request.pageSize()));
        String field = request.allowedSortFields().getOrDefault(request.sortField(), request.allowedSortFields().get("createTime"));
        if (field == null) {
            throw new IllegalArgumentException("未配置默认 ES 排序字段");
        }
        long from = (long) (pageNum - 1) * pageSize;
        if (request.searchAfter() == null && from >= properties.getSearchAfterThreshold()) {
            throw new IllegalArgumentException("超过 ES 深分页阈值，必须使用 search_after");
        }
        try {
            SearchResponse<D> response = client.search(search -> {
                search.index(alias).size(pageSize).query(query)
                        .sort(sort -> sort.field(value -> value.field(field).order("ASC".equalsIgnoreCase(request.sortOrder()) ? SortOrder.Asc : SortOrder.Desc)));
                if (request.searchAfter() == null) {
                    search.from((int) from);
                } else {
                    search.searchAfter(request.searchAfter());
                }
                return search;
            }, documentType);
            PageVo<V> result = new PageVo<>();
            result.setList(response.hits().hits().stream().map(Hit::source).filter(Objects::nonNull).map(converter).toList());
            long total = response.hits().total() == null ? 0L : response.hits().total().value();
            result.setTotal(total);
            result.setPages((long) Math.ceil((double) total / pageSize));
            return result;
        } catch (Exception exception) {
            throw new ElasticsearchQueryException(alias, exception);
        }
    }
}

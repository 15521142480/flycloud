package com.fly.common.elasticsearch.index;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fly.common.elasticsearch.exception.ElasticsearchIndexException;
import lombok.RequiredArgsConstructor;

import java.io.Reader;

/** 通用版本索引创建、存在校验与安全删除能力。 */
@RequiredArgsConstructor
public class ElasticsearchIndexService {

    private final ElasticsearchClient client;

    /** 判断真实索引是否存在。 */
    public boolean exists(String index) {
        try {
            return client.indices().exists(request -> request.index(index)).value();
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("exists", index, exception);
        }
    }

    /** 使用业务提供的 JSON settings + mappings 创建真实索引。 */
    public void create(String index, Reader mapping) {
        try (mapping) {
            client.indices().create(request -> request.index(index).withJson(mapping));
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("create", index, exception);
        }
    }

    /** 刷新索引，使刚完成的 Bulk 数据可见。 */
    public void refresh(String index) {
        try {
            client.indices().refresh(request -> request.index(index));
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("refresh", index, exception);
        }
    }

    /** 删除前先确认没有 Alias 指向该索引。 */
    public void delete(String index, ElasticsearchAliasService aliasService) {
        if (aliasService.isAliasPointingTo(index)) {
            throw new ElasticsearchIndexException("删除前 Alias 仍指向索引：" + index);
        }
        try {
            client.indices().delete(request -> request.index(index));
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("delete", index, exception);
        }
    }
}

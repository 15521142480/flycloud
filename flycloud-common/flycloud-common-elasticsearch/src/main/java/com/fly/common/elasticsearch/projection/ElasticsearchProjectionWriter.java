package com.fly.common.elasticsearch.projection;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fly.common.elasticsearch.exception.ElasticsearchSyncException;
import com.fly.common.elasticsearch.index.ElasticsearchAliasService;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * ES 投影写入器。正常状态写 Alias 当前写索引；索引升级窗口额外写 rebuilding 索引。
 * Index API 以稳定业务主键为 document ID，因此重放天然幂等。
 */
@RequiredArgsConstructor
public class ElasticsearchProjectionWriter {

    private final ElasticsearchClient client;
    private final ElasticsearchAliasService aliasService;

    /**
     * 按稳定业务主键写入投影文档。
     *
     * <p>索引升级窗口内会同时写入 Alias 当前索引和新建索引，确保切换前后的增量数据一致。</p>
     */
    public <T> void upsert(String alias, String documentId, T document, String rebuildingIndex) {
        targets(alias, rebuildingIndex).forEach(index -> {
            try {
                client.index(request -> request.index(index).id(documentId).document(document));
            } catch (Exception exception) {
                throw new ElasticsearchSyncException(index + "/" + documentId, exception);
            }
        });
    }

    /**
     * 按稳定业务主键删除投影文档。
     *
     * <p>升级窗口内同步删除新旧两个写入目标中的文档。</p>
     */
    public void delete(String alias, String documentId, String rebuildingIndex) {
        targets(alias, rebuildingIndex).forEach(index -> {
            try {
                client.delete(request -> request.index(index).id(documentId));
            } catch (Exception exception) {
                throw new ElasticsearchSyncException(index + "/" + documentId, exception);
            }
        });
    }

    /**
     * 计算本次写入目标，去重后返回真实索引集合。
     */
     private Set<String> targets(String alias, String rebuildingIndex) {
        Set<String> indexes = new LinkedHashSet<>();
        String currentIndex = aliasService.getCurrentIndex(alias);
        if (currentIndex != null) {
            indexes.add(currentIndex);
        }
        if (rebuildingIndex != null) {
            indexes.add(rebuildingIndex);
        }
        if (indexes.isEmpty()) {
            throw new ElasticsearchSyncException(alias, new IllegalStateException("Alias 未指向任何索引"));
        }
        return indexes;
    }
}

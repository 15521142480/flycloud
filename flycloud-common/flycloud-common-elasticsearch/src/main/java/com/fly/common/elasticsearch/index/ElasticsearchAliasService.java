package com.fly.common.elasticsearch.index;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.GetAliasResponse;
import com.fly.common.elasticsearch.config.properties.ElasticsearchProperties;
import com.fly.common.elasticsearch.exception.ElasticsearchIndexException;
import lombok.RequiredArgsConstructor;

import co.elastic.clients.elasticsearch._types.ExpandWildcard;

import java.util.List;
import java.util.Map;

/**
 * Alias 查询、单写索引校验、原子切换与回滚能力。
 */
@RequiredArgsConstructor
public class ElasticsearchAliasService {

    private final ElasticsearchClient client;
    private final ElasticsearchProperties properties;

    /**
     * 获取 Alias 当前唯一指向的真实索引；Alias 不存在时返回 null。
     */
     public String getCurrentIndex(String alias) {
        Map<String, ?> aliases = aliases(alias);
        if (aliases.isEmpty()) {
            return null;
        }
        if (properties.isAliasRequireSingleIndex() && aliases.size() != 1) {
            throw new ElasticsearchIndexException("Alias 必须只指向一个写索引：" + alias);
        }
        return aliases.keySet().iterator().next();
    }

    /**
     * 获取指定业务别名的全部版本索引，供观察期清理与审计使用。
     */
     public List<String> getVersionIndexes(String alias) {
        try {
            return client.indices().get(request -> request.index(alias + "_v*").expandWildcards(ExpandWildcard.Open))
                    .result()
                    .keySet()
                    .stream()
                    .filter(ElasticsearchIndexName::isVersionedIndex)
                    .sorted()
                    .toList();
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("list", alias, exception);
        }
    }

    /**
     * 一次 updateAliases 原子请求内删除旧绑定并新增写索引绑定。
     */
     public void switchAlias(String alias, String oldIndex, String newIndex) {
        try {
            client.indices().updateAliases(request -> {
                if (oldIndex != null) request.actions(action -> action.remove(remove -> remove.index(oldIndex).alias(alias)));
                return request.actions(action -> action.add(add -> add.index(newIndex).alias(alias).isWriteIndex(true)));
            });
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("switch-alias", alias, exception);
        }
    }

    /**
     * 将 Alias 原子回滚至指定旧索引。
     */
     public void rollback(String alias, String currentIndex, String rollbackIndex) {
        switchAlias(alias, currentIndex, rollbackIndex);
    }

    /**
     * 判断任意 Alias 是否仍指向指定真实索引，删除索引前必须校验。
     */
     public boolean isAliasPointingTo(String index) {
        try {
            return client.indices().getAlias(request -> request.index(index).ignoreUnavailable(true)).result().values().stream()
                    .anyMatch(alias -> !alias.aliases().isEmpty());
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("check-alias", index, exception);
        }
    }

    /**
     * 查询 Alias 到真实索引的原始映射。
     */
     private Map<String, ?> aliases(String alias) {
        try {
            GetAliasResponse response = client.indices().getAlias(request -> request.name(alias).ignoreUnavailable(true));
            return response.result();
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("get-alias", alias, exception);
        }
    }
}

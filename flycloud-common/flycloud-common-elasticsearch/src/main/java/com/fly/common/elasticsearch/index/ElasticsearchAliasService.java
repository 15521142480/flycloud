package com.fly.common.elasticsearch.index;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.indices.GetAliasResponse;
import com.fly.common.elasticsearch.config.properties.ElasticsearchProperties;
import com.fly.common.elasticsearch.exception.ElasticsearchIndexException;
import com.fly.common.elasticsearch.index.model.ElasticsearchAliasIndexGroup;
import lombok.RequiredArgsConstructor;

import co.elastic.clients.elasticsearch._types.ExpandWildcard;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
                    .sorted(ElasticsearchIndexName.versionDescendingComparator())
                    .toList();
        } catch (ElasticsearchException exception) {
            if (isNotFound(exception)) {
                return List.of();
            }
            throw new ElasticsearchIndexException("list", alias, exception);
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("list", alias, exception);
        }
    }

    /**
     * 查询集群内全部业务 Alias 及其版本索引。
     *
     * <p>只返回符合 {@code {alias}_v{version}} 命名规范的真实索引，避免将 Elasticsearch 内部索引
     * 或不受版本治理的索引暴露给历史版本清理界面。</p>
     *
     * @return 按 Alias 字母顺序排列的索引分组
     */
    public List<ElasticsearchAliasIndexGroup> listAliasIndexGroups() {
        try {
            GetAliasResponse response = client.indices().getAlias(request -> request);
            Set<String> aliases = new TreeSet<>();
            response.result().values().forEach(indexAliases -> aliases.addAll(indexAliases.aliases().keySet()));
            return aliases.stream()
                    .map(alias -> new ElasticsearchAliasIndexGroup(alias, getCurrentIndex(alias), getVersionIndexes(alias)))
                    .filter(group -> !group.versionIndexes().isEmpty())
                    .toList();
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("list-aliases", "*", exception);
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
        } catch (ElasticsearchException exception) {
            // ES 对不存在的 Alias 仍会返回 404；首次创建版本索引时这是正常状态。
            if (isNotFound(exception)) {
                return Map.of();
            }
            throw new ElasticsearchIndexException("get-alias", alias, exception);
        } catch (Exception exception) {
            throw new ElasticsearchIndexException("get-alias", alias, exception);
        }
    }

    /**
     * 判断 ES 异常是否表示目标资源尚未创建。
     *
     * @param exception Elasticsearch 客户端异常
     * @return {@code true} 表示 HTTP 404
     */
    private boolean isNotFound(ElasticsearchException exception) {
        return exception.status() == 404;
    }
}

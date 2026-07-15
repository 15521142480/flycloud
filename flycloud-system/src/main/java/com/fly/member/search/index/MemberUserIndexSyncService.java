package com.fly.member.search.index;

import com.fly.common.elasticsearch.bulk.ElasticsearchBulkDocument;
import com.fly.common.elasticsearch.bulk.ElasticsearchBulkResult;
import com.fly.common.elasticsearch.bulk.ElasticsearchBulkService;
import com.fly.common.elasticsearch.index.ElasticsearchAliasService;
import com.fly.common.elasticsearch.index.ElasticsearchIndexName;
import com.fly.common.elasticsearch.index.ElasticsearchIndexService;
import com.fly.common.elasticsearch.index.ElasticsearchMappingService;
import com.fly.member.mapper.MemberUserMapper;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.member.search.converter.MemberUserDocumentConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.time.Duration;
import java.time.Instant;

/** 会员用户全量同步：基于主键稳定游标和 Bulk API。 */
@Service
@RequiredArgsConstructor
public class MemberUserIndexSyncService {

    private final MemberUserMapper mapper;
    private final MemberUserDocumentConverter converter;
    private final MemberUserIndexDefinition definition;
    private final ElasticsearchBulkService bulkService;
    private final ElasticsearchAliasService aliasService;
    private final ElasticsearchIndexService indexService;
    private final ElasticsearchMappingService mappingService;

    /**
     * 初始化会员真实版本索引（不存在时）并全量同步 MySQL 权威数据。
     *
     * @return 当前业务 Alias 指向的真实索引名称
     */
    public String initializeAndSynchronize() {
        String index = aliasService.getCurrentIndex(definition.alias());
        if (index == null) {
            index = ElasticsearchIndexName.buildVersionedIndex(definition.alias(), definition.initialVersion());
            indexService.create(index, mappingService.load(definition.mappingResource()));
            aliasService.switchAlias(definition.alias(), null, index);
        }
        synchronize(index);
        return index;
    }

    /**
     * 使用主键游标将会员权威数据分批写入指定真实索引。
     *
     * <p>不使用 offset 分页，避免大量数据下的深分页性能问题和记录漂移。</p>
     */
    public ElasticsearchBulkResult synchronize(String index) {
        long success = 0;
        long failed = 0;
        Long lastId = 0L;
        Instant started = Instant.now();
        List<com.fly.common.elasticsearch.bulk.ElasticsearchBulkFailure> failures = new ArrayList<>();
        while (true) {
            List<MemberUser> users = mapper.selectForEsSyncByCursor(lastId, definition.bulkSize());
            if (users.isEmpty()) {
                break;
            }
            ElasticsearchBulkResult result = bulkService.index(index, users.stream()
                    .map(user -> new ElasticsearchBulkDocument<>(String.valueOf(user.getId()), converter.toDocument(user))).toList());
            success += result.successCount();
            failed += result.failedCount();
            failures.addAll(result.failures());
            lastId = users.getLast().getId();
            if (users.size() < definition.bulkSize()) {
                break;
            }
        }
        return new ElasticsearchBulkResult(success, failed, Duration.between(started, Instant.now()), failures);
    }
}

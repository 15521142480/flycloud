package com.fly.member.search.index;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fly.common.elasticsearch.bulk.model.ElasticsearchBulkResult;
import com.fly.common.elasticsearch.index.ElasticsearchAliasService;
import com.fly.common.elasticsearch.index.ElasticsearchIndexName;
import com.fly.common.elasticsearch.index.ElasticsearchIndexService;
import com.fly.common.elasticsearch.index.ElasticsearchMappingService;
import com.fly.common.elasticsearch.index.model.ElasticsearchAliasIndexGroup;
import com.fly.common.enums.elasticsearch.ElasticsearchIndexOperationStatusEnum;
import com.fly.common.enums.elasticsearch.ElasticsearchIndexOperationTypeEnum;
import com.fly.common.enums.elasticsearch.ElasticsearchIndexRollbackStatusEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.member.mapper.MemberUserMapper;
import com.fly.system.api.member.domain.MemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 会员用户 Elasticsearch 索引操作编排服务。
 *
 * <p>统一编排首次初始化、全量同步、版本升级、Alias 回滚和历史版本删除；通用 Alias、Mapping、Bulk、
 * 索引删除等技术能力仍由 {@code flycloud-common-elasticsearch} 提供。</p>
 */
@Service
@RequiredArgsConstructor
public class MemberUserIndexOperationService {

    /** 索引升级期间需要消费者双写的操作执行状态。 */
    private static final Set<String> REBUILDING_STATUSES = Set.of(
            ElasticsearchIndexOperationStatusEnum.PENDING.getCode(),
            ElasticsearchIndexOperationStatusEnum.RUNNING.getCode());

    private final MemberUserIndexDefinition definition;

    private final ElasticsearchAliasService aliasService;

    private final ElasticsearchIndexService indexService;

    private final ElasticsearchMappingService mappingService;

    private final MemberUserIndexSyncService syncService;

    private final MemberUserIndexOperationRecordMapper recordMapper;

    private final MemberUserIndexOperationRecordService recordService;

    private final MemberUserMapper memberUserMapper;

    private final ElasticsearchClient client;

    /**
     * 首次创建版本索引并同步，或向当前 Alias 指向索引执行全量同步。
     *
     * @return 当前 Alias 指向的真实版本索引
     */
    public String initializeOrSynchronize() {
        String currentIndex = aliasService.getCurrentIndex(definition.alias());
        if (currentIndex == null) {
            return initialize();
        }
        return synchronizeCurrent(currentIndex);
    }

    /**
     * 创建下一版本索引、执行全量与补偿同步并原子切换 Alias。
     *
     * @return 切换后的真实版本索引名称
     */
    public String upgrade() {
        ensureNoRunningOperation();
        String oldIndex = aliasService.getCurrentIndex(definition.alias());
        if (oldIndex == null) {
            throw new ServiceException("请先完成会员用户初始索引同步");
        }

        String newIndex = ElasticsearchIndexName.nextVersion(oldIndex);
        MemberUserIndexOperationRecord record = recordService.create(
                ElasticsearchIndexOperationTypeEnum.UPGRADE, definition.alias(), oldIndex, newIndex, UserUtils.getCurOperator());
        try {
            indexService.create(newIndex, mappingService.load(definition.mappingResource()));
            recordService.markRunning(record);

            ElasticsearchBulkResult fullSyncResult = synchronizeRequired(newIndex);
            // 升级窗口内消费者会双写；再次读取 MySQL 权威数据，补齐 Alias 切换前的边界增量。
            ElasticsearchBulkResult compensationResult = synchronizeRequired(newIndex);
            long sourceCount = sourceCount();
            validate(newIndex, sourceCount);

            aliasService.switchAlias(definition.alias(), oldIndex, newIndex);
            recordService.complete(record, sourceCount,
                    fullSyncResult.successCount() + compensationResult.successCount(),
                    fullSyncResult.failedCount() + compensationResult.failedCount(),
                    "会员用户表的索引已经由" + oldIndex + "升级到" + newIndex
                            + "，请观察7天没问题后删除索引" + oldIndex,
                    "MEMBER_USER_INDEX_UPGRADED");
            return newIndex;
        } catch (RuntimeException exception) {
            recordService.markFailed(record, exception.getMessage());
            throw exception;
        }
    }

    /**
     * 在观察期内将 Alias 回滚到指定升级记录的源版本索引。
     *
     * @param recordId 已成功升级且仍可回滚的操作记录主键
     */
    public void rollback(Long recordId) {
        MemberUserIndexOperationRecord upgradeRecord = recordMapper.selectById(recordId);
        if (upgradeRecord == null
                || !ElasticsearchIndexOperationTypeEnum.UPGRADE.getCode().equals(upgradeRecord.getOperationType())
                || !ElasticsearchIndexOperationStatusEnum.SUCCEEDED.getCode().equals(upgradeRecord.getOperationStatus())
                || !ElasticsearchIndexRollbackStatusEnum.AVAILABLE.getCode().equals(upgradeRecord.getRollbackStatus())) {
            throw new ServiceException("仅已成功升级且旧版本仍保留的索引操作记录可以回滚");
        }
        if (!upgradeRecord.getTargetIndex().equals(aliasService.getCurrentIndex(upgradeRecord.getAlias()))) {
            throw new ServiceException("当前 Alias 已不再指向该升级版本，不能重复回滚");
        }

        MemberUserIndexOperationRecord rollbackRecord = recordService.create(
                ElasticsearchIndexOperationTypeEnum.ROLLBACK,
                upgradeRecord.getAlias(),
                upgradeRecord.getTargetIndex(),
                upgradeRecord.getSourceIndex(),
                UserUtils.getCurOperator());
        try {
            recordService.markRunning(rollbackRecord);
            aliasService.rollback(upgradeRecord.getAlias(), upgradeRecord.getTargetIndex(), upgradeRecord.getSourceIndex());
            recordService.completeRollback(rollbackRecord, upgradeRecord);
        } catch (RuntimeException exception) {
            recordService.markFailed(rollbackRecord, exception.getMessage());
            throw exception;
        }
    }

    /**
     * 查询 Elasticsearch 集群内全部业务 Alias 及其真实版本索引。
     *
     * @return Alias 与版本索引分组，版本号倒序
     */
    public List<ElasticsearchAliasIndexGroup> listAliasIndexGroups() {
        return aliasService.listAliasIndexGroups();
    }

    /**
     * 删除已脱离 Alias 的会员用户历史版本索引。
     *
     * <p>当前读写索引、升级重建中的目标索引以及不属于会员用户域的索引均禁止从本业务接口删除。</p>
     *
     * @param index 待删除真实版本索引
     */
    public void deleteHistoricalIndex(String index) {
        ElasticsearchIndexName.validateVersionedIndex(index);
        String alias = ElasticsearchIndexName.parseAlias(index);
        if (!definition.alias().equals(alias)) {
            throw new ServiceException("会员用户索引管理接口只能删除 " + definition.alias() + " 的历史版本");
        }
        ensureNoRunningOperation();
        if (index.equals(aliasService.getCurrentIndex(alias))) {
            throw new ServiceException("当前 Alias 正在使用该索引，禁止删除：" + index);
        }

        MemberUserIndexOperationRecord record = recordService.create(
                ElasticsearchIndexOperationTypeEnum.DELETE, alias, null, index, UserUtils.getCurOperator());
        try {
            recordService.markRunning(record);
            indexService.delete(index, aliasService);
            recordService.completeDelete(record, index);
        } catch (RuntimeException exception) {
            recordService.markFailed(record, exception.getMessage());
            throw exception;
        }
    }

    /**
     * 返回当前升级重建窗口的目标索引；不存在时返回 {@code null}。
     *
     * <p>会员 MQ 消费者使用该结果决定是否对升级中的新旧索引双写。</p>
     */
    public String rebuildingIndex() {
        MemberUserIndexOperationRecord record = recordMapper.selectOne(
                new LambdaQueryWrapper<MemberUserIndexOperationRecord>()
                        .eq(MemberUserIndexOperationRecord::getAlias, definition.alias())
                        .eq(MemberUserIndexOperationRecord::getOperationType, ElasticsearchIndexOperationTypeEnum.UPGRADE.getCode())
                        .in(MemberUserIndexOperationRecord::getOperationStatus, REBUILDING_STATUSES)
                        .orderByDesc(MemberUserIndexOperationRecord::getCreateTime)
                        .last("LIMIT 1"));
        return record == null ? null : record.getTargetIndex();
    }

    /**
     * 首次创建 v1 索引并全量同步权威数据。
     */
    private String initialize() {
        ensureNoRunningOperation();
        String initialIndex = ElasticsearchIndexName.buildVersionedIndex(definition.alias(), definition.initialVersion());
        MemberUserIndexOperationRecord record = recordService.create(
                ElasticsearchIndexOperationTypeEnum.INITIALIZE, definition.alias(), null, initialIndex, UserUtils.getCurOperator());
        try {
            // 支持上次初始化在绑定 Alias 前中断：已存在索引时复用，不重复创建。
            if (!indexService.exists(initialIndex)) {
                indexService.create(initialIndex, mappingService.load(definition.mappingResource()));
            }
            recordService.markRunning(record);
            ElasticsearchBulkResult result = synchronizeRequired(initialIndex);
            long sourceCount = sourceCount();
            validate(initialIndex, sourceCount);
            // 初始同步校验完成后才绑定 Alias，避免未完成的半成品索引被查询链路读取。
            aliasService.switchAlias(definition.alias(), null, initialIndex);
            recordService.complete(record, sourceCount, result.successCount(), result.failedCount(),
                    "会员用户索引" + initialIndex + "已完成初始化和全量同步，共同步" + sourceCount + "条数据",
                    "MEMBER_USER_INDEX_INITIALIZED");
            return initialIndex;
        } catch (RuntimeException exception) {
            recordService.markFailed(record, exception.getMessage());
            throw exception;
        }
    }

    /**
     * 向当前 Alias 指向的索引执行全量同步。
     */
    private String synchronizeCurrent(String currentIndex) {
        ensureNoRunningOperation();
        MemberUserIndexOperationRecord record = recordService.create(
                ElasticsearchIndexOperationTypeEnum.SYNCHRONIZE,
                definition.alias(), currentIndex, currentIndex, UserUtils.getCurOperator());
        try {
            recordService.markRunning(record);
            ElasticsearchBulkResult result = synchronizeRequired(currentIndex);
            long sourceCount = sourceCount();
            validate(currentIndex, sourceCount);
            recordService.complete(record, sourceCount, result.successCount(), result.failedCount(),
                    "会员用户索引" + currentIndex + "已完成全量同步，共同步" + sourceCount + "条数据",
                    "MEMBER_USER_INDEX_SYNCHRONIZED");
            return currentIndex;
        } catch (RuntimeException exception) {
            recordService.markFailed(record, exception.getMessage());
            throw exception;
        }
    }

    /**
     * 执行单轮同步并将文档级失败转换为业务异常。
     */
    private ElasticsearchBulkResult synchronizeRequired(String index) {
        ElasticsearchBulkResult result = syncService.synchronize(index);
        result.assertNoFailures(index);
        return result;
    }

    /**
     * 统计 MySQL 中有效会员数，作为索引文档数量验收基准。
     */
    private long sourceCount() {
        return memberUserMapper.selectCount(new LambdaQueryWrapper<MemberUser>()
                .eq(MemberUser::getIsDeleted, false));
    }

    /**
     * 校验新索引或同步索引的文档总量与集群健康状态。
     */
    private void validate(String index, long expectedCount) {
        try {
            long actualCount = client.count(request -> request.index(index)).count();
            if (actualCount != expectedCount) {
                throw new ServiceException("索引文档数量校验失败，expected=" + expectedCount + "，actual=" + actualCount);
            }
            String healthStatus = client.cluster().health(request -> request.index(index)).status().jsonValue();
            if ("red".equals(healthStatus)) {
                throw new ServiceException("索引健康检查失败：red");
            }
        } catch (ServiceException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ServiceException("索引校验失败：" + exception.getMessage());
        }
    }

    /**
     * 防止同一 Alias 并发执行初始化、同步、升级或删除，避免版本与 Alias 状态竞争。
     */
    private void ensureNoRunningOperation() {
        Long count = recordMapper.selectCount(new LambdaQueryWrapper<MemberUserIndexOperationRecord>()
                .eq(MemberUserIndexOperationRecord::getAlias, definition.alias())
                .in(MemberUserIndexOperationRecord::getOperationStatus,
                        ElasticsearchIndexOperationStatusEnum.PENDING.getCode(),
                        ElasticsearchIndexOperationStatusEnum.RUNNING.getCode()));
        if (count != null && count > 0) {
            throw new ServiceException("会员用户索引存在执行中的操作，请等待完成后重试");
        }
    }

}

package com.fly.test.member.upgrade;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fly.common.elasticsearch.bulk.ElasticsearchBulkResult;
import com.fly.common.elasticsearch.index.ElasticsearchAliasService;
import com.fly.common.elasticsearch.index.ElasticsearchIndexName;
import com.fly.common.elasticsearch.index.ElasticsearchIndexService;
import com.fly.common.elasticsearch.index.ElasticsearchMappingService;
import com.fly.common.exception.ServiceException;
import com.fly.common.rocketmq.constant.RocketMqTagConstants;
import com.fly.common.rocketmq.constant.RocketMqTopicConstants;
import com.fly.common.rocketmq.service.MqOutboxService;
import com.fly.member.mapper.MemberUserMapper;
import com.fly.system.api.member.domain.MemberUser;
import com.fly.system.mapper.SysUserMapper;
import com.fly.test.member.definition.MemberUserIndexDefinition;
import com.fly.test.member.domain.entity.MemberUserIndexUpgradeRecord;
import com.fly.test.member.domain.event.IndexUpgradeNotificationEvent;
import com.fly.test.member.mapper.MemberUserIndexUpgradeRecordMapper;
import com.fly.test.member.sync.MemberUserIndexSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 会员索引版本升级编排服务。
 *
 * <p>升级期间消费者依据 {@link #rebuildingIndex()} 同时写旧索引和新索引；完成全量同步后再补偿同步一次，
 * 随后校验并通过单个 Alias 原子操作切换读写入口。旧索引保留至人工观察期结束后再清理。</p>
 */
@Service
@RequiredArgsConstructor
public class MemberUserIndexUpgradeService {

    private static final Set<String> REBUILDING_STATUSES = Set.of("CREATED", "SYNCING", "VALIDATING");

    private final MemberUserIndexDefinition definition;
    private final ElasticsearchAliasService aliasService;
    private final ElasticsearchIndexService indexService;
    private final ElasticsearchMappingService mappingService;
    private final MemberUserIndexSyncService syncService;
    private final MemberUserIndexUpgradeRecordMapper recordMapper;
    private final MemberUserMapper memberUserMapper;
    private final SysUserMapper sysUserMapper;
    private final MqOutboxService outboxService;
    private final ElasticsearchClient client;

    /**
     * 创建下一个版本索引、执行全量与补偿同步，校验成功后原子切换 Alias。
     *
     * @return 切换后的真实索引名称
     */
    public String upgrade() {
        String oldIndex = aliasService.getCurrentIndex(definition.alias());
        if (oldIndex == null) {
            throw new ServiceException("请先完成会员用户初始索引同步");
        }

        String newIndex = ElasticsearchIndexName.nextVersion(oldIndex);
        MemberUserIndexUpgradeRecord record = createRecord(oldIndex, newIndex);
        try {
            indexService.create(newIndex, mappingService.load(definition.mappingResource()));
            updateStatus(record, "SYNCING", null);

            ElasticsearchBulkResult fullSyncResult = synchronizeRequired(newIndex, "全量同步");
            // 升级状态未结束前，MQ 消费链路会双写；再次基于权威 MySQL 补偿，覆盖切换前的边界增量。
            ElasticsearchBulkResult compensationResult = synchronizeRequired(newIndex, "补偿同步");
            long sourceCount = sourceCount();

            updateCounts(record, sourceCount, fullSyncResult, compensationResult);
            updateStatus(record, "VALIDATING", null);
            validate(newIndex, sourceCount);

            aliasService.switchAlias(definition.alias(), oldIndex, newIndex);
            updateStatus(record, "SWITCHED", null);
            publishNotification(oldIndex, newIndex);
            return newIndex;
        } catch (RuntimeException exception) {
            updateStatus(record, "FAILED", exception.getMessage());
            throw exception;
        }
    }

    /**
     * 在观察期内将 Alias 原子回切到升级前的真实索引。
     *
     * @param recordId 升级审计记录主键
     */
    public void rollback(Long recordId) {
        MemberUserIndexUpgradeRecord record = recordMapper.selectById(recordId);
        if (record == null || !"SWITCHED".equals(record.getStatus())) {
            throw new ServiceException("仅已切换的索引升级记录可以回滚");
        }

        aliasService.rollback(record.getAlias(), record.getNewIndex(), record.getOldIndex());
        record.setStatus("ROLLED_BACK");
        record.setRollbackTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.updateById(record);
    }

    /**
     * 返回正在重建的真实索引。
     *
     * <p>消费者据此决定是否双写；不存在升级记录时返回 {@code null}。</p>
     */
    public String rebuildingIndex() {
        MemberUserIndexUpgradeRecord record = recordMapper.selectOne(
                new LambdaQueryWrapper<MemberUserIndexUpgradeRecord>()
                        .eq(MemberUserIndexUpgradeRecord::getAlias, definition.alias())
                        .in(MemberUserIndexUpgradeRecord::getStatus, REBUILDING_STATUSES)
                        .orderByDesc(MemberUserIndexUpgradeRecord::getCreateTime)
                        .last("LIMIT 1"));
        return record == null ? null : record.getNewIndex();
    }

    /** 创建升级审计记录，使 MQ 消费者从建索引前即进入双写窗口。 */
    private MemberUserIndexUpgradeRecord createRecord(String oldIndex, String newIndex) {
        MemberUserIndexUpgradeRecord record = new MemberUserIndexUpgradeRecord();
        record.setAlias(definition.alias());
        record.setOldIndex(oldIndex);
        record.setNewIndex(newIndex);
        record.setStatus("CREATED");
        record.setStartTime(LocalDateTime.now());
        record.setOperator("system");
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.insert(record);
        return record;
    }

    /** 执行单轮同步并将任何文档级失败转换为可审计的业务异常。 */
    private ElasticsearchBulkResult synchronizeRequired(String index, String stage) {
        ElasticsearchBulkResult result = syncService.synchronize(index);
        if (result.failedCount() > 0) {
            throw new ServiceException(stage + "存在失败文档：" + result.failedCount());
        }
        return result;
    }

    /** 统计 MySQL 权威数据行数，作为索引验收基准。 */
    private long sourceCount() {
        return memberUserMapper.selectCount(new LambdaQueryWrapper<MemberUser>()
                .eq(MemberUser::getIsDeleted, false));
    }

    /** 保存本次升级的权威总量和两轮同步的处理统计。 */
    private void updateCounts(MemberUserIndexUpgradeRecord record, long sourceCount,
                              ElasticsearchBulkResult fullSyncResult, ElasticsearchBulkResult compensationResult) {
        record.setTotalCount(sourceCount);
        record.setSuccessCount(fullSyncResult.successCount() + compensationResult.successCount());
        record.setFailedCount(fullSyncResult.failedCount() + compensationResult.failedCount());
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.updateById(record);
    }

    /** 更新升级状态及异常摘要。 */
    private void updateStatus(MemberUserIndexUpgradeRecord record, String status, String errorMessage) {
        record.setStatus(status);
        record.setErrorMessage(errorMessage);
        if ("SWITCHED".equals(status) || "FAILED".equals(status)) {
            record.setFinishTime(LocalDateTime.now());
        }
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.updateById(record);
    }

    /** 校验新索引文档总量及集群健康状态。 */
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

    /** 将索引升级通知作为本地消息表记录持久化，由异步投递器发送。 */
    private void publishNotification(String oldIndex, String newIndex) {
        IndexUpgradeNotificationEvent event = new IndexUpgradeNotificationEvent();
        event.setUserIds(sysUserMapper.selectUserIdsByRoleCode("yunwei"));
        event.setContent("会员用户表的索引已经由" + oldIndex + "升级到" + newIndex
                + "，请观察7天没问题后删除索引" + oldIndex);
        outboxService.save(
                RocketMqTopicConstants.SYSTEM_USER_EVENT,
                RocketMqTagConstants.NOTIFY,
                newIndex,
                "MEMBER_USER_INDEX_UPGRADED",
                event);
    }
}

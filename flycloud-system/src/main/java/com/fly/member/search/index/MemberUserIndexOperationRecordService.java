package com.fly.member.search.index;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fly.common.enums.elasticsearch.ElasticsearchIndexOperationStatusEnum;
import com.fly.common.enums.elasticsearch.ElasticsearchIndexOperationTypeEnum;
import com.fly.common.enums.elasticsearch.ElasticsearchIndexRollbackStatusEnum;
import com.fly.common.enums.elasticsearch.ElasticsearchIndexVersionStatusEnum;
import com.fly.common.rocketmq.constant.RocketMqTagConstants;
import com.fly.common.rocketmq.constant.RocketMqTopicConstants;
import com.fly.common.rocketmq.outbox.service.MqOutboxService;
import com.fly.member.search.consumer.IndexOperationNotificationConsumer;
import com.fly.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 会员用户索引操作审计与可靠通知服务。
 *
 * <p>索引操作本身是 Elasticsearch 外部调用，不能纳入本地数据库事务；但“操作成功审计记录”与“成功通知本地消息”
 * 必须在同一事务内保存，保证审计一旦标记成功，通知必定可被异步投递。</p>
 */
@Service
@RequiredArgsConstructor
public class MemberUserIndexOperationRecordService {

    private final MemberUserIndexOperationRecordMapper recordMapper;

    private final SysUserMapper sysUserMapper;

    private final MqOutboxService outboxService;

    /**
     * 创建待执行的索引操作审计记录。
     *
     * @param operationType 操作类型
     * @param alias 业务索引别名
     * @param sourceIndex 操作前真实索引
     * @param targetIndex 操作目标真实索引
     * @param operator 操作人
     * @return 已持久化的操作记录
     */
    public MemberUserIndexOperationRecord create(ElasticsearchIndexOperationTypeEnum operationType, String alias,
                                                  String sourceIndex, String targetIndex, String operator) {
        LocalDateTime now = LocalDateTime.now();
        MemberUserIndexOperationRecord record = new MemberUserIndexOperationRecord();
        record.setOperationType(operationType.getCode());
        record.setOperationStatus(ElasticsearchIndexOperationStatusEnum.PENDING.getCode());
        record.setAlias(alias);
        record.setSourceIndex(sourceIndex);
        record.setTargetIndex(targetIndex);
        record.setTargetIndexStatus(ElasticsearchIndexVersionStatusEnum.AVAILABLE.getCode());
        record.setRollbackStatus(ElasticsearchIndexRollbackStatusEnum.NOT_APPLICABLE.getCode());
        record.setStartTime(now);
        record.setOperator(operator);
        record.setCreateTime(now);
        record.setUpdateTime(now);
        recordMapper.insert(record);
        return record;
    }

    /**
     * 将索引操作审计状态更新为执行中。
     *
     * @param record 待更新操作记录
     */
    public void markRunning(MemberUserIndexOperationRecord record) {
        updateOperationStatus(record, ElasticsearchIndexOperationStatusEnum.RUNNING, null);
    }

    /**
     * 在一个本地事务中写入成功审计结果和 MQ 通知本地消息。
     *
     * @param record 操作审计记录
     * @param totalCount MySQL 权威数据总量
     * @param successCount 成功写入文档数量
     * @param failedCount 失败写入文档数量
     * @param notificationContent 成功通知正文；为空时不发送通知
     * @param eventType 业务事件类型；不发送通知时可为空
     */
    @Transactional(rollbackFor = Exception.class)
    public void complete(MemberUserIndexOperationRecord record, Long totalCount, Long successCount, Long failedCount,
                         String notificationContent, String eventType) {
        LocalDateTime now = LocalDateTime.now();
        record.setOperationStatus(ElasticsearchIndexOperationStatusEnum.SUCCEEDED.getCode());
        record.setTotalCount(totalCount);
        record.setSuccessCount(successCount);
        record.setFailedCount(failedCount);
        record.setFinishTime(now);
        record.setUpdateTime(now);
        if (ElasticsearchIndexOperationTypeEnum.UPGRADE.getCode().equals(record.getOperationType())) {
            record.setRollbackStatus(ElasticsearchIndexRollbackStatusEnum.AVAILABLE.getCode());
        }
        recordMapper.updateById(record);

        if (notificationContent != null) {
            IndexOperationNotificationEvent event = new IndexOperationNotificationEvent();
            event.setUserIds(sysUserMapper.selectUserIdsByRoleCode(IndexOperationNotificationConsumer.NOTIFICATION_ROLE_CODE));
            event.setContent(notificationContent);
            outboxService.save(
                    RocketMqTopicConstants.SYSTEM_USER_EVENT,
                    RocketMqTagConstants.NOTIFY,
                    record.getTargetIndex(),
                    eventType,
                    event);
        }
    }

    /**
     * 标记索引操作失败，保留异常摘要供人工排查。
     *
     * @param record 失败的操作记录
     * @param errorMessage 异常摘要
     */
    public void markFailed(MemberUserIndexOperationRecord record, String errorMessage) {
        updateOperationStatus(record, ElasticsearchIndexOperationStatusEnum.FAILED, errorMessage);
    }

    /**
     * 在一个本地事务中完成回滚操作，并关闭原升级操作的回滚入口。
     *
     * @param rollbackRecord 本次回滚操作记录
     * @param upgradeRecord 被回滚的成功升级记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void completeRollback(MemberUserIndexOperationRecord rollbackRecord,
                                 MemberUserIndexOperationRecord upgradeRecord) {
        LocalDateTime now = LocalDateTime.now();
        rollbackRecord.setOperationStatus(ElasticsearchIndexOperationStatusEnum.SUCCEEDED.getCode());
        rollbackRecord.setFinishTime(now);
        rollbackRecord.setUpdateTime(now);
        recordMapper.updateById(rollbackRecord);

        upgradeRecord.setRollbackStatus(ElasticsearchIndexRollbackStatusEnum.ROLLED_BACK.getCode());
        upgradeRecord.setRollbackTime(now);
        upgradeRecord.setUpdateTime(now);
        recordMapper.updateById(upgradeRecord);
    }

    /**
     * 标记历史版本删除成功，并取消依赖该旧版本的升级回滚能力。
     *
     * @param deleteRecord 本次删除操作记录
     * @param deletedIndex 已删除真实版本索引
     */
    @Transactional(rollbackFor = Exception.class)
    public void completeDelete(MemberUserIndexOperationRecord deleteRecord, String deletedIndex) {
        LocalDateTime now = LocalDateTime.now();
        deleteRecord.setOperationStatus(ElasticsearchIndexOperationStatusEnum.SUCCEEDED.getCode());
        deleteRecord.setTargetIndexStatus(ElasticsearchIndexVersionStatusEnum.DELETED.getCode());
        deleteRecord.setFinishTime(now);
        deleteRecord.setUpdateTime(now);
        recordMapper.updateById(deleteRecord);

        recordMapper.update(null, new LambdaUpdateWrapper<MemberUserIndexOperationRecord>()
                .eq(MemberUserIndexOperationRecord::getTargetIndex, deletedIndex)
                .set(MemberUserIndexOperationRecord::getTargetIndexStatus,
                        ElasticsearchIndexVersionStatusEnum.DELETED.getCode())
                .set(MemberUserIndexOperationRecord::getUpdateTime, now));

        recordMapper.update(null, new LambdaUpdateWrapper<MemberUserIndexOperationRecord>()
                .eq(MemberUserIndexOperationRecord::getOperationType, ElasticsearchIndexOperationTypeEnum.UPGRADE.getCode())
                .eq(MemberUserIndexOperationRecord::getSourceIndex, deletedIndex)
                .eq(MemberUserIndexOperationRecord::getRollbackStatus,
                        ElasticsearchIndexRollbackStatusEnum.AVAILABLE.getCode())
                .set(MemberUserIndexOperationRecord::getRollbackStatus,
                        ElasticsearchIndexRollbackStatusEnum.UNAVAILABLE.getCode())
                .set(MemberUserIndexOperationRecord::getRollbackUnavailableReason, "升级前版本索引已删除，不能再回滚")
                .set(MemberUserIndexOperationRecord::getUpdateTime, now));
    }

    /**
     * 更新操作执行状态。
     */
    private void updateOperationStatus(MemberUserIndexOperationRecord record,
                                       ElasticsearchIndexOperationStatusEnum status, String errorMessage) {
        record.setOperationStatus(status.getCode());
        record.setErrorMessage(errorMessage);
        record.setFinishTime(status == ElasticsearchIndexOperationStatusEnum.FAILED ? LocalDateTime.now() : null);
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.updateById(record);
    }
}

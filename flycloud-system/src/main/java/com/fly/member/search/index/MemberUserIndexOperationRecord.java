package com.fly.member.search.index;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员用户 Elasticsearch 索引操作审计记录。
 *
 * <p>统一记录首次初始化、全量同步、版本升级、别名回滚和历史版本删除，
 * 不将某一种操作的专属状态复用于其他操作。</p>
 */
@Data
@TableName("member_user_index_operation_record")
public class MemberUserIndexOperationRecord {

    /** 索引操作审计记录主键。 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 操作类型，取值见 ElasticsearchIndexOperationTypeEnum。 */
    private String operationType;

    /** 操作执行状态，取值见 ElasticsearchIndexOperationStatusEnum。 */
    private String operationStatus;

    /** 稳定业务索引别名。 */
    private String alias;

    /** 操作前的真实版本索引；首次初始化时为空。 */
    private String sourceIndex;

    /** 本次操作的目标真实版本索引。 */
    private String targetIndex;

    /** 目标真实索引的生命周期状态，取值见 ElasticsearchIndexVersionStatusEnum。 */
    private String targetIndexStatus;

    /** 升级操作的回滚可用状态，取值见 ElasticsearchIndexRollbackStatusEnum。 */
    private String rollbackStatus;

    /** 回滚不可用原因，例如升级前版本索引已经删除。 */
    private String rollbackUnavailableReason;

    /** 同步时 MySQL 权威数据总量。 */
    private Long totalCount;

    /** 本次索引操作成功写入的文档数量。 */
    private Long successCount;

    /** 本次索引操作失败写入的文档数量。 */
    private Long failedCount;

    /** 索引操作开始时间。 */
    private LocalDateTime startTime;

    /** 索引操作完成或失败时间。 */
    private LocalDateTime finishTime;

    /** 发起操作的后台用户。 */
    private String operator;

    /** 失败原因摘要。 */
    private String errorMessage;

    /** Alias 实际回滚时间。 */
    private LocalDateTime rollbackTime;

    /** 审计记录创建时间。 */
    private LocalDateTime createTime;

    /** 审计记录最后更新时间。 */
    private LocalDateTime updateTime;
}

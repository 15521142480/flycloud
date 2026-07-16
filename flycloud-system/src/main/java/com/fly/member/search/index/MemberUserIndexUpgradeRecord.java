package com.fly.member.search.index;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会员索引升级审计记录。
 */
@Data
@TableName("member_user_index_upgrade_record")
public class MemberUserIndexUpgradeRecord {

    /**
     * 索引升级审计记录主键。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 稳定业务索引别名。
     */
    private String alias;

    /**
     * 升级前真实版本索引。
     */
    private String oldIndex;

    /**
     * 升级后真实版本索引。
     */
    private String newIndex;

    /**
     * 升级状态：CREATED、SYNCING、VALIDATING、SWITCHED、FAILED 或 ROLLED_BACK。
     */
    private String status;

    /**
     * 升级时 MySQL 权威数据总量。
     */
    private Long totalCount;

    /**
     * 全量同步与补偿同步累计成功数量。
     */
    private Long successCount;

    /**
     * 全量同步与补偿同步累计失败数量。
     */
    private Long failedCount;

    /**
     * 索引升级开始时间。
     */
    private LocalDateTime startTime;

    /**
     * 索引升级完成或失败时间。
     */
    private LocalDateTime finishTime;

    /**
     * 发起升级的操作人。
     */
    private String operator;

    /**
     * 失败原因摘要。
     */
    private String errorMessage;

    /**
     * 索引别名回滚时间。
     */
    private LocalDateTime rollbackTime;

    /**
     * 审计记录创建时间。
     */
    private LocalDateTime createTime;

    /**
     * 审计记录最后更新时间。
     */
    private LocalDateTime updateTime;
}

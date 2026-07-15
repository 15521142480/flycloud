package com.fly.member.search.index;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/** 会员索引升级审计记录。 */
@Data
@TableName("member_user_index_upgrade_record")
public class MemberUserIndexUpgradeRecord {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String alias;
    private String oldIndex;
    private String newIndex;
    private String status;
    private Long totalCount;
    private Long successCount;
    private Long failedCount;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private String operator;
    private String errorMessage;
    private LocalDateTime rollbackTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

package com.fly.common.rocketmq.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/** 本地消息表记录。消息与业务数据在同一数据库事务内落库，由异步调度器投递。 */
@Data
@TableName("mq_outbox_message")
public class MqOutboxMessage {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String messageId;
    private String topic;
    private String tag;
    private String bizKey;
    private String payload;
    private Integer status;
    private Integer retryCount;
    private LocalDateTime nextRetryTime;
    private String dispatchToken;
    private String lastError;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

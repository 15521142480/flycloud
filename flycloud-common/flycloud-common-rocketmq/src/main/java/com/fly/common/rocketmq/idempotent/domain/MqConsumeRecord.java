package com.fly.common.rocketmq.idempotent.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * MQ 消费成功幂等记录。唯一键为消费者组和业务消息 ID。
 */
@Data
@TableName("mq_consume_record")
public class MqConsumeRecord {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String consumerGroup;
    private String messageId;
    private String topic;
    private String tag;
    private String bizKey;
    private LocalDateTime consumeTime;
    private String createBy;
    private LocalDateTime createTime;
    private Boolean isDeleted;
}

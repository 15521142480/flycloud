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

    /**
     * 消费记录主键。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消费者组；同一业务消息可被不同消费者组各消费一次。
     */
    private String consumerGroup;

    /**
     * 业务消息唯一标识，与消费者组共同组成幂等唯一键。
     */
    private String messageId;

    /**
     * 已消费消息所属 Topic。
     */
    private String topic;

    /**
     * 已消费消息所属 Tag。
     */
    private String tag;

    /**
     * 业务唯一标识，便于按业务对象追踪消费记录。
     */
    private String bizKey;

    /**
     * 消费业务成功完成的时间。
     */
    private LocalDateTime consumeTime;

    /**
     * 创建人；系统自动消费时为空字符串。
     */
    private String createBy;

    /**
     * 幂等记录创建时间。
     */
    private LocalDateTime createTime;

    /**
     * 逻辑删除标识。
     */
    private Boolean isDeleted;
}

package com.fly.common.rocketmq.outbox.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 本地消息表记录。消息与业务数据在同一数据库事务内落库，由异步调度器投递。
 */
@Data
@TableName("mq_outbox_message")
public class MqOutboxMessage {

    /**
     * 本地消息表记录主键；仅用于调度抢占和投递状态管理。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 业务消息唯一标识；投递重试时保持不变，并作为消费者幂等键的一部分。
     */
    private String messageId;

    /**
     * RocketMQ 主题。
     */
    private String topic;

    /**
     * RocketMQ 事件标签。
     */
    private String tag;

    /**
     * 业务唯一标识，例如会员用户编号或订单编号。
     */
    private String bizKey;

    /**
     * 标准消息信封 JSON；禁止直接保存业务实体。
     */
    private String payload;

    /**
     * 投递状态：0 待投递、1 已投递、2 最终失败、3 投递中。
     */
    private Integer status;

    /**
     * 已执行的投递重试次数。
     */
    private Integer retryCount;

    /**
     * 下一次允许投递或租约到期的时间。
     */
    private LocalDateTime nextRetryTime;

    /**
     * 调度实例抢占令牌，用于防止过期实例覆盖最新投递状态。
     */
    private String dispatchToken;

    /**
     * 最近一次投递失败原因。
     */
    private String lastError;

    /**
     * 本地消息创建时间。
     */
    private LocalDateTime createTime;

    /**
     * 本地消息最后更新时间。
     */
    private LocalDateTime updateTime;
}

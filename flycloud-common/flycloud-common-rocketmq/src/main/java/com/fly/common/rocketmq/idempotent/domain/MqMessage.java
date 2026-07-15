package com.fly.common.rocketmq.idempotent.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 所有业务 MQ 消息的统一信封。
 *
 * <p>消息只传递稳定的业务数据，不允许直接传递数据库 Entity。</p>
 */
@Data
public class MqMessage<T> implements Serializable {

    /**
     * 业务消息唯一标识。
     */
    private String messageId;

    /**
     * 业务事件发生时间。
     */
    private LocalDateTime occurredTime;

    /**
     * 业务主键。
     */
    private String bizKey;

    /**
     * RocketMQ 事件 Tag。
     */
    private String tag;

    /**
     * 业务事件类型。
     */
    private String eventType;

    /**
     * 强类型业务载荷。
     */
    private T payload;
}

package com.fly.common.rocketmq.domain;

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

    private String messageId;
    private LocalDateTime occurredTime;
    private String bizKey;
    private String eventType;
    private T payload;
}

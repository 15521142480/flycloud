package com.fly.common.enums.rocketmq;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RocketMQ 本地消息表投递状态。
 *
 * <p>状态描述的是生产者是否已获得 Broker 确认，不代表消费者是否已成功消费；消费结果由
 * {@code mq_consume_record} 单独记录。</p>
 */
@Getter
@AllArgsConstructor
public enum RocketMqOutboxStatusEnum {

    /** 已持久化，等待调度器投递。 */
    PENDING(0, "待投递"),

    /** Broker 已确认接收消息。 */
    SENT(1, "已投递"),

    /** 已达到最大重试次数，等待人工处理。 */
    FAILED(2, "最终失败"),

    /** 已被调度实例以租约方式抢占，正在投递。 */
    SENDING(3, "投递中");

    /** 持久化编码。 */
    private final Integer code;

    /** 中文名称。 */
    private final String name;
}

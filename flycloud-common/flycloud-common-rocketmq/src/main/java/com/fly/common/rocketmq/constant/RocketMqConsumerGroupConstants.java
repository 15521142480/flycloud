package com.fly.common.rocketmq.constant;

/**
 * 消费者组常量。一个消费者组对同一业务消息只成功处理一次。
 */
public final class RocketMqConsumerGroupConstants {

    public static final String SYSTEM_MEMBER_USER_ES_INDEX = "flycloud-system-member-user-es-index-consumer";
    public static final String SYSTEM_OPERATION_NOTIFY = "flycloud-system-operation-notify-consumer";

    private RocketMqConsumerGroupConstants() {
    }
}

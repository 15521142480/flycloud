package com.fly.common.rocketmq.constant;

/**
 * 消费者组常量。一个消费者组对同一业务消息只成功处理一次。
 */
public final class RocketMqConsumerGroupConstants {

    /**
     * 系统会员用户 ES 索引投影消费者组。
     */
    public static final String SYSTEM_MEMBER_USER_ES_INDEX = "flycloud-system-member-user-es-index-consumer";

    /**
     * 系统运维通知消费者组。
     */
    public static final String SYSTEM_OPERATION_NOTIFY = "flycloud-system-operation-notify-consumer";

    /**
     * 常量类禁止实例化。
     */
    private RocketMqConsumerGroupConstants() {
    }
}

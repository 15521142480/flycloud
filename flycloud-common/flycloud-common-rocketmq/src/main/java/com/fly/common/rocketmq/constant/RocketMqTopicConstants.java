package com.fly.common.rocketmq.constant;

/**
 * 按业务域划分的 RocketMQ Topic 常量。
 */
public final class RocketMqTopicConstants {

    public static final String SYSTEM_USER_EVENT = "flycloud-system-user-event";
    public static final String MALL_PRODUCT_EVENT = "flycloud-mall-product-event";
    public static final String BPM_MODEL_EVENT = "flycloud-bpm-model-event";

    private RocketMqTopicConstants() {
    }
}

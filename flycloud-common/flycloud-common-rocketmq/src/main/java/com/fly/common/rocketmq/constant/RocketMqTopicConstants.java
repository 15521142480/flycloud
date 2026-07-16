package com.fly.common.rocketmq.constant;

/**
 * 按业务域划分的 RocketMQ Topic 常量。
 */
public final class RocketMqTopicConstants {

    /**
     * 系统用户领域事件 Topic。
     */
    public static final String SYSTEM_USER_EVENT = "flycloud-system-user-event";

    /**
     * 商城商品领域事件 Topic。
     */
    public static final String MALL_PRODUCT_EVENT = "flycloud-mall-product-event";

    /**
     * BPM 模型领域事件 Topic。
     */
    public static final String BPM_MODEL_EVENT = "flycloud-bpm-model-event";

    /**
     * 常量类禁止实例化。
     */
    private RocketMqTopicConstants() {
    }
}

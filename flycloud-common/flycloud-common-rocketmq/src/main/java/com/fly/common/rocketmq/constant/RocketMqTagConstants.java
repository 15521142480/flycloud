package com.fly.common.rocketmq.constant;

/**
 * 跨业务域复用的消息事件 Tag 常量。
 */
public final class RocketMqTagConstants {

    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    /**
     * 系统通知事件；消息体中的 eventType 用于进一步区分。
     */
     public static final String NOTIFY = "notify";

    private RocketMqTagConstants() {
    }
}

package com.fly.common.rocketmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** 飞翔云 RocketMQ 公共配置。 */
@Data
@ConfigurationProperties(prefix = "flycloud.rocketmq")
public class RocketMqProperties {

    /** 是否启用 RocketMQ 公共能力。 */
    private boolean enabled = false;

    /** 本地消息表投递配置。 */
    private Outbox outbox = new Outbox();

    /** 本地消息表异步投递参数。 */
    @Data
    public static class Outbox {

        /** 是否启用本地消息调度。 */
        private boolean enabled = true;

        /** 轮询投递间隔（毫秒）。 */
        private long dispatchDelayMillis = 5000;

        /** 每轮扫描的最大消息数。 */
        private int batchSize = 100;

        /** 单条消息最大异步投递尝试次数。 */
        private int maxRetryCount = 16;

        /** 调度实例抢占消息后的租约时长（秒）；实例异常退出后到期消息可再次投递。 */
        private long claimLeaseSeconds = 60;
    }
}

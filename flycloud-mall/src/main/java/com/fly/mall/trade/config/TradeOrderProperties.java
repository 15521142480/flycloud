package com.fly.mall.trade.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 交易订单配置。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Component
@ConfigurationProperties(prefix = "flycloud.trade.order")
public class TradeOrderProperties {

    /**
     * 支付超时时间，默认 2 小时。
     */
    private Duration payExpireTime = Duration.ofHours(2);

    /**
     * 发货后自动收货超时时间，默认 14 天。
     */
    private Duration receiveExpireTime = Duration.ofDays(14);

}

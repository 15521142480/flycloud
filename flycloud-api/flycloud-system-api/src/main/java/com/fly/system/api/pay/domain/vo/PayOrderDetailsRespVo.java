package com.fly.system.api.pay.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 支付订单详情响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayOrderDetailsRespVo extends PayOrderRespVo {
    private static final long serialVersionUID = 1L;

    /**
     * 支付应用名称。
     */
    private String appName;

    /**
     * 更新时间。
     */
    private LocalDateTime updateTime;

    /**
     * 支付订单拓展信息。
     */
    private PayOrderExtensionRespVo extension;

    /**
     * 支付订单拓展响应对象。
     */
    @Data
    public static class PayOrderExtensionRespVo {
        /**
         * 支付订单号。
         */
        private String no;

        /**
         * 支付异步通知内容。
         */
        private String channelNotifyData;
    }
}

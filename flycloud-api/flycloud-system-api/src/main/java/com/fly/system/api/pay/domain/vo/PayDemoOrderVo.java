package com.fly.system.api.pay.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付示例订单视图对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class PayDemoOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long spuId;

    private String spuName;

    private Integer price;

    private Boolean payStatus;

    private Long payOrderId;

    private LocalDateTime payTime;

    private String payChannelCode;

    private Long payRefundId;

    private Integer refundPrice;

    private LocalDateTime refundTime;

    private LocalDateTime createTime;

}

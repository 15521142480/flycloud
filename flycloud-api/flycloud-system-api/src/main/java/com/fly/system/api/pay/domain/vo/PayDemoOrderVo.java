package com.fly.system.api.pay.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付示例订单视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayDemoOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long userId;

    @JsonLongId
    private Long spuId;

    private String spuName;

    private Integer price;

    private Boolean payStatus;

    @JsonLongId
    private Long payOrderId;

    private LocalDateTime payTime;

    private String payChannelCode;

    @JsonLongId
    private Long payRefundId;

    private Integer refundPrice;

    private LocalDateTime refundTime;

    private LocalDateTime createTime;

}

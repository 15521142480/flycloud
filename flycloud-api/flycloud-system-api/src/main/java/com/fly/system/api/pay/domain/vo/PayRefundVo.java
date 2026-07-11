package com.fly.system.api.pay.domain.vo;

import com.fly.common.annotation.JsonLongId;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付退款单视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayRefundVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonLongId
    private Long id;
    private String no;
    @JsonLongId
    private Long appId;
    private String channelCode;
    @JsonLongId
    private Long orderId;
    private String orderNo;
    @JsonLongId
    private Long userId;
    private String merchantOrderId;
    private String merchantRefundId;
    private Integer status;
    private Integer payPrice;
    private Integer refundPrice;
    private String reason;
    private LocalDateTime successTime;
    private LocalDateTime createTime;
}

package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付钱包充值记录返回对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@Schema(description = "支付钱包充值记录返回对象")
public class PayWalletRechargeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long walletId;

    private Integer totalPrice;

    private Integer payPrice;

    private Integer bonusPrice;

    private Long packageId;

    private Boolean payStatus;

    private Long payOrderId;

    private String payChannelCode;

    private LocalDateTime payTime;

    private Integer refundStatus;

    private LocalDateTime createTime;

}

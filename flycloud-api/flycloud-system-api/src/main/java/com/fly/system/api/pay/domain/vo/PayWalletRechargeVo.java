package com.fly.system.api.pay.domain.vo;

import com.fly.common.annotation.JsonLongId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付钱包充值记录返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "支付钱包充值记录返回对象")
public class PayWalletRechargeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    @JsonLongId
    private Long walletId;

    private Integer totalPrice;

    private Integer payPrice;

    private Integer bonusPrice;

    @JsonLongId
    private Long packageId;

    private Boolean payStatus;

    @JsonLongId
    private Long payOrderId;

    private String payChannelCode;

    private LocalDateTime payTime;

    private Integer refundStatus;

    private LocalDateTime createTime;

}

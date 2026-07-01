package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 支付钱包充值记录。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("pay_wallet_recharge")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "支付钱包充值记录对象", description = "支付钱包充值记录")
public class PayWalletRecharge extends BaseEntity {

    @TableId
    @Schema(description = "充值编号")
    private Long id;

    @Schema(description = "钱包编号")
    private Long walletId;

    @Schema(description = "到账金额，单位：分")
    private Integer totalPrice;

    @Schema(description = "支付金额，单位：分")
    private Integer payPrice;

    @Schema(description = "赠送金额，单位：分")
    private Integer bonusPrice;

    @Schema(description = "充值套餐编号")
    private Long packageId;

    @Schema(description = "支付状态")
    private Boolean payStatus;

    @Schema(description = "支付订单编号")
    private Long payOrderId;

    @Schema(description = "支付渠道编码")
    private String payChannelCode;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "支付退款编号")
    private Long payRefundId;

    @Schema(description = "退款总金额，单位：分")
    private Integer refundTotalPrice;

    @Schema(description = "退款支付金额，单位：分")
    private Integer refundPayPrice;

    @Schema(description = "退款赠送金额，单位：分")
    private Integer refundBonusPrice;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "退款状态")
    private Integer refundStatus;

}

package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动端 - 钱包充值记录返回对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@Schema(description = "移动端 - 钱包充值记录返回对象")
public class AppPayWalletRechargeRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "充值编号")
    private Long id;

    @Schema(description = "到账金额，单位：分")
    private Integer totalPrice;

    @Schema(description = "支付金额，单位：分")
    private Integer payPrice;

    @Schema(description = "赠送金额，单位：分")
    private Integer bonusPrice;

    @Schema(description = "支付渠道编码")
    private String payChannelCode;

    @Schema(description = "支付渠道名称")
    private String payChannelName;

    @Schema(description = "支付订单编号")
    private Long payOrderId;

    @Schema(description = "支付渠道订单号")
    private String payOrderChannelOrderNo;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "退款状态")
    private Integer refundStatus;

}

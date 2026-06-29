package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付钱包充值套餐返回对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@Schema(description = "支付钱包充值套餐返回对象")
public class PayWalletRechargePackageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "套餐编号")
    private Long id;

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "支付金额，单位：分")
    private Integer payPrice;

    @Schema(description = "赠送金额，单位：分")
    private Integer bonusPrice;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}

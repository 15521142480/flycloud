package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付钱包充值套餐。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_wallet_recharge_package")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "支付钱包充值套餐对象", description = "支付钱包充值套餐")
public class PayWalletRechargePackage extends BaseEntity {

    @TableId
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

}

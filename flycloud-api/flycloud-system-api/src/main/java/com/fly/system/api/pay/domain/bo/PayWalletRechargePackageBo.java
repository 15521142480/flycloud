package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付钱包充值套餐业务对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付钱包充值套餐业务对象")
public class PayWalletRechargePackageBo extends BaseEntity {

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

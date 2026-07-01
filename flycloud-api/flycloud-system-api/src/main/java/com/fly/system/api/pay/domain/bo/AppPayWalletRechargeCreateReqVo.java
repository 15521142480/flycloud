package com.fly.system.api.pay.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 移动端 - 创建钱包充值请求对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 创建钱包充值请求对象")
public class AppPayWalletRechargeCreateReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(value = 1, message = "支付金额必须大于 0")
    @Schema(description = "支付金额，单位：分")
    private Integer payPrice;

    @Schema(description = "充值套餐编号")
    private Long packageId;

    /**
     * 校验充值金额和套餐至少传一个。
     */
    @AssertTrue(message = "充值金额和充值套餐不能同时为空")
    public boolean isValidPayPriceAndPackageId() {
        return Objects.nonNull(payPrice) || Objects.nonNull(packageId);
    }

}

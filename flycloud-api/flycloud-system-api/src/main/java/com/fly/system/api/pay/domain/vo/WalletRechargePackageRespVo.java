package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 后台 - 钱包充值套餐响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "后台 - 钱包充值套餐响应对象")
public class WalletRechargePackageRespVo extends PayWalletRechargePackageVo {
    private static final long serialVersionUID = 1L;
}

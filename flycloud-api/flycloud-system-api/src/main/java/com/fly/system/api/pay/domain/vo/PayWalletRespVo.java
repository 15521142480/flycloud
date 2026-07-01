package com.fly.system.api.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 支付钱包返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 支付钱包返回对象")
public class PayWalletRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "钱包编号")
    private Long id;

    @Schema(description = "用户编号")
    private Long userId;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "余额，单位：分")
    private Integer balance;

    @Schema(description = "冻结金额，单位：分")
    private Integer freezePrice;

    @Schema(description = "累计支出，单位：分")
    private Integer totalExpense;

    @Schema(description = "累计充值，单位：分")
    private Integer totalRecharge;

}

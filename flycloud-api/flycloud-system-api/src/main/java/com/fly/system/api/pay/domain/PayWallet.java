package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付钱包。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_wallet")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "支付钱包对象", description = "支付钱包")
public class PayWallet extends BaseEntity {

    @TableId
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

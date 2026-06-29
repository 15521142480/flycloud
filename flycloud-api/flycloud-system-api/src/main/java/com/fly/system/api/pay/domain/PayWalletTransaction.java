package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付钱包流水。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_wallet_transaction")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "支付钱包流水对象", description = "支付钱包流水")
public class PayWalletTransaction extends BaseEntity {

    @TableId
    @Schema(description = "流水编号")
    private Long id;

    @Schema(description = "流水号")
    private String no;

    @Schema(description = "钱包编号")
    private Long walletId;

    @Schema(description = "业务类型")
    private Integer bizType;

    @Schema(description = "业务编号")
    private String bizId;

    @Schema(description = "流水标题")
    private String title;

    @Schema(description = "交易金额，单位：分")
    private Integer price;

    @Schema(description = "交易后余额，单位：分")
    private Integer balance;

}

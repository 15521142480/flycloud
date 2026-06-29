package com.fly.system.api.pay.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 钱包流水创建请求对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@Schema(description = "钱包流水创建请求对象")
public class WalletTransactionCreateReqBo implements Serializable {

    private static final long serialVersionUID = 1L;

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

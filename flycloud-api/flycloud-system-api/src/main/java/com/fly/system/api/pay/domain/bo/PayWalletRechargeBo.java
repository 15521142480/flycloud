package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付钱包充值记录查询业务对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付钱包充值记录查询业务对象")
public class PayWalletRechargeBo extends BaseEntity {

    @Schema(description = "充值编号")
    private Long id;

    @Schema(description = "钱包编号")
    private Long walletId;

    @Schema(description = "支付状态")
    private Boolean payStatus;

    @Schema(description = "支付订单编号")
    private Long payOrderId;

}

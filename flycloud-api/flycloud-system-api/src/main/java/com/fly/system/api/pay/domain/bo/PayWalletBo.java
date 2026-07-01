package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付钱包查询业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付钱包查询业务对象")
public class PayWalletBo extends BaseEntity {

    @Schema(description = "钱包编号")
    private Long id;

    @Schema(description = "用户编号")
    private Long userId;

    @Schema(description = "用户类型")
    private Integer userType;

}

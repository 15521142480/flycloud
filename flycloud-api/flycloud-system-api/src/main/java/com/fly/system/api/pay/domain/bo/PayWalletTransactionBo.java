package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 支付钱包流水查询业务对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付钱包流水查询业务对象")
public class PayWalletTransactionBo extends BaseEntity {

    @Schema(description = "钱包编号")
    private Long walletId;

    @Schema(description = "用户编号")
    private Long userId;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "业务类型")
    private Integer bizType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间范围")
    private LocalDateTime[] createTimeRange;

}

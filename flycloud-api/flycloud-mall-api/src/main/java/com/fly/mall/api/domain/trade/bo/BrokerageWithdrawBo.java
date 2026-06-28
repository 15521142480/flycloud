package com.fly.mall.api.domain.trade.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 佣金提现 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class BrokerageWithdrawBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "price")
    private Integer price;

    @Schema(description = "feePrice")
    private Integer feePrice;

    @Schema(description = "totalPrice")
    private Integer totalPrice;

    @Schema(description = "type")
    private Integer type;

    @Schema(description = "userName")
    private String userName;

    @Schema(description = "userAccount")
    private String userAccount;

    @Schema(description = "qrCodeUrl")
    private String qrCodeUrl;

    @Schema(description = "bankName")
    private String bankName;

    @Schema(description = "bankAddress")
    private String bankAddress;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "auditReason")
    private String auditReason;

    @Schema(description = "auditTime")
    private LocalDateTime auditTime;

    @Schema(description = "remark")
    private String remark;

    @Schema(description = "payTransferId")
    private Long payTransferId;

    @Schema(description = "transferChannelCode")
    private String transferChannelCode;

    @Schema(description = "transferTime")
    private LocalDateTime transferTime;

    @Schema(description = "transferErrorMsg")
    private String transferErrorMsg;

}

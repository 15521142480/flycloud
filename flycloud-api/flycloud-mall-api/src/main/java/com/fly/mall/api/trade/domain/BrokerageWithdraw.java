package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 佣金提现 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_brokerage_withdraw")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "佣金提现对象", description = "佣金提现表")
public class BrokerageWithdraw extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Integer price;

    private Integer feePrice;

    private Integer totalPrice;

    private Integer type;

    private String userName;

    private String userAccount;

    private String qrCodeUrl;

    private String bankName;

    private String bankAddress;

    private Integer status;

    private String auditReason;

    private LocalDateTime auditTime;

    private String remark;

    private Long payTransferId;

    private String transferChannelCode;

    private LocalDateTime transferTime;

    private String transferErrorMsg;

}

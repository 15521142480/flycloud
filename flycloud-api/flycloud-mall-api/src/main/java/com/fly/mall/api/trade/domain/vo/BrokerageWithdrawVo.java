package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 佣金提现 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class BrokerageWithdrawVo implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private Boolean isDeleted;

    private LocalDateTime createTime;

}

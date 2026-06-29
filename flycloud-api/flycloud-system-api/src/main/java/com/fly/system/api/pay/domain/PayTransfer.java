package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 支付转账单。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_transfer")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayTransfer extends BaseEntity {
    @TableId
    private Long id;
    private String no;
    private Long appId;
    private Long channelId;
    private String channelCode;
    private Long userId;
    private Integer userType;
    private String merchantTransferId;
    private String subject;
    private Integer price;
    private String userAccount;
    private String userName;
    private Integer status;
    private LocalDateTime successTime;
    private String notifyUrl;
    private String userIp;
    private String channelExtras;
    private String channelTransferNo;
    private String channelErrorCode;
    private String channelErrorMsg;
    private String channelNotifyData;
    private String channelPackageInfo;
}

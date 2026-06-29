package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 支付退款单。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_refund")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayRefund extends BaseEntity {
    @TableId
    private Long id;
    private String no;
    private Long appId;
    private Long channelId;
    private String channelCode;
    private Long orderId;
    private String orderNo;
    private Long userId;
    private Integer userType;
    private String merchantOrderId;
    private String merchantRefundId;
    private String notifyUrl;
    private Integer status;
    private Integer payPrice;
    private Integer refundPrice;
    private String reason;
    private String userIp;
    private String channelOrderNo;
    private String channelRefundNo;
    private LocalDateTime successTime;
    private String channelErrorCode;
    private String channelErrorMsg;
    private String channelNotifyData;
}

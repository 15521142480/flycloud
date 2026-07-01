package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 支付订单。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("pay_order")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "支付订单对象", description = "支付订单")
public class PayOrder extends BaseEntity {

    @TableId
    private Long id;

    private Long appId;

    private Long channelId;

    private String channelCode;

    private Long userId;

    private Integer userType;

    private String merchantOrderId;

    private String subject;

    private String body;

    private String notifyUrl;

    private Integer price;

    private Double channelFeeRate;

    private Integer channelFeePrice;

    private Integer status;

    private String userIp;

    private LocalDateTime expireTime;

    private LocalDateTime successTime;

    private Long extensionId;

    private String no;

    private Integer refundPrice;

    private String channelUserId;

    private String channelOrderNo;

}

package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 支付示例订单。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("pay_demo_order")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayDemoOrder extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Long spuId;

    private String spuName;

    private Integer price;

    private Boolean payStatus;

    private Long payOrderId;

    private LocalDateTime payTime;

    private String payChannelCode;

    private Long payRefundId;

    private Integer refundPrice;

    private LocalDateTime refundTime;

}

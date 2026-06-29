package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付渠道。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_channel")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayChannel extends BaseEntity {
    @TableId
    private Long id;
    private String code;
    private Integer status;
    private Double feeRate;
    private String remark;
    private Long appId;
    private String config;
}

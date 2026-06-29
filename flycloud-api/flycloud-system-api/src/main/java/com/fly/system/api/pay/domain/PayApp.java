package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付应用。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_app")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayApp extends BaseEntity {
    @TableId
    private Long id;
    private String appKey;
    private String name;
    private Integer status;
    private String remark;
    private String orderNotifyUrl;
    private String refundNotifyUrl;
    private String transferNotifyUrl;
}

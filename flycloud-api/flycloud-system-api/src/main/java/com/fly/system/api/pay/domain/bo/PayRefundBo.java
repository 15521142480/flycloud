package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付退款单业务对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayRefundBo extends BaseEntity {
    private Long id;
    private String no;
    private Long appId;
    private Long orderId;
    private Long userId;
    private String merchantOrderId;
    private String merchantRefundId;
    private Integer status;
}

package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付应用业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayAppBo extends BaseEntity {
    private Long id;
    private String appKey;
    private String name;
    private Integer status;
    private String remark;
    private String orderNotifyUrl;
    private String refundNotifyUrl;
    private String transferNotifyUrl;
}

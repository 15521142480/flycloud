package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付渠道业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayChannelBo extends BaseEntity {
    private Long id;
    private String code;
    private Integer status;
    private Double feeRate;
    private String remark;
    private Long appId;
    private String config;
}

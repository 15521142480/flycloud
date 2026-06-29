package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付转账单业务对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayTransferBo extends BaseEntity {
    private Long id;
    private String no;
    private Long appId;
    private String channelCode;
    private Long userId;
    private String merchantTransferId;
    private Integer status;
}

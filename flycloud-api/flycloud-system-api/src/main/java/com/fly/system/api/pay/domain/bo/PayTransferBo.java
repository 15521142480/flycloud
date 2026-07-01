package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付转账单业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayTransferBo extends BaseEntity {
    private Long id;
    private String no;
    private Long appId;
    private String channelCode;
    private Long userId;
    private Integer userType;
    private String merchantTransferId;
    private String subject;
    private Integer price;
    private String userAccount;
    private String userName;
    private Integer status;
}

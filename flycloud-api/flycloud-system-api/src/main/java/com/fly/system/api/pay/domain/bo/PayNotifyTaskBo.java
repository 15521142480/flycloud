package com.fly.system.api.pay.domain.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 支付通知任务查询 BO。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
public class PayNotifyTaskBo {

    private Long appId;

    private Integer type;

    private Long dataId;

    private Integer status;

    private String merchantOrderId;

    private String merchantRefundId;

    private String merchantTransferId;

    private LocalDateTime[] createTime;

}

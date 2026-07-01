package com.fly.system.api.pay.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 支付通知任务 VO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayNotifyTaskVo {

    private Long id;

    private Long appId;

    private String appName;

    private Integer type;

    private Long dataId;

    private String merchantOrderId;

    private String merchantRefundId;

    private String merchantTransferId;

    private Integer status;

    private LocalDateTime nextNotifyTime;

    private LocalDateTime lastExecuteTime;

    private Integer notifyTimes;

    private Integer maxNotifyTimes;

    private String notifyUrl;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}

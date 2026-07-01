package com.fly.system.api.pay.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 支付通知日志 VO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PayNotifyLogVo {

    private Long id;

    private Long taskId;

    private Integer notifyTimes;

    private String response;

    private Integer status;

    private LocalDateTime createTime;

}

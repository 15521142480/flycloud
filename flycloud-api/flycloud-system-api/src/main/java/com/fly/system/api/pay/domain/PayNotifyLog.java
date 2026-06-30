package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付通知日志。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_notify_log")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayNotifyLog extends BaseEntity {

    @TableId
    private Long id;

    private Long taskId;

    private Integer notifyTimes;

    private String response;

    private Integer status;

}

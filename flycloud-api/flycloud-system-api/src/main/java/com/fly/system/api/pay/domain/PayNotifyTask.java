package com.fly.system.api.pay.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 支付通知任务。
 *
 * @author lxs
 * @date 2026-06-30
 */
@TableName("pay_notify_task")
@Data
@EqualsAndHashCode(callSuper = true)
public class PayNotifyTask extends BaseEntity {

    /**
     * 通知频率，单位秒；算上首次通知一共 9 次。
     */
    public static final Integer[] NOTIFY_FREQUENCY = new Integer[]{
            15, 15, 30, 180, 1800, 1800, 1800, 3600
    };

    @TableId
    private Long id;

    private Long appId;

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

}

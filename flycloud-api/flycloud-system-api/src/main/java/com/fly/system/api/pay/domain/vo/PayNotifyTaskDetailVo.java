package com.fly.system.api.pay.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 支付通知任务详情 VO。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayNotifyTaskDetailVo extends PayNotifyTaskVo {

    private List<PayNotifyLogVo> logs;

}

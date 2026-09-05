package com.fly.pay.event;

/**
 * 支付通知任务事件。
 *
 * @param taskId 通知任务编号
 */
public record PayNotifyTaskEvent(Long taskId) {
}

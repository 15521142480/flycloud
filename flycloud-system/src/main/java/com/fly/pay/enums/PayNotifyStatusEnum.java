package com.fly.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付通知状态枚举。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Getter
@AllArgsConstructor
public enum PayNotifyStatusEnum {

    WAITING(0, "等待通知"),
    SUCCESS(10, "通知成功"),
    FAILURE(20, "通知失败"),
    REQUEST_SUCCESS(21, "请求成功，但是结果失败"),
    REQUEST_FAILURE(22, "请求失败");

    private final Integer status;

    private final String name;

}

package com.fly.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付通知类型枚举。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Getter
@AllArgsConstructor
public enum PayNotifyTypeEnum {

    ORDER(1, "支付单"),
    REFUND(2, "退款单"),
    TRANSFER(3, "转账单");

    private final Integer type;

    private final String name;

}

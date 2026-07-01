package com.fly.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付示例提现单状态枚举。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Getter
@AllArgsConstructor
public enum PayDemoWithdrawStatusEnum {

    /**
     * 等待提现。
     */
    WAITING(0),

    /**
     * 提现成功。
     */
    SUCCESS(10),

    /**
     * 提现关闭。
     */
    CLOSED(20);

    private final Integer status;

    /**
     * 是否为等待提现状态。
     */
    public static boolean isWaiting(Integer status) {
        return WAITING.getStatus().equals(status);
    }

    /**
     * 是否为提现成功状态。
     */
    public static boolean isSuccess(Integer status) {
        return SUCCESS.getStatus().equals(status);
    }

    /**
     * 是否为提现关闭状态。
     */
    public static boolean isClosed(Integer status) {
        return CLOSED.getStatus().equals(status);
    }

}

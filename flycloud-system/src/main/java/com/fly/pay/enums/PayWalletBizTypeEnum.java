package com.fly.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 钱包交易业务类型。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Getter
@AllArgsConstructor
public enum PayWalletBizTypeEnum {

    RECHARGE(1, "充值"),
    RECHARGE_REFUND(2, "充值退款"),
    PAYMENT(3, "支付"),
    PAYMENT_REFUND(4, "支付退款"),
    UPDATE_BALANCE(5, "更新余额"),
    TRANSFER(6, "转账");

    private final Integer type;

    private final String description;

    /**
     * 根据类型值获取钱包交易业务类型。
     */
    public static PayWalletBizTypeEnum valueOfType(Integer type) {
        return Arrays.stream(values()).filter(item -> item.type.equals(type)).findFirst().orElse(null);
    }

}

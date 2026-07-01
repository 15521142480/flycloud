package com.fly.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付示例提现方式枚举。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Getter
@AllArgsConstructor
public enum PayDemoWithdrawTypeEnum {

    /**
     * 支付宝。
     */
    ALIPAY(1),

    /**
     * 微信。
     */
    WECHAT(2),

    /**
     * 钱包。
     */
    WALLET(3);

    private final Integer type;

}

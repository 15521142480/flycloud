package com.fly.common.enums.mall;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商城佣金提现状态枚举。
 */
@Getter
@AllArgsConstructor
public enum BrokerageWithdrawStatusEnum implements ArrayValuable<Integer> {

    AUDITING(0, "审核中"),
    AUDIT_SUCCESS(10, "审核通过"),
    WITHDRAW_SUCCESS(11, "提现成功"),
    AUDIT_FAIL(20, "审核不通过"),
    WITHDRAW_FAIL(21, "提现失败");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(BrokerageWithdrawStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}

package com.fly.common.enums.pay;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 支付退款单状态枚举。
 */
@Getter
@AllArgsConstructor
public enum PayRefundStatusEnum implements ArrayValuable<Integer> {

    WAITING(0, "等待退款"),
    SUCCESS(10, "退款成功"),
    FAILURE(20, "退款失败");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(PayRefundStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static boolean isSuccess(Integer status) {
        return SUCCESS.status.equals(status);
    }
}

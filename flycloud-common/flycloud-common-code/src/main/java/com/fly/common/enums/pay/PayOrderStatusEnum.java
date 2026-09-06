package com.fly.common.enums.pay;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 支付订单状态枚举。
 */
@Getter
@AllArgsConstructor
public enum PayOrderStatusEnum implements ArrayValuable<Integer> {

    WAITING(0, "等待支付"),
    SUCCESS(10, "支付成功"),
    REFUND(20, "已退款"),
    CLOSED(30, "支付关闭");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(PayOrderStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static boolean isWaiting(Integer status) {
        return WAITING.status.equals(status);
    }

    public static boolean isSuccess(Integer status) {
        return SUCCESS.status.equals(status);
    }

    public static boolean isRefund(Integer status) {
        return REFUND.status.equals(status);
    }

    public static boolean isClosed(Integer status) {
        return CLOSED.status.equals(status);
    }
}

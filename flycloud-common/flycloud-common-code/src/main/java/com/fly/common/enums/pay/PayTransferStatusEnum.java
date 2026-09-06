package com.fly.common.enums.pay;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 支付转账单状态枚举。
 */
@Getter
@AllArgsConstructor
public enum PayTransferStatusEnum implements ArrayValuable<Integer> {

    WAITING(0, "等待转账"),
    PROCESSING(10, "转账进行中"),
    SUCCESS(20, "转账成功"),
    CLOSED(30, "转账关闭");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(PayTransferStatusEnum::getStatus)
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

    public static boolean isProcessing(Integer status) {
        return PROCESSING.status.equals(status);
    }

    public static boolean isSuccess(Integer status) {
        return SUCCESS.status.equals(status);
    }

    public static boolean isClosed(Integer status) {
        return CLOSED.status.equals(status);
    }
}

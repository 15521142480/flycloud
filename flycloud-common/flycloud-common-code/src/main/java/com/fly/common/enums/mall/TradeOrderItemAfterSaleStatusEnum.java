package com.fly.common.enums.mall;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商城交易订单项售后状态枚举。
 */
@Getter
@AllArgsConstructor
public enum TradeOrderItemAfterSaleStatusEnum implements ArrayValuable<Integer> {

    NONE(0, "未售后"),
    APPLYING(10, "售后中"),
    SUCCESS(20, "已退款");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(TradeOrderItemAfterSaleStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}

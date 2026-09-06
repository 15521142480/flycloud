package com.fly.common.enums.mall;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商城交易订单状态枚举。
 */
@Getter
@AllArgsConstructor
public enum TradeOrderStatusEnum implements ArrayValuable<Integer> {

    UNPAID(0, "待支付"),
    UNDELIVERED(10, "待发货"),
    DELIVERED(20, "已发货"),
    COMPLETED(30, "已完成"),
    CANCELED(40, "已取消");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(TradeOrderStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}

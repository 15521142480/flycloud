package com.fly.common.enums.mall;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商城交易订单退款状态枚举。
 */
@Getter
@AllArgsConstructor
public enum TradeOrderRefundStatusEnum implements ArrayValuable<Integer> {

    NONE(0, "未退款"),
    PART(10, "部分退款"),
    ALL(20, "全部退款");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(TradeOrderRefundStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}

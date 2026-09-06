package com.fly.common.enums.mall;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商城佣金记录状态枚举。
 */
@Getter
@AllArgsConstructor
public enum BrokerageRecordStatusEnum implements ArrayValuable<Integer> {

    WAIT_SETTLEMENT(0, "待结算"),
    SETTLED(1, "已结算"),
    CANCELED(2, "已取消");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(BrokerageRecordStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}

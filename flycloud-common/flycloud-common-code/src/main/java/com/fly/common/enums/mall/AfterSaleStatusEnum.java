package com.fly.common.enums.mall;

import com.fly.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 商城交易售后单状态枚举。
 */
@Getter
@AllArgsConstructor
public enum AfterSaleStatusEnum implements ArrayValuable<Integer> {

    APPLY(10, "申请售后"),
    SELLER_AGREE(20, "商品待退货"),
    BUYER_DELIVERY(30, "商家待收货"),
    WAIT_REFUND(40, "等待退款"),
    COMPLETE(50, "退款成功"),
    BUYER_CANCEL(61, "买家取消"),
    SELLER_DISAGREE(62, "商家拒绝"),
    SELLER_REFUSE(63, "商家拒绝收货");

    public static final Integer[] ARRAYS = Arrays.stream(values())
            .map(AfterSaleStatusEnum::getStatus)
            .toArray(Integer[]::new);

    private final Integer status;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}

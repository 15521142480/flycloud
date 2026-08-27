package com.fly.ai.toolcalling.model;

import java.time.LocalDateTime;

/**
 * 提供给模型的最小化订单信息。
 * <p>
 * 不包含收货人、手机号、地址、物流单号、用户 IP 等敏感字段；金额单位均为分。
 *
 * @param orderId 订单编号
 * @param orderNo 订单流水号
 * @param status 订单状态编码
 * @param productCount 商品数量
 * @param payStatus 是否支付
 * @param totalPrice 商品总价，单位分
 * @param discountPrice 优惠金额，单位分
 * @param deliveryPrice 运费，单位分
 * @param payPrice 实付金额，单位分
 * @param createTime 下单时间
 * @author lxs
 * @date 2026-08-27
 */
public record AiToolOrderSummary(
        Long orderId,
        String orderNo,
        Integer status,
        Integer productCount,
        Boolean payStatus,
        Integer totalPrice,
        Integer discountPrice,
        Integer deliveryPrice,
        Integer payPrice,
        LocalDateTime createTime) {
}

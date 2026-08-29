package com.fly.ai.common.tool.model;

import java.time.LocalDateTime;

/**
 * 提供给模型的最小化订单信息。
 * <p>
 * 不包含收货人、手机号、地址、物流单号、用户 IP 等敏感字段。{@code buyerUserId} 仅用于让模型在用户追问
 * “谁购买了订单”时继续调用公共用户查询工具，不直接携带买家个人资料；金额单位均为分。
 *
 * @param orderId 订单编号
 * @param orderNo 订单流水号
 * @param buyerUserId 下单用户编号，仅用于继续查询公共用户信息
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
        Long buyerUserId,
        Integer status,
        Integer productCount,
        Boolean payStatus,
        Integer totalPrice,
        Integer discountPrice,
        Integer deliveryPrice,
        Integer payPrice,
        LocalDateTime createTime) {
}

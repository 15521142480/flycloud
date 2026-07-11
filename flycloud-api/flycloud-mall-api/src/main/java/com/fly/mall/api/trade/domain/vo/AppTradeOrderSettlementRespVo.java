package com.fly.mall.api.trade.domain.vo;

import com.fly.common.annotation.JsonLongId;

import com.fly.mall.api.product.domain.vo.AppProductPropertyValueDetailRespVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 移动端 - 交易订单结算返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 交易订单结算返回对象")
public class AppTradeOrderSettlementRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "交易类型")
    private Integer type;

    @Schema(description = "购物项数组")
    private List<Item> items;

    @Schema(description = "优惠券数组")
    private List<Coupon> coupons;

    @Schema(description = "费用")
    private Price price;

    @Schema(description = "收件地址")
    private Address address;

    @Schema(description = "已使用的积分")
    private Integer usePoint;

    @Schema(description = "总积分")
    private Integer totalPoint;

    @Schema(description = "营销活动数组")
    private List<Object> promotions;

    /**
     * 移动端 - 结算购物项。
     */
    @Data
    @Schema(description = "移动端 - 结算购物项")
    public static class Item implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "品类编号")
        @JsonLongId
        private Long categoryId;

        @Schema(description = "SPU 编号")
        @JsonLongId
        private Long spuId;

        @Schema(description = "SPU 名称")
        private String spuName;

        @Schema(description = "SKU 编号")
        @JsonLongId
        private Long skuId;

        @Schema(description = "价格，单位：分")
        private Integer price;

        @Schema(description = "图片地址")
        private String picUrl;

        @Schema(description = "属性数组")
        private List<AppProductPropertyValueDetailRespVo> properties;

        @Schema(description = "购物车编号")
        @JsonLongId
        private Long cartId;

        @Schema(description = "购买数量")
        private Integer count;

    }

    /**
     * 移动端 - 订单费用。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "移动端 - 订单费用")
    public static class Price implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "商品原价，单位：分")
        private Integer totalPrice;

        @Schema(description = "订单优惠，单位：分")
        private Integer discountPrice;

        @Schema(description = "运费金额，单位：分")
        private Integer deliveryPrice;

        @Schema(description = "优惠券减免金额，单位：分")
        private Integer couponPrice;

        @Schema(description = "积分抵扣金额，单位：分")
        private Integer pointPrice;

        @Schema(description = "VIP 减免金额，单位：分")
        private Integer vipPrice;

        @Schema(description = "实际支付金额，单位：分")
        private Integer payPrice;

    }

    /**
     * 移动端 - 地址信息。
     */
    @Data
    @Schema(description = "移动端 - 地址信息")
    public static class Address implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "编号")
        @JsonLongId
        private Long id;

        @Schema(description = "收件人名称")
        private String name;

        @Schema(description = "手机号")
        private String mobile;

        @Schema(description = "地区编号")
        @JsonLongId
        private Long areaId;

        @Schema(description = "地区名称")
        private String areaName;

        @Schema(description = "详细地址")
        private String detailAddress;

        @Schema(description = "是否默认收件地址")
        private Boolean defaultStatus;

    }

    /**
     * 移动端 - 优惠券信息。
     */
    @Data
    @Schema(description = "移动端 - 优惠券信息")
    public static class Coupon implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "优惠券编号")
        @JsonLongId
        private Long id;

        @Schema(description = "优惠券名称")
        private String name;

        @Schema(description = "使用门槛金额，单位：分")
        private Integer usePrice;

        @Schema(description = "生效开始时间")
        private LocalDateTime validStartTime;

        @Schema(description = "生效结束时间")
        private LocalDateTime validEndTime;

        @Schema(description = "优惠类型")
        private Integer discountType;

        @Schema(description = "折扣百分比")
        private Integer discountPercent;

        @Schema(description = "优惠金额")
        private Integer discountPrice;

        @Schema(description = "折扣上限")
        private Integer discountLimitPrice;

        @Schema(description = "是否可用")
        private Boolean match;

        @Schema(description = "不可用原因")
        private String mismatchReason;

    }

}

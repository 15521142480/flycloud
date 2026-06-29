package com.fly.mall.api.trade.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端 - 交易订单结算请求对象。
 *
 * @author lxs
 * @date 2026-06-29
 */
@Data
@Schema(description = "移动端 - 交易订单结算请求对象")
public class AppTradeOrderSettlementReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商品项数组")
    @Valid
    @NotEmpty(message = "商品不能为空")
    private List<Item> items;

    @Schema(description = "优惠券编号")
    private Long couponId;

    @Schema(description = "是否使用积分")
    private Boolean pointStatus;

    @Schema(description = "配送方式")
    private Integer deliveryType;

    @Schema(description = "收件地址编号")
    private Long addressId;

    @Schema(description = "自提门店编号")
    private Long pickUpStoreId;

    @Schema(description = "收件人名称")
    private String receiverName;

    @Schema(description = "收件人手机")
    private String receiverMobile;

    @Schema(description = "秒杀活动编号")
    private Long seckillActivityId;

    @Schema(description = "拼团活动编号")
    private Long combinationActivityId;

    @Schema(description = "拼团团长编号")
    private Long combinationHeadId;

    @Schema(description = "砍价记录编号")
    private Long bargainRecordId;

    @Schema(description = "积分商城活动编号")
    private Long pointActivityId;

    /**
     * 移动端 - 结算商品项。
     */
    @Data
    @Schema(description = "移动端 - 结算商品项")
    public static class Item implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "商品 SKU 编号")
        private Long skuId;

        @Schema(description = "购买数量")
        @Min(value = 1, message = "购买数量最小值为 {value}")
        private Integer count;

        @Schema(description = "购物车项编号")
        private Long cartId;

    }

}

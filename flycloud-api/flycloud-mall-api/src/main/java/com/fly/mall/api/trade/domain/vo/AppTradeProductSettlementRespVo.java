package com.fly.mall.api.trade.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 移动端 - 商品结算信息返回对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@Schema(description = "移动端 - 商品结算信息返回对象")
public class AppTradeProductSettlementRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "SPU 商品编号")
    private Long spuId;

    @Schema(description = "SKU 价格信息数组")
    private List<Sku> skus;

    @Schema(description = "满减送活动信息")
    private RewardActivity rewardActivity;

    /**
     * 移动端 - SKU 结算价格。
     */
    @Data
    @Schema(description = "移动端 - SKU 结算价格")
    public static class Sku implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "商品 SKU 编号")
        private Long id;

        @Schema(description = "优惠后价格，单位：分")
        private Integer promotionPrice;

        @Schema(description = "营销类型")
        private Integer promotionType;

        @Schema(description = "营销编号")
        private Long promotionId;

        @Schema(description = "活动结束时间")
        private LocalDateTime promotionEndTime;

    }

    /**
     * 移动端 - 满减送活动信息。
     */
    @Data
    @Schema(description = "移动端 - 满减送活动信息")
    public static class RewardActivity implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "满减活动编号")
        private Long id;

        @Schema(description = "条件类型")
        private Integer conditionType;

        @Schema(description = "优惠规则数组")
        private List<RewardActivityRule> rules;

    }

    /**
     * 移动端 - 满减送优惠规则。
     */
    @Data
    @Schema(description = "移动端 - 满减送优惠规则")
    public static class RewardActivityRule implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "优惠门槛")
        private Integer limit;

        @Schema(description = "优惠价格，单位：分")
        private Integer discountPrice;

        @Schema(description = "是否包邮")
        private Boolean freeDelivery;

        @Schema(description = "赠送积分")
        private Integer point;

        @Schema(description = "赠送优惠券模板数量")
        private Map<Long, Integer> giveCouponTemplateCounts;

    }

}

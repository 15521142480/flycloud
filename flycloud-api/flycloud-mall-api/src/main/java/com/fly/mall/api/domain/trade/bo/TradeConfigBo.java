package com.fly.mall.api.domain.trade.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 交易配置 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class TradeConfigBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "afterSaleRefundReasons")
    private List<String> afterSaleRefundReasons;

    @Schema(description = "afterSaleReturnReasons")
    private List<String> afterSaleReturnReasons;

    @Schema(description = "deliveryExpressFreeEnabled")
    private Boolean deliveryExpressFreeEnabled;

    @Schema(description = "deliveryExpressFreePrice")
    private Integer deliveryExpressFreePrice;

    @Schema(description = "deliveryPickUpEnabled")
    private Boolean deliveryPickUpEnabled;

    @Schema(description = "brokerageEnabled")
    private Boolean brokerageEnabled;

    @Schema(description = "brokerageEnabledCondition")
    private Integer brokerageEnabledCondition;

    @Schema(description = "brokerageBindMode")
    private Integer brokerageBindMode;

    @Schema(description = "brokeragePosterUrls")
    private List<String> brokeragePosterUrls;

    @Schema(description = "brokerageFirstPercent")
    private Integer brokerageFirstPercent;

    @Schema(description = "brokerageSecondPercent")
    private Integer brokerageSecondPercent;

    @Schema(description = "brokerageWithdrawMinPrice")
    private Integer brokerageWithdrawMinPrice;

    @Schema(description = "brokerageWithdrawFeePercent")
    private Integer brokerageWithdrawFeePercent;

    @Schema(description = "brokerageFrozenDays")
    private Integer brokerageFrozenDays;

    @Schema(description = "brokerageWithdrawTypes")
    private List<Integer> brokerageWithdrawTypes;

}

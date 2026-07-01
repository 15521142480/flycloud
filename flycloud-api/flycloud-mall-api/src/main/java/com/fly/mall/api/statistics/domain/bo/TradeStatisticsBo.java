package com.fly.mall.api.statistics.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 交易统计 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class TradeStatisticsBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "time")
    private LocalDateTime time;

    @Schema(description = "orderCreateCount")
    private Integer orderCreateCount;

    @Schema(description = "orderPayCount")
    private Integer orderPayCount;

    @Schema(description = "orderPayPrice")
    private Integer orderPayPrice;

    @Schema(description = "afterSaleCount")
    private Integer afterSaleCount;

    @Schema(description = "afterSaleRefundPrice")
    private Integer afterSaleRefundPrice;

    @Schema(description = "brokerageSettlementPrice")
    private Integer brokerageSettlementPrice;

    @Schema(description = "walletPayPrice")
    private Integer walletPayPrice;

    @Schema(description = "rechargePayCount")
    private Integer rechargePayCount;

    @Schema(description = "rechargePayPrice")
    private Integer rechargePayPrice;

    @Schema(description = "rechargeRefundCount")
    private Integer rechargeRefundCount;

    @Schema(description = "rechargeRefundPrice")
    private Integer rechargeRefundPrice;

}

package com.fly.mall.api.trade.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端 - 交易订单数量响应对象。
 *
 * @author lxs
 * @date 2026-09-06
 */
@Data
@Schema(description = "移动端 - 交易订单数量响应对象")
public class AppTradeOrderCountRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "全部订单数量")
    private Long allCount;

    @Schema(description = "待付款订单数量")
    private Long unpaidCount;

    @Schema(description = "待发货订单数量")
    private Long undeliveredCount;

    @Schema(description = "待收货订单数量")
    private Long deliveredCount;

    @Schema(description = "待评价订单数量")
    private Long uncommentedCount;

    @Schema(description = "售后单数量")
    private Long afterSaleCount;

}

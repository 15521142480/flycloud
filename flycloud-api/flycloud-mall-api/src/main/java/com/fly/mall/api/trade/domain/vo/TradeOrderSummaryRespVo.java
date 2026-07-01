package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * 管理后台 - 交易订单统计响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class TradeOrderSummaryRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单数量。
     */
    private Long orderCount;

    /**
     * 订单金额。
     */
    private Long orderPayPrice;

    /**
     * 售后数量。
     */
    private Long afterSaleCount;

    /**
     * 售后金额。
     */
    private Long afterSalePrice;
}

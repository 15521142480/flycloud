package com.fly.mall.api.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 交易订单数量响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class TradeOrderCountRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long undelivered;

    private Long pickUp;

    private Long afterSaleApply;

    private Long auditingWithdraw;

}

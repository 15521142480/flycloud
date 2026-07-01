package com.fly.mall.api.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 交易订单统计响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class StatisticsTradeOrderSummaryRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer orderPayCount;

    private Integer orderPayPrice;

}

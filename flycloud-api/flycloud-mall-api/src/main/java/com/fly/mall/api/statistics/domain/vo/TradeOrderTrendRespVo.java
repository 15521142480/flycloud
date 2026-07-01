package com.fly.mall.api.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单量趋势统计响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
public class TradeOrderTrendRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String date;

    private Integer orderPayCount;

    private Integer orderPayPrice;

}

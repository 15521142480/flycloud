package com.fly.mall.api.statistics.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 交易统计响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
public class TradeSummaryRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer yesterdayOrderCount;

    private Integer yesterdayPayPrice;

    private Integer monthOrderCount;

    private Integer monthPayPrice;

}

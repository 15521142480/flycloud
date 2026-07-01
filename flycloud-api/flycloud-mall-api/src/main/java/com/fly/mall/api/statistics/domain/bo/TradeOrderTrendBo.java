package com.fly.mall.api.statistics.domain.bo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 交易订单趋势查询对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
public class TradeOrderTrendBo {

    /**
     * 日期范围类型。
     */
    private Integer type;

    /**
     * 起始时间。
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    /**
     * 截止时间。
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

}

package com.fly.mall.api.statistics.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 交易统计 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class TradeStatisticsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDateTime time;

    private Integer orderCreateCount;

    private Integer orderPayCount;

    private Integer orderPayPrice;

    private Integer afterSaleCount;

    private Integer afterSaleRefundPrice;

    private Integer brokerageSettlementPrice;

    private Integer walletPayPrice;

    private Integer rechargePayCount;

    private Integer rechargePayPrice;

    private Integer rechargeRefundCount;

    private Integer rechargeRefundPrice;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}

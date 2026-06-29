package com.fly.mall.api.statistics.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易统计 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_statistics")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "交易统计对象", description = "交易统计表")
public class TradeStatistics extends BaseEntity {

    @TableId
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

}

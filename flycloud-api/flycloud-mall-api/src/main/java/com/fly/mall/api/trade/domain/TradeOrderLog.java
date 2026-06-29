package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易订单日志 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "trade_order_log")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "交易订单日志对象", description = "交易订单日志表")
public class TradeOrderLog extends BaseEntity {

    @TableId
    private Long id;

    private Long userId;

    private Integer userType;

    private Long orderId;

    private Integer beforeStatus;

    private Integer afterStatus;

    private Integer operateType;

    private String content;

}

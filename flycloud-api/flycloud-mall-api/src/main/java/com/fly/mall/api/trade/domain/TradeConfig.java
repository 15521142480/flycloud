package com.fly.mall.api.trade.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 交易配置 表。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName(value = "trade_config", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "交易配置对象", description = "交易配置表")
public class TradeConfig extends BaseEntity {

    @TableId
    private Long id;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> afterSaleRefundReasons;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> afterSaleReturnReasons;

    private Boolean deliveryExpressFreeEnabled;

    private Integer deliveryExpressFreePrice;

    private Boolean deliveryPickUpEnabled;

    private Boolean brokerageEnabled;

    private Integer brokerageEnabledCondition;

    private Integer brokerageBindMode;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> brokeragePosterUrls;

    private Integer brokerageFirstPercent;

    private Integer brokerageSecondPercent;

    private Integer brokerageWithdrawMinPrice;

    private Integer brokerageWithdrawFeePercent;

    private Integer brokerageFrozenDays;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> brokerageWithdrawTypes;

}

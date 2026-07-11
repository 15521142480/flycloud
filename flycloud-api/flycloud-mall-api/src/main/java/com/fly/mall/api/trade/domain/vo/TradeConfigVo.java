package com.fly.mall.api.trade.domain.vo;

import com.fly.common.annotation.JsonLongId;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 交易配置 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class TradeConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonLongId
    private Long id;

    private List<String> afterSaleRefundReasons;

    private List<String> afterSaleReturnReasons;

    private Boolean deliveryExpressFreeEnabled;

    private Integer deliveryExpressFreePrice;

    private Boolean deliveryPickUpEnabled;

    private Boolean brokerageEnabled;

    private Integer brokerageEnabledCondition;

    private Integer brokerageBindMode;

    private List<String> brokeragePosterUrls;

    private Integer brokerageFirstPercent;

    private Integer brokerageSecondPercent;

    private Integer brokerageWithdrawMinPrice;

    private Integer brokerageWithdrawFeePercent;

    private Integer brokerageFrozenDays;

    private List<Integer> brokerageWithdrawTypes;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}

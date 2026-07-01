package com.fly.mall.api.trade.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 移动端 - 交易订单分页项响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class AppTradeOrderPageItemRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String no;

    private Integer type;

    private Integer status;

    private Integer productCount;

    private Boolean commentStatus;

    private LocalDateTime createTime;

    private Long payOrderId;

    private Integer payPrice;

    private Integer deliveryType;

    private List<AppTradeOrderItemRespVo> items;

    private Long combinationRecordId;
}

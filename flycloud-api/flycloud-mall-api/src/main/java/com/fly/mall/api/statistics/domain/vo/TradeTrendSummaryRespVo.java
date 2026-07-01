package com.fly.mall.api.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 交易状况统计响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class TradeTrendSummaryRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("日期")
    private LocalDate date;

    @ExcelProperty("营业额")
    private Integer turnoverPrice;

    @ExcelProperty("订单支付金额")
    private Integer orderPayPrice;

    @ExcelProperty("余额支付金额")
    private Integer walletPayPrice;

    @ExcelProperty("订单退款金额")
    private Integer afterSaleRefundPrice;

    @ExcelProperty("支付佣金金额")
    private Integer brokerageSettlementPrice;

    @ExcelProperty("充值金额")
    private Integer rechargePrice;

    @ExcelProperty("支出金额")
    private Integer expensePrice;

}

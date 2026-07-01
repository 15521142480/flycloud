package com.fly.mall.api.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 商品统计响应对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductStatisticsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("编号")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("统计日期")
    private LocalDate time;

    @ExcelProperty("商品SPU编号")
    private Long spuId;

    @ExcelProperty("商品名称")
    private String name;

    @ExcelProperty("商品封面图")
    private String picUrl;

    @ExcelProperty("浏览量")
    private Integer browseCount;

    @ExcelProperty("访客量")
    private Integer browseUserCount;

    @ExcelProperty("收藏数量")
    private Integer favoriteCount;

    @ExcelProperty("加购数量")
    private Integer cartCount;

    @ExcelProperty("下单件数")
    private Integer orderCount;

    @ExcelProperty("支付件数")
    private Integer orderPayCount;

    @ExcelProperty("支付金额")
    private Integer orderPayPrice;

    @ExcelProperty("退款件数")
    private Integer afterSaleCount;

    @ExcelProperty("退款金额")
    private Integer afterSaleRefundPrice;

    private Integer browseConvertPercent;

}

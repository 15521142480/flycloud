package com.fly.mall.api.statistics.domain.vo;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品统计 视图对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductStatisticsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDate time;

    private Long spuId;

    private Integer browseCount;

    private Integer browseUserCount;

    private Integer favoriteCount;

    private Integer cartCount;

    private Integer orderCount;

    private Integer orderPayCount;

    private Integer orderPayPrice;

    private Integer afterSaleCount;

    private Integer afterSaleRefundPrice;

    private Integer browseConvertPercent;

    private Boolean isDeleted;

    private LocalDateTime createTime;

}

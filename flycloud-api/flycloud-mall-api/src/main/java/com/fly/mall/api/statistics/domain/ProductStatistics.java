package com.fly.mall.api.statistics.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品统计 表。
 *
 * @author lxs
 * @date 2026-06-28
 */
@TableName(value = "product_statistics")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "商品统计对象", description = "商品统计表")
public class ProductStatistics extends BaseEntity {

    @TableId
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

}

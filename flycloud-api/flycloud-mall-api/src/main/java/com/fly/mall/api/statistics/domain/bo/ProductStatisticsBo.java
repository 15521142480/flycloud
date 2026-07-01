package com.fly.mall.api.statistics.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

/**
 * 商品统计 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class ProductStatisticsBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "time")
    private LocalDate time;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "browseCount")
    private Integer browseCount;

    @Schema(description = "browseUserCount")
    private Integer browseUserCount;

    @Schema(description = "favoriteCount")
    private Integer favoriteCount;

    @Schema(description = "cartCount")
    private Integer cartCount;

    @Schema(description = "orderCount")
    private Integer orderCount;

    @Schema(description = "orderPayCount")
    private Integer orderPayCount;

    @Schema(description = "orderPayPrice")
    private Integer orderPayPrice;

    @Schema(description = "afterSaleCount")
    private Integer afterSaleCount;

    @Schema(description = "afterSaleRefundPrice")
    private Integer afterSaleRefundPrice;

    @Schema(description = "browseConvertPercent")
    private Integer browseConvertPercent;

}

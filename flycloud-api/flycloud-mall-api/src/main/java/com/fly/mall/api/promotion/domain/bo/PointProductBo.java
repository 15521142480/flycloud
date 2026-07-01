package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 积分商城商品 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class PointProductBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "activityId")
    private Long activityId;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "count")
    private Integer count;

    @Schema(description = "point")
    private Integer point;

    @Schema(description = "price")
    private Integer price;

    @Schema(description = "stock")
    private Integer stock;

    @Schema(description = "activityStatus")
    private Integer activityStatus;

}

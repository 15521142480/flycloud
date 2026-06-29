package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 秒杀商品 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class SeckillProductBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "activityId")
    private Long activityId;

    @Schema(description = "configIds")
    private List<Long> configIds;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "seckillPrice")
    private Integer seckillPrice;

    @Schema(description = "stock")
    private Integer stock;

    @Schema(description = "activityStatus")
    private Integer activityStatus;

    @Schema(description = "activityStartTime")
    private LocalDateTime activityStartTime;

    @Schema(description = "activityEndTime")
    private LocalDateTime activityEndTime;

}

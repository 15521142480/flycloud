package com.fly.mall.api.domain.promotion.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 拼团商品 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class CombinationProductBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "activityId")
    private Long activityId;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "combinationPrice")
    private Integer combinationPrice;

    @Schema(description = "activityStatus")
    private Integer activityStatus;

    @Schema(description = "activityStartTime")
    private LocalDateTime activityStartTime;

    @Schema(description = "activityEndTime")
    private LocalDateTime activityEndTime;

}

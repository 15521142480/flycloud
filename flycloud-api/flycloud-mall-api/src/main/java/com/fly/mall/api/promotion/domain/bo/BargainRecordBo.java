package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 砍价记录 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class BargainRecordBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "activityId")
    private Long activityId;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "bargainFirstPrice")
    private Integer bargainFirstPrice;

    @Schema(description = "bargainPrice")
    private Integer bargainPrice;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "endTime")
    private LocalDateTime endTime;

    @Schema(description = "orderId")
    private Long orderId;

}

package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 砍价活动 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class BargainActivityBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "startTime")
    private LocalDateTime startTime;

    @Schema(description = "endTime")
    private LocalDateTime endTime;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "bargainFirstPrice")
    private Integer bargainFirstPrice;

    @Schema(description = "bargainMinPrice")
    private Integer bargainMinPrice;

    @Schema(description = "stock")
    private Integer stock;

    @Schema(description = "totalStock")
    private Integer totalStock;

    @Schema(description = "helpMaxCount")
    private Integer helpMaxCount;

    @Schema(description = "bargainCount")
    private Integer bargainCount;

    @Schema(description = "totalLimitCount")
    private Integer totalLimitCount;

    @Schema(description = "randomMinPrice")
    private Integer randomMinPrice;

    @Schema(description = "randomMaxPrice")
    private Integer randomMaxPrice;

}

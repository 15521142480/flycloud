package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 砍价助力 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class BargainHelpBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "activityId")
    private Long activityId;

    @Schema(description = "recordId")
    private Long recordId;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "reducePrice")
    private Integer reducePrice;

}

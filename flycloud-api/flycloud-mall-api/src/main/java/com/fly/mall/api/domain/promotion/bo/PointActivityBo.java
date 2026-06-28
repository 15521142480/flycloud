package com.fly.mall.api.domain.promotion.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 积分商城活动 BO。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Data
public class PointActivityBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "remark")
    private String remark;

    @Schema(description = "sort")
    private Integer sort;

    @Schema(description = "stock")
    private Integer stock;

    @Schema(description = "totalStock")
    private Integer totalStock;

}

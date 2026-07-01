package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 秒杀活动 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class SeckillActivityBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "name")
    private String name;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "remark")
    private String remark;

    @Schema(description = "startTime")
    private LocalDateTime startTime;

    @Schema(description = "endTime")
    private LocalDateTime endTime;

    @Schema(description = "sort")
    private Integer sort;

    @Schema(description = "configIds")
    private List<Long> configIds;

    @Schema(description = "totalLimitCount")
    private Integer totalLimitCount;

    @Schema(description = "singleLimitCount")
    private Integer singleLimitCount;

    @Schema(description = "stock")
    private Integer stock;

    @Schema(description = "totalStock")
    private Integer totalStock;

}

package com.fly.mall.api.promotion.domain.bo;

import com.fly.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 拼团活动 BO。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
public class CombinationActivityBo extends BaseEntity {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "spuId")
    private Long spuId;

    @Schema(description = "totalLimitCount")
    private Integer totalLimitCount;

    @Schema(description = "singleLimitCount")
    private Integer singleLimitCount;

    @Schema(description = "startTime")
    private LocalDateTime startTime;

    @Schema(description = "endTime")
    private LocalDateTime endTime;

    @Schema(description = "userSize")
    private Integer userSize;

    @Schema(description = "virtualGroup")
    private Boolean virtualGroup;

    @Schema(description = "status")
    private Integer status;

    @Schema(description = "limitDuration")
    private Integer limitDuration;

}

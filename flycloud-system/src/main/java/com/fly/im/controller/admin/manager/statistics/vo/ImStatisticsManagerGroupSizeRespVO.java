package com.fly.im.controller.admin.manager.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - IM 数据看板群规模分布项 Response VO")
@Data
@Accessors(chain = true)
public class ImStatisticsManagerGroupSizeRespVO {

    @Schema(description = "区间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1-9 人")
    private String range;

    @Schema(description = "群数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "320")
    private Long count;

}

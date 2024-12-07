package com.fly.bpm.api.domain.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Schema(description = "管理后台 - 不通过流程任务的 Request VO")
@Data
@Accessors(chain = true)
public class BpmTaskRejectReqVO implements Serializable {

    @Schema(description = "任务(工作项)编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    @Schema(description = "审批意见", requiredMode = Schema.RequiredMode.REQUIRED, example = "不错不错！")
    @NotEmpty(message = "审批意见不能为空")
    private String reason;

}

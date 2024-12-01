package com.fly.bpm.api.domain.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(description = "管理后台 - 流程任务的转办 Request VO")
@Data
@Accessors(chain = true)
public class BpmTaskTransferReqVO implements Serializable {

    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    @Schema(description = "新审批人的用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "新审批人的用户编号不能为空")
    private Long assigneeUserId;

    @Schema(description = "转办原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "做不了决定，需要你先帮忙瞅瞅")
    @NotEmpty(message = "转办原因不能为空")
    private String reason;

}

package com.fly.bpm.api.domain.vo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Schema(description = "管理后台 - 流程模型的更新 BPMN XML Request VO")
@Data
public class BpmModeUpdateBpmnReqVO implements Serializable {

    @Schema(description = "流程编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "流程编号不能为空")
    private String id;

    @Schema(description = "BPMN XML", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "BPMN XML 不能为空")
    private String bpmnXml;

}

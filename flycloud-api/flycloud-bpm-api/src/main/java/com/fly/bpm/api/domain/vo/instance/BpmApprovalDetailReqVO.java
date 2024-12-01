package com.fly.bpm.api.domain.vo.instance;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import java.io.Serializable;

@Schema(description = "管理后台 - 审批详情 Request VO")
@Data
public class BpmApprovalDetailReqVO implements Serializable {

    @Schema(description = "流程定义的编号", example = "1024")
    private String processDefinitionId;

    @Schema(description = "流程实例的编号", example = "1024")
    private String processInstanceId;

    @AssertTrue(message = "流程定义的编号和流程实例的编号不能同时为空")
    @JsonIgnore
    public boolean isValidProcessParam() {
        return StrUtil.isNotEmpty(processDefinitionId) || StrUtil.isNotEmpty(processInstanceId);
    }

}

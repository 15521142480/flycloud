package com.fly.bpm.api.domain.vo.instance;

import com.fly.bpm.api.domain.vo.process.BpmProcessDefinitionRespVO;
import com.fly.bpm.api.domain.vo.user.SysUserBpmVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 流程实例的 Response VO")
@Data
@Accessors(chain = true)
public class BpmProcessInstanceRespVO implements Serializable {

    @Schema(description = "流程实例的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String id;

    @Schema(description = "流程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    private String name;

    @Schema(description = "流程分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String category;
    @Schema(description = "流程分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "请假")
    private String categoryName;

    @Schema(description = "流程实例的状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status; // 参见 BpmProcessInstanceStatusEnum 枚举

    @Schema(description = "发起时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    @Schema(description = "持续时间", example = "1000")
    private Long durationInMillis;

    @Schema(description = "提交的表单值", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, Object> formVariables;

    @Schema(description = "业务的唯一标识-例如说，请假申请的编号", example = "1")
    private String businessKey;

    /**
     * 发起流程的用户
     */
    private SysUserBpmVO startUser;

    @Schema(description = "流程定义的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    private String processDefinitionId;
    /**
     * 流程定义
     */
    private BpmProcessDefinitionRespVO processDefinition;

    /**
     * 当前审批中的任务
     */
    private List<Task> tasks; // 仅在流程实例分页才返回

    @Schema(description = "流程任务")
    @Data
    public static class Task {

        @Schema(description = "流程任务的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
        private String id;

        @Schema(description = "任务名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
        private String name;

    }

}

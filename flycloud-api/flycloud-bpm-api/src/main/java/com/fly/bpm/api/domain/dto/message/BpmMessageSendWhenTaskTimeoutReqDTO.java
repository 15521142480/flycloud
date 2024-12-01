package com.fly.bpm.api.domain.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * BPM 发送任务审批超时 Request DTO
 */
@Data
@Accessors(chain = true)
public class BpmMessageSendWhenTaskTimeoutReqDTO {

    /**
     * 流程实例的编号
     */
    @NotEmpty(message = "流程实例的编号不能为空")
    private String processInstanceId;
    /**
     * 流程实例的名字
     */
    @NotEmpty(message = "流程实例的名字不能为空")
    private String processInstanceName;

    /**
     * 流程任务的编号
     */
    @NotEmpty(message = "流程任务的编号不能为空")
    private String taskId;
    /**
     * 流程任务的名字
     */
    @NotEmpty(message = "流程任务的名字不能为空")
    private String taskName;

    /**
     * 审批人的用户编号
     */
    @NotNull(message = "审批人的用户编号不能为空")
    private Long assigneeUserId;

}

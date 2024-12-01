package com.fly.bpm.api.domain.dto.message;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * BPM 发送流程实例被不通过 Request DTO
 */
@Data
@Accessors(chain = true)
public class BpmMessageSendWhenProcessInstanceRejectReqDTO {

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
    @NotNull(message = "发起人的用户编号")
    private Long startUserId;

    /**
     * 不通过理由
     */
    @NotEmpty(message = "不通过理由不能为空")
    private String reason;

}

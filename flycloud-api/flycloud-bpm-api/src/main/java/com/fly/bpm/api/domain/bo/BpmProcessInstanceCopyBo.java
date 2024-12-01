package com.fly.bpm.api.domain.bo;

import com.fly.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.fly.common.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * BPM 流程实例抄送业务对象
 *
 * @author fly
 * @date 2024-11-24
 */

@Data
public class BpmProcessInstanceCopyBo {

    /**
     * 编号
     */
    // @NotNull(message = "编号不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户编号，被抄送人
     */
    // @NotNull(message = "用户编号，被抄送人不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 发起流程的用户编号
     */
    // @NotNull(message = "发起流程的用户编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long startUserId;

    /**
     * 流程实例的id
     */
    // @NotBlank(message = "流程实例的id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String processInstanceId;

    /**
     * 流程实例的名字
     */
    // @NotBlank(message = "流程实例的名字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String processInstanceName;

    /**
     * 流程定义的分类
     */
    // @NotBlank(message = "流程定义的分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private String category;

    /**
     * 发起抄送的任务编号
     */
    // @NotBlank(message = "发起抄送的任务编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taskId;

    /**
     * 任务的名字
     */
    // @NotBlank(message = "任务的名字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taskName;

    /**
     * 流程活动编号
     */
    // @NotBlank(message = "流程活动编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String activityId;


    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime[] createTime;


}

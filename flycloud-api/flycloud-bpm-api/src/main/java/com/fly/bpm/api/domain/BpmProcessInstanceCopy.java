package com.fly.bpm.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import com.fly.common.domain.BaseEntity;
import lombok.experimental.Accessors;

/**
 * BPM 流程实例抄送对象 bpm_process_instance_copy
 *
 * @author fly
 * @date 2024-11-24
 */
@TableName("bpm_process_instance_copy")
@Data
//@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BpmProcessInstanceCopy extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 编号
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 用户编号，被抄送人
     */
    private Long userId;
    /**
     * 发起流程的用户编号
     */
    private Long startUserId;
    /**
     * 流程实例的id
     */
    private String processInstanceId;
    /**
     * 流程实例的名字
     */
    private String processInstanceName;
    /**
     * 流程定义的分类
     */
    private String category;
    /**
     * 发起抄送的任务编号
     */
    private String taskId;
    /**
     * 任务的名字
     */
    private String taskName;
    /**
     * 流程活动编号
     */
    private String activityId;

}

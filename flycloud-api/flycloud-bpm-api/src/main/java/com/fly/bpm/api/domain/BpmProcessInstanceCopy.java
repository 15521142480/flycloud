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
    @TableId
    private Long id;

    /**
     * 发起人 Id
     *
     * 冗余 ProcessInstance 的 startUserId 字段
     */
    private Long startUserId;

    /**
     * 流程名
     *
     * 冗余 ProcessInstance 的 name 字段
     */
    private String processInstanceName;

    /**
     * 流程实例的编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

    /**
     * 流程分类
     *
     * 冗余 ProcessInstance 的 category 字段
     */
    private String category;

    /**
     * 流程活动的编号
     * <p/>
     *
     * 冗余 {@link FlowNode#getId()}，对应 BPMN XML 节点编号
     * 原因：用于查询抄送节点的表单字段权限。因为仿钉钉/飞书的抄送节点 (ServiceTask)，没有 taskId，只有 activityId
     */
    private String activityId;

    /**
     * 流程活动的名字
     *
     * 冗余 {@link FlowNode#getName()}
     */
    private String activityName;

    /**
     * 流程活动的编号
     *
     * 关联 {@link HistoricTaskInstance#getId()}
     */
    private String taskId;

    /**
     * 用户编号（被抄送的用户编号）
     *
     * 关联 system_users 的 id 属性
     */
    private Long userId;

    /**
     * 抄送意见
     */
    private String reason;

}

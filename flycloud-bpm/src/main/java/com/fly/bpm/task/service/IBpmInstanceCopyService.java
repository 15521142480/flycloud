package com.fly.bpm.task.service;

import com.fly.bpm.api.domain.BpmProcessInstanceCopy;
import com.fly.bpm.api.domain.bo.BpmProcessInstanceCopyBo;
import com.fly.bpm.api.domain.vo.BpmProcessInstanceCopyVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import org.flowable.bpmn.model.FlowNode;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Set;

/**
 * BPM 流程实例抄送Service接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface IBpmInstanceCopyService {

    /**
     * 获得抄送的流程的分页
     *
     * @param userId 当前登录用户
     * @param bo 参数
     * @param page 分页
     * @return 抄送的分页结果
     */
    PageVo<BpmProcessInstanceCopyVo> getProcessInstanceCopyPage(Long userId, BpmProcessInstanceCopyBo bo, PageBo page);

    PageVo<BpmProcessInstanceCopy> getProcessInstanceCopyPageByEntity(Long userId, BpmProcessInstanceCopyBo bo, PageBo page);


    /**
     * 【管理员】流程实例的抄送
     *
     * @param userIds 抄送的用户编号
     * @param reason 抄送意见
     * @param taskId 流程任务编号
     */
    void createProcessInstanceCopy(Collection<Long> userIds, String reason, String taskId);

    /**
     * 【自动抄送】流程实例的抄送
     *
     * @param userIds 抄送的用户编号
     * @param reason 抄送意见
     * @param processInstanceId 流程编号
     * @param activityId 流程活动编号（对应 {@link FlowNode#getId()}）
     * @param activityName 任务编号（对应 {@link FlowNode#getName()}）
     * @param taskId 任务编号，允许空
     */
    void createProcessInstanceCopy(Collection<Long> userIds, String reason,
                                   @NotEmpty(message = "流程实例编号不能为空") String processInstanceId,
                                   @NotEmpty(message = "流程活动编号不能为空") String activityId,
                                   @NotEmpty(message = "流程活动名字不能为空") String activityName,
                                   String taskId);

}

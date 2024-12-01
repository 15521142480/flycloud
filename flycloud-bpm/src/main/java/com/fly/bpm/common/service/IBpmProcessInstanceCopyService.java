package com.fly.bpm.common.service;

import com.fly.bpm.api.domain.BpmProcessInstanceCopy;
import com.fly.bpm.api.domain.bo.BpmProcessInstanceCopyBo;
import com.fly.bpm.api.domain.vo.BpmProcessInstanceCopyVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;

import java.util.Collection;
import java.util.Set;

/**
 * BPM 流程实例抄送Service接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface IBpmProcessInstanceCopyService {

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
     * 流程实例的抄送
     *
     * @param userIds 抄送的用户编号
     * @param taskId 流程任务编号
     */
    void createProcessInstanceCopy(Collection<Long> userIds, String taskId);

    /**
     * 流程实例的抄送
     *
     * @param userIds 抄送的用户编号
     * @param processInstanceId 流程编号
     * @param activityId 流程活动编号 id (对应 BPMN XML 节点 Id)
     * // TODO fly这个 taskId 是不是可以不要了
     * @param taskId 任务编号
     * @param taskName 任务名称
     */
    void createProcessInstanceCopy(Collection<Long> userIds, String processInstanceId, String activityId, String taskId, String taskName);


    // TODO 重点在 review 下
    /**
     * 通过流程实例和流程活动编号获取抄送人的 Id
     *
     * @param processInstanceId 流程实例 Id
     * @param activityId 流程活动编号 Id
     * @return 抄送人 Ids
     */
    Set<Long> getCopyUserIds(String processInstanceId, String activityId);

}

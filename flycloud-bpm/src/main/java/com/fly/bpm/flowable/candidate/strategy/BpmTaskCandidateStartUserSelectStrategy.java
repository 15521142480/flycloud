package com.fly.bpm.flowable.candidate.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.fly.bpm.flowable.utils.BpmnModelUtils;
import com.fly.bpm.flowable.utils.FlowableUtils;
import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.system.api.feign.ISysUserApi;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 发起人自选 {@link BpmTaskCandidateUserStrategy} 实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmTaskCandidateStartUserSelectStrategy extends BpmTaskCandidateAbstractStrategy {

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private BpmInstanceService processInstanceService;

    public BpmTaskCandidateStartUserSelectStrategy(ISysUserApi sysUserApi) {
        super(sysUserApi);
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.START_USER_SELECT;
    }

    @Override
    public void validateParam(String param) {}

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Assert.notNull(processInstance, "流程实例({})不能为空", execution.getProcessInstanceId());
        Map<String, List<Long>> startUserSelectAssignees = FlowableUtils.getStartUserSelectAssignees(processInstance);
        Assert.notNull(startUserSelectAssignees, "流程实例({}) 的发起人自选审批人不能为空",
                execution.getProcessInstanceId());
        // 获得审批人
        List<Long> assignees = startUserSelectAssignees.get(execution.getCurrentActivityId());
        Set<Long> users = new LinkedHashSet<>(assignees);
        removeDisableUsers(users);
        return users;
    }

    @Override
    public Set<Long> calculateUsers(Long startUserId, ProcessInstance processInstance, String activityId, String param) {
        if (processInstance == null) {
            return Collections.emptySet();
        }
        Map<String, List<Long>> startUserSelectAssignees = FlowableUtils.getStartUserSelectAssignees(processInstance);
        Assert.notNull(startUserSelectAssignees, "流程实例({}) 的发起人自选审批人不能为空", processInstance.getId());
        // 获得审批人
        List<Long> assignees = startUserSelectAssignees.get(activityId);
        Set<Long> users = new LinkedHashSet<>(assignees);
        removeDisableUsers(users);
        return users;
    }

    @Override
    public boolean isParamRequired() {
        return false;
    }

    /**
     * 获得发起人自选审批人的 UserTask 列表
     *
     * @param bpmnModel BPMN 模型
     * @return UserTask 列表
     */
    public static List<UserTask> getStartUserSelectUserTaskList(BpmnModel bpmnModel) {
        if (bpmnModel == null) {
            return null;
        }
        List<UserTask> userTaskList = BpmnModelUtils.getBpmnModelElements(bpmnModel, UserTask.class);
        if (CollUtil.isEmpty(userTaskList)) {
            return null;
        }
        userTaskList.removeIf(userTask -> !Objects.equals(BpmnModelUtils.parseCandidateStrategy(userTask),
                BpmTaskCandidateStrategyEnum.START_USER_SELECT.getStrategy()));
        return userTaskList;
    }

}

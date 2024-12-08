package com.fly.bpm.flowable.candidate.strategy.other;

import cn.hutool.core.lang.Assert;
import com.fly.bpm.api.domain.BpmProcessDefinitionInfo;
import com.fly.bpm.common.service.BpmProcessDefinitionService;
import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.bpm.flowable.utils.BpmnModelUtils;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.enums.bpm.BpmUserTaskAssignEmptyHandlerTypeEnum;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 审批人为空 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 *
 */
@Component
public class BpmTaskCandidateAssignEmptyStrategy implements BpmTaskCandidateStrategy {

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private BpmProcessDefinitionService processDefinitionService;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.ASSIGN_EMPTY;
    }

    @Override
    public void validateParam(String param) {
    }

    @Override
    public Set<Long> calculateUsersByTask(DelegateExecution execution, String param) {
        return getCandidateUsers(execution.getProcessDefinitionId(), execution.getCurrentFlowElement());
    }

    @Override
    public Set<Long> calculateUsersByActivity(BpmnModel bpmnModel, String activityId, String param,
                                              Long startUserId, String processDefinitionId, Map<String, Object> processVariables) {
        FlowElement flowElement = BpmnModelUtils.getFlowElementById(bpmnModel, activityId);
        return getCandidateUsers(processDefinitionId, flowElement);
    }

    private Set<Long> getCandidateUsers(String processDefinitionId, FlowElement flowElement) {
        // 情况一：指定人员审批
        Integer assignEmptyHandlerType = BpmnModelUtils.parseAssignEmptyHandlerType(flowElement);
        if (Objects.equals(assignEmptyHandlerType, BpmUserTaskAssignEmptyHandlerTypeEnum.ASSIGN_USER.getType())) {
            return new HashSet<>(BpmnModelUtils.parseAssignEmptyHandlerUserIds(flowElement));
        }

        // 情况二：流程管理员
        if (Objects.equals(assignEmptyHandlerType, BpmUserTaskAssignEmptyHandlerTypeEnum.ASSIGN_ADMIN.getType())) {
            BpmProcessDefinitionInfo processDefinition = processDefinitionService.getProcessDefinitionInfo(processDefinitionId);
            Assert.notNull(processDefinition, "流程定义({})不存在", processDefinitionId);
            return new HashSet<>(processDefinition.getManagerUserIds());
        }

        // 都不满足，还是返回空
        return new HashSet<>();
    }

}
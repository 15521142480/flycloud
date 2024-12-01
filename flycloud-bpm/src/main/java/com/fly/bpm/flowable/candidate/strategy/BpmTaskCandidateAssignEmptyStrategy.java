package com.fly.bpm.flowable.candidate.strategy;

import cn.hutool.core.lang.Assert;
import com.fly.bpm.api.domain.BpmProcessDefinitionInfo;
import com.fly.bpm.common.service.BpmProcessDefinitionService;
import com.fly.bpm.flowable.utils.BpmnModelUtils;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.enums.bpm.BpmUserTaskAssignEmptyHandlerTypeEnum;
import com.fly.system.api.feign.ISysUserApi;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 审批人为空 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateAssignEmptyStrategy extends BpmTaskCandidateAbstractStrategy {

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private BpmProcessDefinitionService processDefinitionService;

    public BpmTaskCandidateAssignEmptyStrategy(ISysUserApi sysUserProvider) {
        super(sysUserProvider);
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.ASSIGN_EMPTY;
    }

    @Override
    public void validateParam(String param) {
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        
        // 情况一：指定人员审批
        Integer assignEmptyHandlerType = BpmnModelUtils.parseAssignEmptyHandlerType(execution.getCurrentFlowElement());
        if (Objects.equals(assignEmptyHandlerType, BpmUserTaskAssignEmptyHandlerTypeEnum.ASSIGN_USER.getType())) {
            Set<Long> users = new HashSet<>(BpmnModelUtils.parseAssignEmptyHandlerUserIds(execution.getCurrentFlowElement()));
            removeDisableUsers(users);
            return users;
        }

        // 情况二：流程管理员
        if (Objects.equals(assignEmptyHandlerType, BpmUserTaskAssignEmptyHandlerTypeEnum.ASSIGN_ADMIN.getType())) {
            BpmProcessDefinitionInfo processDefinition = processDefinitionService.getProcessDefinitionInfo(execution.getProcessDefinitionId());
            Assert.notNull(processDefinition, "流程定义({})不存在", execution.getProcessDefinitionId());
            return new HashSet<>(processDefinition.getManagerUserIds());
        }

        // 都不满足，还是返回空
        return new HashSet<>();
    }

}
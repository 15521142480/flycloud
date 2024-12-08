package com.fly.bpm.flowable.candidate.strategy.other;

import cn.hutool.core.convert.Convert;
import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.bpm.flowable.utils.FlowableUtils;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 流程表达式 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 *
 */
@Component
public class BpmTaskCandidateExpressionStrategy implements BpmTaskCandidateStrategy {

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.EXPRESSION;
    }

    @Override
    public void validateParam(String param) {
        // do nothing 因为它基本做不了校验
    }

    @Override
    public Set<Long> calculateUsersByTask(DelegateExecution execution, String param) {
        Object result = FlowableUtils.getExpressionValue(execution, param);
        return Convert.toSet(Long.class, result);
    }

    @Override
    public Set<Long> calculateUsersByActivity(BpmnModel bpmnModel, String activityId, String param,
                                              Long startUserId, String processDefinitionId, Map<String, Object> processVariables) {
        Object result = FlowableUtils.getExpressionValue(processVariables, param);
        return Convert.toSet(Long.class, result);
    }

}
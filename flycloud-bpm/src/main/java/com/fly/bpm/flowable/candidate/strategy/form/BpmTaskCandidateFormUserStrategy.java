package com.fly.bpm.flowable.candidate.strategy.form;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * 表单内用户字段 {@link com.fly.bpm.flowable.candidate.strategy.BpmTaskCandidateUserStrategy} 实现类
 *
 *
 */
@Component
public class BpmTaskCandidateFormUserStrategy implements BpmTaskCandidateStrategy {

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.FORM_USER;
    }

    @Override
    public void validateParam(String param) {
        Assert.notEmpty(param, "表单内用户字段不能为空");
    }

    @Override
    public Set<Long> calculateUsersByTask(DelegateExecution execution, String param) {
        Object result = execution.getVariable(param);
        return Convert.toSet(Long.class, result);
    }

    @Override
    public Set<Long> calculateUsersByActivity(BpmnModel bpmnModel, String activityId,
                                              String param, Long startUserId, String processDefinitionId,
                                              Map<String, Object> processVariables) {
        Object result = processVariables == null ? null : processVariables.get(param);
        return Convert.toSet(Long.class, result);
    }

}

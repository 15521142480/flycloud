package com.fly.bpm.flowable.candidate.strategy;

import cn.hutool.core.convert.Convert;
import com.fly.bpm.flowable.utils.FlowableUtils;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.system.api.feign.ISysUserApi;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 流程表达式 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 * @author 芋道源码
 */
@Component
public class BpmTaskCandidateExpressionStrategy extends BpmTaskCandidateAbstractStrategy {

    public BpmTaskCandidateExpressionStrategy(ISysUserApi sysUserApi) {
        super(sysUserApi);
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.EXPRESSION;
    }

    @Override
    public void validateParam(String param) {
        // do nothing 因为它基本做不了校验
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        Object result = FlowableUtils.getExpressionValue(execution, param);
        Set<Long> users = Convert.toSet(Long.class, result);
        removeDisableUsers(users);
        return users;
    }

}
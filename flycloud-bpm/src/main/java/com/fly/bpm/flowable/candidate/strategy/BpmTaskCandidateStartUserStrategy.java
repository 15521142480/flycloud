package com.fly.bpm.flowable.candidate.strategy;

import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.collection.SetUtils;
import com.fly.system.api.feign.ISysUserApi;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 发起人自己 {@link BpmTaskCandidateUserStrategy} 实现类
 * <p>
 * 适合场景：用于需要发起人信息复核等场景
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateStartUserStrategy extends BpmTaskCandidateAbstractStrategy {

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private BpmInstanceService processInstanceService;

    public BpmTaskCandidateStartUserStrategy(ISysUserApi sysUserApi) {
        super(sysUserApi);
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.START_USER;
    }

    @Override
    public void validateParam(String param) {
    }

    @Override
    public boolean isParamRequired() {
        return false;
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Set<Long> users =  SetUtils.asSet(Long.valueOf(processInstance.getStartUserId()));
        removeDisableUsers(users);
        return users;
    }

    @Override
    public Set<Long> calculateUsers(Long startUserId, ProcessInstance processInstance, String activityId, String param) {
        Set<Long> users = SetUtils.asSet(startUserId);
        removeDisableUsers(users);
        return users;
    }

}

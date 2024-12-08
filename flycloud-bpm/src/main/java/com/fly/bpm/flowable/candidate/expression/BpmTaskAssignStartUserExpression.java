package com.fly.bpm.flowable.candidate.expression;

import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.common.utils.collection.SetUtils;
import com.fly.common.utils.number.NumberUtils;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 分配给发起人审批的 Expression 流程表达式
 *
 *
 */
@Component
public class BpmTaskAssignStartUserExpression {

    @Resource
    private BpmInstanceService processInstanceService;

    /**
     * 计算审批的候选人
     *
     * @param execution 流程执行实体
     * @return 发起人
     */
    public Set<Long> calculateUsers(ExecutionEntityImpl execution) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtils.parseLong(processInstance.getStartUserId());
        return SetUtils.asSet(startUserId);
    }

}

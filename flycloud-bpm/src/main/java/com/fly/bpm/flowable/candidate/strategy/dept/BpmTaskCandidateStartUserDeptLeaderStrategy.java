package com.fly.bpm.flowable.candidate.strategy.dept;

import cn.hutool.core.lang.Assert;
import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.collection.SetUtils;
import com.fly.common.utils.number.NumberUtils;
import com.fly.system.api.domain.vo.SysDeptVo;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 发起人的部门负责人, 可以是上级部门负责人 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateStartUserDeptLeaderStrategy extends AbstractBpmTaskCandidateDeptLeaderStrategy {

    @Resource
    @Lazy // 避免循环依赖
    private BpmInstanceService processInstanceService;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.START_USER_DEPT_LEADER;
    }

    @Override
    public void validateParam(String param) {
        // 参数是部门的层级
        Assert.isTrue(Integer.parseInt(param) > 0, "部门的层级必须大于 0");
    }

    @Override
    public Set<Long> calculateUsersByTask(DelegateExecution execution, String param) {
        // 获得流程发起人
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtils.parseLong(processInstance.getStartUserId());
        // 获取发起人的部门负责人
        return getStartUserDeptLeader(startUserId, param);
    }

    @Override
    public Set<Long> calculateUsersByActivity(BpmnModel bpmnModel, String activityId, String param,
                                              Long startUserId, String processDefinitionId, Map<String, Object> processVariables) {
        // 获取发起人的部门负责人
        return getStartUserDeptLeader(startUserId, param);
    }

    private Set<Long> getStartUserDeptLeader(Long startUserId, String param) {

        int level = Integer.parseInt(param); // 参数是部门的层级
        SysDeptVo dept = super.getStartUserDept(startUserId);
        if (dept == null) {
            return new HashSet<>();
        }
        Long deptLeaderId = super.getAssignLevelDeptLeaderId(dept, level);
        return deptLeaderId != null ? SetUtils.asSet(deptLeaderId) : new HashSet<>();
    }

}

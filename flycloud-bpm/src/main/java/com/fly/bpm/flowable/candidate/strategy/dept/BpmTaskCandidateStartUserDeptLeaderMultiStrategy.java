package com.fly.bpm.flowable.candidate.strategy.dept;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
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
 * 发起人连续多级部门的负责人 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateStartUserDeptLeaderMultiStrategy extends AbstractBpmTaskCandidateDeptLeaderStrategy {

    @Resource
    @Lazy
    private BpmInstanceService processInstanceService;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.START_USER_DEPT_LEADER_MULTI;
    }

    @Override
    public void validateParam(String param) {
        int level = Integer.parseInt(param); // 参数是部门的层级
        Assert.isTrue(level > 0, "部门的层级必须大于 0");
    }

    @Override
    public Set<Long> calculateUsersByTask(DelegateExecution execution, String param) {
        int level = Integer.parseInt(param); // 参数是部门的层级
        // 获得流程发起人
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtils.parseLong(processInstance.getStartUserId());
        // 获取发起人的 multi 部门负责人
        SysDeptVo dept = super.getStartUserDept(startUserId);
        if (dept == null) {
            return new HashSet<>();
        }
        return super.getMultiLevelDeptLeaderIds(ListUtil.toList(dept.getId()), level);
    }

    @Override
    public Set<Long> calculateUsersByActivity(BpmnModel bpmnModel, String activityId, String param,
                                              Long startUserId, String processDefinitionId, Map<String, Object> processVariables) {
        int level = Integer.parseInt(param); // 参数是部门的层级
        SysDeptVo dept = super.getStartUserDept(startUserId);
        if (dept == null) {
            return new HashSet<>();
        }
        return super.getMultiLevelDeptLeaderIds(ListUtil.toList(dept.getId()), level);
    }

}

package com.fly.bpm.flowable.candidate.strategy;

import cn.hutool.core.lang.Assert;
import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.collection.SetUtils;
import com.fly.common.utils.number.NumberUtils;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.feign.ISysDeptApi;
import com.fly.system.api.feign.ISysUserApi;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;


/**
 * 发起人的部门负责人, 可以是上级部门负责人 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateStartUserDeptLeaderStrategy extends BpmTaskCandidateAbstractDeptLeaderStrategy {

    @Resource
    @Lazy // 避免循环依赖
    private BpmInstanceService processInstanceService;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.START_USER_DEPT_LEADER;
    }

    public BpmTaskCandidateStartUserDeptLeaderStrategy(ISysUserApi sysUserApi, ISysDeptApi sysDeptApi) {
        super(sysUserApi, sysDeptApi);
    }

    @Override
    public void validateParam(String param) {
        // 参数是部门的层级
        Assert.isTrue(Integer.parseInt(param) > 0, "部门的层级必须大于 0");
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        // 获得流程发起人
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtils.parseLong(processInstance.getStartUserId());
        // 获取发起人的部门负责人
        Set<Long> users = getStartUserDeptLeader(startUserId, param);
        removeDisableUsers(users);
        return users;
    }

    @Override
    public Set<Long> calculateUsers(Long startUserId, ProcessInstance processInstance, String activityId, String param) {
        // 获取发起人的部门负责人
        Set<Long> users =  getStartUserDeptLeader(startUserId, param);
        removeDisableUsers(users);
        return users;
    }

    private Set<Long> getStartUserDeptLeader(Long startUserId, String param) {

        SysDeptVo dept = getStartUserDept(startUserId);
        if (dept == null) {
            return new HashSet<>();
        }
        Long deptLeaderId = getAssignLevelDeptLeaderId(dept, Integer.valueOf(param)); // 参数是部门的层级
        return deptLeaderId != null ? SetUtils.asSet(deptLeaderId) : new HashSet<>();
    }

    /**
     * 获取发起人的部门
     *
     * @param startUserId 发起人 Id
     */
    protected SysDeptVo getStartUserDept(Long startUserId) {

        SysUser startUser = sysUserProvider.getUserById(startUserId).getCheckedData();
        if (startUser.getDeptId() == null) { // 找不到部门
            return null;
        }
        return sysDeptProvider.getDeptById(startUser.getDeptId()).getCheckedData();
    }

}

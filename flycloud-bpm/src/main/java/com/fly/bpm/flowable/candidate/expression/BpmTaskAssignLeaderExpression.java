package com.fly.bpm.flowable.candidate.expression;

import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.common.utils.collection.SetUtils;
import com.fly.common.utils.number.NumberUtils;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.feign.ISysDeptApi;
import com.fly.system.api.feign.ISysUserApi;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * 分配给发起人的 Leader 审批的 Expression 流程表达式
 * 目前 Leader 的定义是，发起人所在部门的 Leader
 *
 */
@Component
public class BpmTaskAssignLeaderExpression {

    @Resource
    private ISysUserApi sysUserApi;
    @Resource
    private ISysDeptApi sysDeptApi;

    @Resource
    private BpmInstanceService processInstanceService;
    @Autowired
    private ISysDeptApi iSysDeptApi;

    /**
     * 计算审批的候选人
     *
     * @param execution 流程执行实体
     * @param level 指定级别
     * @return 指定级别的领导
     */
    public Set<Long> calculateUsers(DelegateExecution execution, int level) {
        Assert.isTrue(level > 0, "level 必须大于 0");
        // 获得发起人
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtils.parseLong(processInstance.getStartUserId());
        // 获得对应 leve 的部门
        SysDeptVo dept = null;
        for (int i = 0; i < level; i++) {
            // 获得 level 对应的部门
            if (dept == null) {
                dept = getStartUserDept(startUserId);
                if (dept == null) { // 找不到发起人的部门，所以无法使用该规则
                    return emptySet();
                }
            } else {
                SysDeptVo parentDept = iSysDeptApi.getDeptById(dept.getParentId()).getCheckedData();
                if (parentDept == null) { // 找不到父级部门，所以只好结束寻找。原因是：例如说，级别比较高的人，所在部门层级比较少
                    break;
                }
                dept = parentDept;
            }
        }
        return dept.getLeaderUserId() != null ? SetUtils.asSet(dept.getLeaderUserId()) : emptySet();
    }

    private SysDeptVo getStartUserDept(Long startUserId) {

        SysUserVo startUser = sysUserApi.getUserById(startUserId).getCheckedData();
        if (startUser.getDeptId() == null) { // 找不到部门，所以无法使用该规则
            return null;
        }
        return iSysDeptApi.getDeptById(startUser.getDeptId()).getCheckedData();
    }

}

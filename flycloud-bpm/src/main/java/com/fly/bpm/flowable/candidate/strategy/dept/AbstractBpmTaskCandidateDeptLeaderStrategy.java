package com.fly.bpm.flowable.candidate.strategy.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.feign.ISysDeptApi;
import com.fly.system.api.feign.ISysUserApi;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 部门的负责人 {@link BpmTaskCandidateStrategy} 抽象类
 *
 * @author lxs
 */
public abstract class AbstractBpmTaskCandidateDeptLeaderStrategy implements BpmTaskCandidateStrategy {

    @Resource
    protected ISysDeptApi deptApi;
    @Resource
    protected ISysUserApi sysUserApi;

    /**
     * 获得指定层级的部门负责人，只有第 level 的负责人
     *
     * @param dept 指定部门
     * @param level 第几级
     * @return 部门负责人的编号
     */
    protected Long getAssignLevelDeptLeaderId(SysDeptVo dept, Integer level) {

        Assert.isTrue(level > 0, "level 必须大于 0");
        if (dept == null) {
            return null;
        }
        SysDeptVo currentDept = dept;
        for (int i = 1; i < level; i++) {
            SysDeptVo parentDept = deptApi.getDeptById(currentDept.getParentId()).getCheckedData();
            if (parentDept == null) { // 找不到父级部门，到了最高级。返回最高级的部门负责人
                break;
            }
            currentDept = parentDept;
        }
        return currentDept.getLeaderUserId();
    }

    /**
     * 获得连续层级的部门负责人，包含 [1, level] 的负责人
     *
     * @param deptIds 指定部门编号数组
     * @param level 最大层级
     * @return 连续部门负责人 Id
     */
    protected Set<Long> getMultiLevelDeptLeaderIds(List<Long> deptIds, Integer level) {
        Assert.isTrue(level > 0, "level 必须大于 0");
        if (CollUtil.isEmpty(deptIds)) {
            return new HashSet<>();
        }
        Set<Long> deptLeaderIds = new LinkedHashSet<>(); // 保证有序
        for (Long deptId : deptIds) {
            SysDeptVo dept = deptApi.getDeptById(deptId).getCheckedData();
            for (int i = 0; i < level; i++) {
                if (dept.getLeaderUserId() != null) {
                    deptLeaderIds.add(dept.getLeaderUserId());
                }
                SysDeptVo parentDept = deptApi.getDeptById(dept.getParentId()).getCheckedData();
                if (parentDept == null) { // 找不到父级部门. 已经到了最高层级了
                    break;
                }
                dept = parentDept;
            }
        }
        return deptLeaderIds;
    }

    /**
     * 获取发起人的部门
     *
     * @param startUserId 发起人 Id
     */
    protected SysDeptVo getStartUserDept(Long startUserId) {

        SysUserVo startUser = sysUserApi.getUserById(startUserId).getCheckedData();
        if (startUser.getDeptId() == null) { // 找不到部门
            return null;
        }
        return deptApi.getDeptById(startUser.getDeptId()).getCheckedData();
    }

}

package com.fly.bpm.flowable.candidate.strategy.dept;

import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.domain.vo.SysUserVo;
import com.fly.system.api.feign.ISysDeptApi;
import com.fly.system.api.feign.ISysUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


/**
 * 部门的成员 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 *
 */
@Component
public class BpmTaskCandidateDeptMemberStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private ISysDeptApi deptApi;
    @Resource
    private ISysUserApi sysUserApi;
    @Autowired
    private ISysUserApi iSysUserApi;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.DEPT_MEMBER;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> deptIds = StringUtils.splitToLongSet(param);
        deptApi.validateDeptByIds(deptIds).checkError();
    }

    @Override
    public Set<Long> calculateUsers(String param) {

        Set<Long> deptIds = StringUtils.splitToLongSet(param);
        List<SysUserVo> users = iSysUserApi.getUserListByIds(deptIds).getCheckedData();
        return CollectionUtils.convertSet(users, SysUserVo::getId);
    }

}
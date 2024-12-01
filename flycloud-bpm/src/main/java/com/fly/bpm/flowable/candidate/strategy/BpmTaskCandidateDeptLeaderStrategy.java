package com.fly.bpm.flowable.candidate.strategy;

import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.feign.ISysDeptApi;
import com.fly.system.api.feign.ISysUserApi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


/**
 * 部门的负责人 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateDeptLeaderStrategy extends BpmTaskCandidateAbstractStrategy {

    private final ISysDeptApi sysDeptApi;


    public BpmTaskCandidateDeptLeaderStrategy(ISysUserApi sysUserApi, ISysDeptApi sysDeptApi) {
        super(sysUserApi);
        this.sysDeptApi = sysDeptApi;
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.DEPT_LEADER;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> deptIds = StringUtils.splitToLongSet(param);
        sysDeptApi.validateDeptByIds(deptIds);
    }

    @Override
    public Set<Long> calculateUsers(String param) {
        Set<Long> deptIds = StringUtils.splitToLongSet(param);
        List<SysDeptVo> deptList = sysDeptApi.getDeptListByIds(deptIds).getCheckedData();
        return CollectionUtils.convertSet(deptList, SysDeptVo::getLeaderUserId);
    }

}
package com.fly.bpm.flowable.candidate.strategy.dept;

import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.feign.ISysDeptApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


/**
 * 部门的负责人 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateDeptLeaderStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private ISysDeptApi deptApi;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.DEPT_LEADER;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> deptIds = StringUtils.splitToLongSet(param);
        deptApi.validateDeptByIds(deptIds).checkError();
    }

    @Override
    public Set<Long> calculateUsers(String param) {

        Set<Long> deptIds = StringUtils.splitToLongSet(param);
        List<SysDeptVo> deptVoList = deptApi.getDeptListByIds(deptIds).getCheckedData();
        return CollectionUtils.convertSet(deptVoList, SysDeptVo::getLeaderUserId);
    }

}
package com.fly.bpm.flowable.candidate.strategy.user;

import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.system.domain.vo.SysUserVo;
import com.fly.system.api.system.feign.ISysRoleApi;
import com.fly.system.api.system.feign.ISysUserApi;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Set;

/**
 * 角色 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateRoleStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private ISysRoleApi sysRoleApi;
    @Resource
    private ISysUserApi sysUserApi;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.ROLE;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> roleIds = StringUtils.splitToLongSet(param);
        sysRoleApi.validateRoleByIds(roleIds);
    }

    @Override
    public Set<Long> calculateUsers(String param) {
        Set<Long> roleIds = StringUtils.splitToLongSet(param);
        List<SysUserVo> userList = sysUserApi.getUserListByRoleIds(roleIds).getCheckedData();
        return CollectionUtils.convertSet(userList, SysUserVo::getId);
    }

}
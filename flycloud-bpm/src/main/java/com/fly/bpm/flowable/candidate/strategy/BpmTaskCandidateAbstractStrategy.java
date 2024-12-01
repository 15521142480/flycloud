package com.fly.bpm.flowable.candidate.strategy;

import cn.hutool.core.collection.CollUtil;
import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.common.enums.StatusEnum;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.feign.ISysUserApi;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * {@link BpmTaskCandidateStrategy} 抽象类
 *
 * @author lxs
 */
public abstract class BpmTaskCandidateAbstractStrategy implements BpmTaskCandidateStrategy {

    protected ISysUserApi sysUserProvider;

    public BpmTaskCandidateAbstractStrategy(ISysUserApi sysUserProvider) {
        this.sysUserProvider = sysUserProvider;
    }

    @Override
    public void removeDisableUsers(Set<Long> users) {

        if (CollUtil.isEmpty(users)) {
            return;
        }
        Map<Long, SysUser> userMap = sysUserProvider.getUserMapByIds(users);
        users.removeIf(id -> {
            SysUser user = userMap.get(id);
            return user == null || !Objects.equals(StatusEnum.ENABLE.getStatus(), user.getStatus());
        });
    }

}

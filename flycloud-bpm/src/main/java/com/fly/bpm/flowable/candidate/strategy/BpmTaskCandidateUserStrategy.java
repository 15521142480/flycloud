package com.fly.bpm.flowable.candidate.strategy;

import cn.hutool.core.text.StrPool;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.system.api.feign.ISysUserApi;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 用户 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateUserStrategy extends BpmTaskCandidateAbstractStrategy {

    public BpmTaskCandidateUserStrategy(ISysUserApi sysUserApi) {
        super(sysUserApi);
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.USER;
    }

    @Override
    public void validateParam(String param) {
        super.sysUserProvider.validateDeptByIds(StringUtils.splitToLongSet(param)).checkError();
    }

    @Override
    public Set<Long> calculateUsers(String param) {
        return new LinkedHashSet<>(StringUtils.splitToLong(param, StrPool.COMMA));
    }

}
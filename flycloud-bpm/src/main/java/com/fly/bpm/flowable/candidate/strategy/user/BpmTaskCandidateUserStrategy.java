package com.fly.bpm.flowable.candidate.strategy.user;

import cn.hutool.core.text.StrPool;
import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.system.api.feign.ISysUserApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedHashSet;

/**
 * 用户 {@link BpmTaskCandidateStrategy} 实现类
 *
 *
 */
@Component
public class BpmTaskCandidateUserStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private ISysUserApi sysUserApi;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.USER;
    }

    @Override
    public void validateParam(String param) {
        sysUserApi.validateDeptByIds(StringUtils.splitToLongSet(param)).checkError();
    }

    @Override
    public LinkedHashSet<Long> calculateUsers(String param) {
        return new LinkedHashSet<>(StringUtils.splitToLong(param, StrPool.COMMA));
    }

}
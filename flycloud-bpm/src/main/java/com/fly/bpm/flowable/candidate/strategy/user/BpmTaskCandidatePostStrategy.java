package com.fly.bpm.flowable.candidate.strategy.user;

import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.system.domain.vo.SysUserVo;
import com.fly.system.api.system.feign.ISysPostApi;
import com.fly.system.api.system.feign.ISysUserApi;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Set;


/**
 * 岗位 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 *
 */
@Component
public class BpmTaskCandidatePostStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private ISysPostApi sysPostApi;
    @Resource
    private ISysUserApi iSysUserApi;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.POST;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> postIds = StringUtils.splitToLongSet(param);
        sysPostApi.validatePostByIds(postIds);
    }

    @Override
    public Set<Long> calculateUsers(String param) {
        Set<Long> postIds = StringUtils.splitToLongSet(param);
        List<SysUserVo> userList = iSysUserApi.getUserListByPostIds(postIds).getCheckedData();
        return CollectionUtils.convertSet(userList, SysUserVo::getId);
    }

}
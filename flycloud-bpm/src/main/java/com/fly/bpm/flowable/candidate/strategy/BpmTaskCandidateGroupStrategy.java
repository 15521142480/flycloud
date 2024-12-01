package com.fly.bpm.flowable.candidate.strategy;

import com.fly.bpm.api.domain.BpmUserGroup;
import com.fly.bpm.common.service.IBpmUserGroupService;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.feign.ISysUserApi;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 用户组 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateGroupStrategy extends BpmTaskCandidateAbstractStrategy {

    private final IBpmUserGroupService userGroupService;

    public BpmTaskCandidateGroupStrategy(ISysUserApi sysUserApi, IBpmUserGroupService userGroupService) {
        super(sysUserApi);
        this.userGroupService = userGroupService;
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.USER_GROUP;
    }

    @Override
    public void validateParam(String param) {

//        Set<Long> groupIds = StringUtils.splitToLongSet(param);
//        userGroupService.getUserGroupList(groupIds);
    }

    @Override
    public Set<Long> calculateUsers(String param) {

        return new HashSet<>();

//        Set<Long> groupIds = StringUtils.splitToLongSet(param);
//        List<BpmUserGroup> groups = userGroupService.getUserGroupList(groupIds);
//        return CollectionUtils.convertSetByFlatMap(groups, BpmUserGroup::getUserIds, Collection::stream);
    }

}
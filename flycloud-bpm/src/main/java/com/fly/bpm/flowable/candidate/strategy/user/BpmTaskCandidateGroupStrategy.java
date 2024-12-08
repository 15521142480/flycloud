package com.fly.bpm.flowable.candidate.strategy.user;

import com.fly.bpm.api.domain.vo.BpmUserGroupVo;
import com.fly.bpm.common.service.IBpmUserGroupService;
import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 用户组 {@link BpmTaskCandidateGroupStrategy} 实现类
 *
 *
 */
@Component
public class BpmTaskCandidateGroupStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private IBpmUserGroupService userGroupService;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.USER_GROUP;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> groupIds = StringUtils.splitToLongSet(param);
        userGroupService.validUserGroupsByIds(groupIds);
    }

    @Override
    public Set<Long> calculateUsers(String param) {

        Set<Long> groupIds = StringUtils.splitToLongSet(param);
        List<BpmUserGroupVo> groups = userGroupService.queryListByIds(groupIds);
        return CollectionUtils.convertSetByFlatMap(groups, BpmUserGroupVo::getUserIds, Collection::stream);
    }

}
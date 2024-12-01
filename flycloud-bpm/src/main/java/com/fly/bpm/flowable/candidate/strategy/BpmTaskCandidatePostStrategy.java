//package com.fly.bpm.flowable.candidate.strategy;
//
//import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
//import com.fly.common.utils.StringUtils;
//import com.fly.system.api.feign.ISysUserApi;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Set;
//
//import static com.fly.common.utils.collection.CollectionUtils.convertSet;
//
///**
// * 岗位 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
// *
// * @author lxs
// */
//@Component
//public class BpmTaskCandidatePostStrategy extends BpmTaskCandidateAbstractStrategy {
//
//    private final PostApi postApi;
//
//    public BpmTaskCandidatePostStrategy(ISysUserApi sysUserApi, PostApi postApi) {
//        super(sysUserApi);
//        this.postApi = postApi;
//    }
//
//    @Override
//    public BpmTaskCandidateStrategyEnum getStrategy() {
//        return BpmTaskCandidateStrategyEnum.POST;
//    }
//
//    @Override
//    public void validateParam(String param) {
//        Set<Long> postIds = StringUtils.splitToLongSet(param);
//        postApi.validPostList(postIds);
//    }
//
//    @Override
//    public Set<Long> calculateUsers(String param) {
//        Set<Long> postIds = StrUtils.splitToLongSet(param);
//        List<AdminUserRespDTO> users = adminUserApi.getUserListByPostIds(postIds).getCheckedData();
//        return convertSet(users, AdminUserRespDTO::getId);
//    }
//
//}
//package com.fly.bpm.flowable.candidate.strategy.user;
//
//import com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Set;
//
//
///**
// * 岗位 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
// *
// *
// */
//@Component
//public class BpmTaskCandidatePostStrategy implements BpmTaskCandidateStrategy {
//
//    @Resource
//    private PostApi postApi;
//    @Resource
//    private AdminUserApi adminUserApi;
//
//    @Override
//    public BpmTaskCandidateStrategyEnum getStrategy() {
//        return BpmTaskCandidateStrategyEnum.POST;
//    }
//
//    @Override
//    public void validateParam(String param) {
//        Set<Long> postIds = StrUtils.splitToLongSet(param);
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
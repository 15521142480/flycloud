//package com.fly.bpm.flowable.candidate.strategy;
//
//import cn.iocoder.yudao.framework.common.util.string.StrUtils;
//import cn.iocoder.yudao.module.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
//import cn.iocoder.yudao.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
//import cn.iocoder.yudao.module.system.api.permission.PermissionApi;
//import cn.iocoder.yudao.module.system.api.permission.RoleApi;
//import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Set;
//
///**
// * 角色 {@link BpmTaskCandidateStrategy} 实现类
// *
// * @author lxs
// */
//@Component
//public class BpmTaskCandidateRoleStrategy extends BpmTaskCandidateAbstractStrategy {
//
//    @Resource
//    private RoleApi roleApi;
//    @Resource
//    private PermissionApi permissionApi;
//
//    public BpmTaskCandidateRoleStrategy(AdminUserApi adminUserApi) {
//        super(adminUserApi);
//    }
//
//    @Override
//    public BpmTaskCandidateStrategyEnum getStrategy() {
//        return BpmTaskCandidateStrategyEnum.ROLE;
//    }
//
//    @Override
//    public void validateParam(String param) {
//        Set<Long> roleIds = StrUtils.splitToLongSet(param);
//        roleApi.validRoleList(roleIds);
//    }
//
//    @Override
//    public Set<Long> calculateUsers(String param) {
//        Set<Long> roleIds = StrUtils.splitToLongSet(param);
//        return permissionApi.getUserRoleIdListByRoleIds(roleIds).getCheckedData();
//    }
//
//}
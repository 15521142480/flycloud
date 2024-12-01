package com.fly.bpm.flowable.candidate.strategy;

import cn.hutool.core.lang.Assert;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.utils.StringUtils;
import com.fly.system.api.feign.ISysDeptApi;
import com.fly.system.api.feign.ISysUserApi;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 连续多级部门的负责人 {@link com.fly.bpm.flowable.candidate.BpmTaskCandidateStrategy} 实现类
 *
 * @author lxs
 */
@Component
public class BpmTaskCandidateDeptLeaderMultiStrategy extends BpmTaskCandidateAbstractDeptLeaderStrategy {

    public BpmTaskCandidateDeptLeaderMultiStrategy(ISysUserApi sysUserProvider, ISysDeptApi sysDeptProvider) {
        super(sysUserProvider, sysDeptProvider);
    }

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.MULTI_DEPT_LEADER_MULTI;
    }

    @Override
    public void validateParam(String param) {

        // 参数格式: | 分隔：1）左边为部门（多个部门用 , 分隔）。2）右边为部门层级
        String[] params = param.split("\\|");
        Assert.isTrue(params.length == 2, "参数格式不匹配");
        sysDeptProvider.validateDeptByIds(StringUtils.splitToLong(params[0], ",")).checkError();
        Assert.isTrue(Integer.parseInt(params[1]) > 0, "部门层级必须大于 0");
    }

    @Override
    public Set<Long> calculateUsers(String param) {
        String[] params = param.split("\\|");
        return super.getMultiLevelDeptLeaderIds(StringUtils.splitToLong(params[0], ","), Integer.valueOf(params[1]));
    }

}

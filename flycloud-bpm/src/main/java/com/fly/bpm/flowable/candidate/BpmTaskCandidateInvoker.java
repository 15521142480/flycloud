package com.fly.bpm.flowable.candidate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fly.bpm.task.service.BpmInstanceService;
import com.fly.bpm.flowable.utils.BpmnModelUtils;
import com.fly.common.constant.bpm.BpmTaskCandidateStrategyEnum;
import com.fly.common.database.annotation.DataPermission;
import com.fly.common.enums.StatusEnum;
import com.fly.common.enums.bpm.BpmUserTaskApproveTypeEnum;
import com.fly.common.enums.bpm.BpmUserTaskAssignStartUserHandlerTypeEnum;
import com.fly.common.utils.ObjectUtils;
import com.fly.system.api.domain.SysUser;
import com.fly.system.api.feign.ISysUserApi;
import com.google.common.annotations.VisibleForTesting;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import java.util.*;

import static com.fly.common.exception.utils.ServiceExceptionUtils.exception;
import static com.fly.common.constant.bpm.ErrorCodeConstants.MODEL_DEPLOY_FAIL_TASK_CANDIDATE_NOT_CONFIG;

/**
 * {@link BpmTaskCandidateStrategy} 的调用者，用于调用对应的策略，实现任务的候选人的计算
 *
 */
//@Slf4j
//@Component
public class BpmTaskCandidateInvoker {


    private final Map<BpmTaskCandidateStrategyEnum, BpmTaskCandidateStrategy> strategyMap = new HashMap<>();

    private final ISysUserApi iSysUserProvider;


    /**
     * 构造返回ISysUserProvider
     *
     * @param strategyList
     * @param sysUserProvider
    */
    public BpmTaskCandidateInvoker(List<BpmTaskCandidateStrategy> strategyList,
                                   ISysUserApi sysUserProvider) {
        strategyList.forEach(strategy -> {
            BpmTaskCandidateStrategy oldStrategy = strategyMap.put(strategy.getStrategy(), strategy);
            Assert.isNull(oldStrategy, "策略(%s) 重复", strategy.getStrategy());
        });
        this.iSysUserProvider = sysUserProvider;
    }


    /**
     * 校验流程模型的任务分配规则全部都配置了
     * 目的：如果有规则未配置，会导致流程任务找不到负责人，进而流程无法进行下去！
     *
     * @param bpmnBytes BPMN XML
     */
    public void validateBpmnConfig(byte[] bpmnBytes) {
        BpmnModel bpmnModel = BpmnModelUtils.getBpmnModel(bpmnBytes);
        assert bpmnModel != null;
        List<UserTask> userTaskList = BpmnModelUtils.getBpmnModelElements(bpmnModel, UserTask.class);
        // 遍历所有的 UserTask，校验审批人配置
        userTaskList.forEach(userTask -> {
            // 1.1 非人工审批，无需校验审批人配置
            Integer approveType = BpmnModelUtils.parseApproveType(userTask);
            if (ObjectUtils.equalsAny(approveType,
                    BpmUserTaskApproveTypeEnum.AUTO_APPROVE.getType(),
                    BpmUserTaskApproveTypeEnum.AUTO_REJECT.getType())) {
                return;
            }
            // 1.2 非空校验
            Integer strategy = BpmnModelUtils.parseCandidateStrategy(userTask);
            String param = BpmnModelUtils.parseCandidateParam(userTask);
            if (strategy == null) {
                throw exception(MODEL_DEPLOY_FAIL_TASK_CANDIDATE_NOT_CONFIG, userTask.getName());
            }
            BpmTaskCandidateStrategy candidateStrategy = getCandidateStrategy(strategy);
            if (candidateStrategy.isParamRequired() && StrUtil.isBlank(param)) {
                throw exception(MODEL_DEPLOY_FAIL_TASK_CANDIDATE_NOT_CONFIG, userTask.getName());
            }
            // 2. 具体策略校验
            getCandidateStrategy(strategy).validateParam(param);
        });
    }

    /**
     * 计算任务的候选人
     *
     * @param execution 执行任务
     * @return 用户编号集合
     */
    @DataPermission(enable = false) // 忽略数据权限，避免因为过滤，导致找不到候选人
    public Set<Long> calculateUsers(DelegateExecution execution) {
        // 审批类型非人工审核时，不进行计算候选人。原因是：后续会自动通过、不通过
        Integer approveType = BpmnModelUtils.parseApproveType(execution.getCurrentFlowElement());
        if (ObjectUtils.equalsAny(approveType,
                BpmUserTaskApproveTypeEnum.AUTO_APPROVE.getType(),
                BpmUserTaskApproveTypeEnum.AUTO_REJECT.getType())) {
            return new HashSet<>();
        }

        Integer strategy = BpmnModelUtils.parseCandidateStrategy(execution.getCurrentFlowElement());
        String param = BpmnModelUtils.parseCandidateParam(execution.getCurrentFlowElement());
        // 1.1 计算任务的候选人
        Set<Long> userIds = getCandidateStrategy(strategy).calculateUsers(execution, param);
        removeDisableUsers(userIds);
        // 1.2 移除被禁用的用户
        removeDisableUsers(userIds);

        // 2. 候选人为空时，根据“审批人为空”的配置补充
        if (CollUtil.isEmpty(userIds)) {
            userIds = getCandidateStrategy(BpmTaskCandidateStrategyEnum.ASSIGN_EMPTY.getStrategy())
                    .calculateUsers(execution, param);
            // ASSIGN_EMPTY 策略，不需要移除被禁用的用户。原因是，再移除，可能会出现更没审批人了！！！
        }

        // 3. 移除发起人的用户
        removeStartUserIfSkip(execution, userIds);
        return userIds;
    }

    @VisibleForTesting
    void removeDisableUsers(Set<Long> assigneeUserIds) {
        if (CollUtil.isEmpty(assigneeUserIds)) {
            return;
        }
        Map<Long, SysUser> userMap = iSysUserProvider.getUserMapByIds(assigneeUserIds);
        assigneeUserIds.removeIf(id -> {
            SysUser user = userMap.get(id);
            return user == null || !Objects.equals(StatusEnum.ENABLE.getStatus(), user.getStatus());
        });
    }

    /**
     * 如果“审批人与发起人相同时”，配置了 SKIP 跳过，则移除发起人
     *
     * 注意：如果只有一个候选人，则不处理，避免无法审批
     *
     * @param execution 执行中的任务
     * @param assigneeUserIds 当前分配的候选人
     */
    @VisibleForTesting
    void removeStartUserIfSkip(DelegateExecution execution, Set<Long> assigneeUserIds) {
        if (CollUtil.size(assigneeUserIds) <= 1) {
            return;
        }
        Integer assignStartUserHandlerType = BpmnModelUtils.parseAssignStartUserHandlerType(execution.getCurrentFlowElement());
        if (ObjectUtil.notEqual(assignStartUserHandlerType, BpmUserTaskAssignStartUserHandlerTypeEnum.SKIP.getType())) {
            return;
        }
        ProcessInstance processInstance = SpringUtil.getBean(BpmInstanceService.class)
                .getProcessInstance(execution.getProcessInstanceId());
        Assert.notNull(processInstance, "流程实例({}) 不存在", execution.getProcessInstanceId());
        assigneeUserIds.remove(Long.valueOf(processInstance.getStartUserId()));
    }


    /**
     * 验证流程有无选择审批方式, 根据不同审批方式走不同审批策略
     */
    public BpmTaskCandidateStrategy getCandidateStrategy(Integer strategy) {
        BpmTaskCandidateStrategyEnum strategyEnum = BpmTaskCandidateStrategyEnum.valueOf(strategy);
        Assert.notNull(strategyEnum, "策略(%s) 不存在", strategy);
        BpmTaskCandidateStrategy strategyObj = strategyMap.get(strategyEnum);
        Assert.notNull(strategyObj, "策略(%s) 不存在", strategy);
        return strategyObj;
    }

}
package com.fly.bpm.oa.listener;

import com.fly.bpm.oa.service.IBpmOaLeaveService;
import com.fly.common.constant.CommonConstants;
import com.fly.common.constant.bpm.BpmnVariableConstants;
import com.fly.common.enums.bpm.BpmProcessInstanceStatusEnum;
import com.fly.common.exception.BpmException;
import com.fly.common.utils.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 请假流程结束的执行监听器
 *
 * @author: lxs
 * @date: 2026/3/3
 */
@Component("bpmOaLeaveInstanceEndListener")
public class BpmOaLeaveInstanceEndListener implements ExecutionListener {

    @Resource
    private IBpmOaLeaveService leaveService;

    @Resource
    private RuntimeService runtimeService;


    @Override
    public void notify(DelegateExecution delegateExecution) {

        String businessKey = delegateExecution.getProcessInstanceBusinessKey();
        String instanceId = delegateExecution.getProcessInstanceId();
        if (StringUtils.isBlank(businessKey) || StringUtils.isBlank(businessKey)) {
            throw new BpmException(CommonConstants.FAIL, "businessKey || instanceId为空！");
        }

        // todo 2026/03/04 如下，涉及流程重要数据需要修改多处，设计上可以优化下；如抽出一个自定义的流程实例表记录流程实例的最新信息，而不是放在流程变量里？
        // 流程实例状态变量
        runtimeService.setVariable(instanceId, BpmnVariableConstants.PROCESS_INSTANCE_VARIABLE_STATUS, BpmProcessInstanceStatusEnum.APPROVE.getStatus());

        // 业务表流程状态
        leaveService.updateLeaveStatus(Long.valueOf(businessKey), BpmProcessInstanceStatusEnum.APPROVE.getStatus());
    }
}

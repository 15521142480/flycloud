package com.fly.bpm.oa.listener;

import com.fly.bpm.oa.service.IBpmOaLeaveService;
import com.fly.common.constant.CommonConstants;
import com.fly.common.enums.bpm.BpmProcessInstanceStatusEnum;
import com.fly.common.exception.BpmException;
import com.fly.common.utils.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 请假单的结果的监听器实现类
 *
 * @author: lxs
 * @date: 2026/3/3
 */
@Component("bpmOaLeaveInstanceEndListener")
public class BpmOaLeaveInstanceEndListener implements ExecutionListener {

    @Resource
    private IBpmOaLeaveService leaveService;


    @Override
    public void notify(DelegateExecution delegateExecution) {

        String businessKey = delegateExecution.getProcessInstanceBusinessKey();
        if (StringUtils.isBlank(businessKey)) {
            throw new BpmException(CommonConstants.FAIL, "流程businessKey为空！");
        }
        leaveService.updateLeaveStatus(Long.valueOf(businessKey), BpmProcessInstanceStatusEnum.APPROVE.getStatus());
    }
}

package com.fly.bpm.flowable.listener;

import cn.hutool.core.collection.CollUtil;
import com.fly.bpm.common.service.IBpmProcessInstanceCopyService;
import com.fly.bpm.flowable.candidate.BpmTaskCandidateInvoker;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 处理抄送用户的 {@link JavaDelegate} 的实现类
 *
 * 目前只有快搭模式的【抄送节点】使用
 *
 */
@Component(BpmCopyTaskDelegate.BEAN_NAME)
//@RequiredArgsConstructor
public class BpmCopyTaskDelegate implements JavaDelegate {

    public static final String BEAN_NAME = "bpmCopyTaskDelegate";

//    private final BpmTaskCandidateInvoker taskCandidateInvoker;
    @Resource
    private BpmTaskCandidateInvoker taskCandidateInvoker;

    @Resource
    private IBpmProcessInstanceCopyService processInstanceCopyService;



    /**
     * 执行抄送
     *
     * @param execution
    */
    @Override
    public void execute(DelegateExecution execution) {

        // 1. 获得抄送人
        Set<Long> userIds = taskCandidateInvoker.calculateUsers(execution);
        if (CollUtil.isEmpty(userIds)) {
            return;
        }

        // 2. 执行抄送
        FlowElement currentFlowElement = execution.getCurrentFlowElement();
        processInstanceCopyService.createProcessInstanceCopy(userIds, execution.getProcessInstanceId(),
                currentFlowElement.getId(), null, currentFlowElement.getName());
    }

}

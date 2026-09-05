package com.fly.pay.event;

import com.fly.pay.service.IPayNotifyService;
import com.fly.system.api.pay.domain.PayNotifyTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 支付通知任务事件监听器。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PayNotifyTaskEventListener {

    private final IPayNotifyService notifyService;

    /**
     * 业务事务(创建pay_notify_task)提交后异步发送首次通知，避免回调查询到尚未提交的数据。
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onTaskEvent(PayNotifyTaskEvent event) {
        PayNotifyTask task = notifyService.getNotifyTask(event.taskId());
        if (task == null) {
            log.warn("[onTaskEvent][支付通知任务({})不存在]", event.taskId());
            return;
        }
        notifyService.executeNotify(task);
    }

}

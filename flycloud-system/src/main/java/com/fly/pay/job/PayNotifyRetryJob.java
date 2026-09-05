package com.fly.pay.job;

import com.fly.pay.service.IPayNotifyService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 支付通知失败重试任务
 *
 * note 方案：先由 PayNotifyTaskEventListener 监听器保证实时支付回调，再由此处的定时支付回调兜底
 *  定时支付回调 -> 通过不断扫描待通知的 PayNotifyTaskDO 记录，回调业务线的回调接口
 */
@Slf4j
@Component
@RequiredArgsConstructor
//@ConditionalOnProperty(prefix = "flycloud.pay.notify", name = "retry-enabled",
//        havingValue = "true", matchIfMissing = true)
public class PayNotifyRetryJob {

    private static final String LOCK_KEY = "flycloud:pay:notify:retry";

    private final IPayNotifyService notifyService;
    private final RedissonClient redissonClient;

    /**
     * 扫描到期通知；分布式锁保证多实例部署时只有一个实例执行
     *
     * todo 由于服务器资源问题没有部署XxlJob服务，此处采用备用方案，后续需调整
     *
     */
//    @Scheduled(fixedDelayString = "${flycloud.pay.notify.retry-interval:10}", timeUnit = TimeUnit.SECONDS)
    @Scheduled(fixedDelay = 120, timeUnit = TimeUnit.SECONDS) // 备用方案
//    @XxlJob("payNotifyRetryJob") // 主方案
    public void execute() {
        RLock lock = redissonClient.getLock(LOCK_KEY);
        if (!lock.tryLock()) {
            return;
        }
        try {
            int count = notifyService.executeNotify();
            if (count > 0) {
                log.info("[execute][本次执行支付通知任务 {} 个]", count);
            }
        } catch (Exception e) {
            log.error("[execute][执行支付通知重试任务失败]", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}

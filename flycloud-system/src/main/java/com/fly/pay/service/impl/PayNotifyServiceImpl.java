package com.fly.pay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.json.JsonUtils;
import com.fly.pay.enums.PayNotifyStatusEnum;
import com.fly.pay.enums.PayNotifyTypeEnum;
import com.fly.pay.mapper.PayAppMapper;
import com.fly.pay.mapper.PayNotifyLogMapper;
import com.fly.pay.mapper.PayNotifyTaskMapper;
import com.fly.pay.mapper.PayOrderMapper;
import com.fly.pay.mapper.PayRefundMapper;
import com.fly.pay.mapper.PayTransferMapper;
import com.fly.pay.service.IPayNotifyService;
import com.fly.system.api.pay.domain.PayApp;
import com.fly.system.api.pay.domain.PayNotifyLog;
import com.fly.system.api.pay.domain.PayNotifyTask;
import com.fly.system.api.pay.domain.PayOrder;
import com.fly.system.api.pay.domain.PayRefund;
import com.fly.system.api.pay.domain.PayTransfer;
import com.fly.system.api.pay.domain.bo.PayNotifyTaskBo;
import com.fly.system.api.pay.domain.vo.PayNotifyLogVo;
import com.fly.system.api.pay.domain.vo.PayNotifyTaskDetailVo;
import com.fly.system.api.pay.domain.vo.PayNotifyTaskVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 支付通知 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PayNotifyServiceImpl implements IPayNotifyService {

    private static final int NOTIFY_TIMEOUT_MILLIS = 120_000;

    private final PayNotifyTaskMapper notifyTaskMapper;
    private final PayNotifyLogMapper notifyLogMapper;
    private final PayOrderMapper payOrderMapper;
    private final PayRefundMapper payRefundMapper;
    private final PayTransferMapper payTransferMapper;
    private final PayAppMapper payAppMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPayNotifyTask(Integer type, Long dataId) {

        PayNotifyTask task = new PayNotifyTask();
        task.setType(type);
        task.setDataId(dataId);
        task.setStatus(PayNotifyStatusEnum.WAITING.getStatus());
        task.setNextNotifyTime(LocalDateTime.now());
        task.setNotifyTimes(0);
        task.setMaxNotifyTimes(PayNotifyTask.NOTIFY_FREQUENCY.length + 1);
        task.setCreateBy(String.valueOf(UserUtils.getCurUserId()));
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());

        fillNotifyTaskBusinessInfo(task);
        if (StringUtils.isBlank(task.getNotifyUrl())) {
            log.info("[createPayNotifyTask][type({}) dataId({}) 未配置 notifyUrl，跳过通知任务创建]", type, dataId);
            return;
        }
        notifyTaskMapper.insert(task);
        executeAfterCommit(() -> executeNotifyAsync(task));
    }

    /**
     * 填充通知任务关联的业务信息。
     */
    private void fillNotifyTaskBusinessInfo(PayNotifyTask task) {
        if (Objects.equals(task.getType(), PayNotifyTypeEnum.ORDER.getType())) {
            PayOrder order = payOrderMapper.selectById(task.getDataId());
            if (order == null) {
                throw new ServiceException("支付订单不存在");
            }
            task.setAppId(order.getAppId());
            task.setNotifyUrl(order.getNotifyUrl());
            task.setMerchantOrderId(order.getMerchantOrderId());
        } else if (Objects.equals(task.getType(), PayNotifyTypeEnum.REFUND.getType())) {
            PayRefund refund = payRefundMapper.selectById(task.getDataId());
            if (refund == null) {
                throw new ServiceException("支付退款单不存在");
            }
            task.setAppId(refund.getAppId());
            task.setNotifyUrl(refund.getNotifyUrl());
            task.setMerchantOrderId(refund.getMerchantOrderId());
            task.setMerchantRefundId(refund.getMerchantRefundId());
        } else if (Objects.equals(task.getType(), PayNotifyTypeEnum.TRANSFER.getType())) {
            PayTransfer transfer = payTransferMapper.selectById(task.getDataId());
            if (transfer == null) {
                throw new ServiceException("支付转账单不存在");
            }
            task.setAppId(transfer.getAppId());
            task.setNotifyUrl(transfer.getNotifyUrl());
            task.setMerchantTransferId(transfer.getMerchantTransferId());
        } else {
            throw new ServiceException("未知的支付通知类型");
        }
    }

    @Override
    public int executeNotify() {
        List<PayNotifyTask> tasks = notifyTaskMapper.selectListByNotify();
        if (CollUtil.isEmpty(tasks)) {
            return 0;
        }
        tasks.forEach(this::executeNotify);
        return tasks.size();
    }

    @Async
    public void executeNotifyAsync(PayNotifyTask task) {
        executeNotify(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeNotify(PayNotifyTask task) {
        PayNotifyTask dbTask = notifyTaskMapper.selectById(task.getId());
        if (dbTask == null || Objects.equals(dbTask.getStatus(), PayNotifyStatusEnum.SUCCESS.getStatus())) {
            return;
        }
        R<?> invokeResult = null;
        Throwable invokeException = null;
        try {
            invokeResult = executeNotifyInvoke(dbTask);
        } catch (Throwable e) {
            invokeException = e;
        }
        Integer newStatus = processNotifyResult(dbTask, invokeResult, invokeException);
        PayNotifyLog log = new PayNotifyLog();
        log.setTaskId(dbTask.getId());
        log.setNotifyTimes((dbTask.getNotifyTimes() == null ? 0 : dbTask.getNotifyTimes()) + 1);
        log.setStatus(newStatus);
        log.setResponse(invokeException != null ? ExceptionUtil.getRootCauseMessage(invokeException)
                : JsonUtils.toJsonString(invokeResult));
        notifyLogMapper.insert(log);
    }

    /**
     * 执行 HTTP 通知。
     */
    private R<?> executeNotifyInvoke(PayNotifyTask task) {
        Object request = buildNotifyRequest(task);
        try (HttpResponse response = HttpUtil.createPost(task.getNotifyUrl())
                .body(JsonUtils.toJsonString(request))
                .timeout(NOTIFY_TIMEOUT_MILLIS)
                .execute()) {
            return JsonUtils.parseObject(response.body(), R.class);
        }
    }

    /**
     * 构建通知请求体。
     */
    private Object buildNotifyRequest(PayNotifyTask task) {
        Map<String, Object> request = new HashMap<>();
        if (Objects.equals(task.getType(), PayNotifyTypeEnum.ORDER.getType())) {
            request.put("merchantOrderId", task.getMerchantOrderId());
            request.put("payOrderId", task.getDataId());
        } else if (Objects.equals(task.getType(), PayNotifyTypeEnum.REFUND.getType())) {
            request.put("merchantOrderId", task.getMerchantOrderId());
            request.put("merchantRefundId", task.getMerchantRefundId());
            request.put("payRefundId", task.getDataId());
        } else if (Objects.equals(task.getType(), PayNotifyTypeEnum.TRANSFER.getType())) {
            request.put("merchantTransferId", task.getMerchantTransferId());
            request.put("payTransferId", task.getDataId());
        }
        return request;
    }

    /**
     * 处理并更新通知结果。
     */
    private Integer processNotifyResult(PayNotifyTask task, R<?> invokeResult, Throwable invokeException) {

        int nextNotifyTimes = (task.getNotifyTimes() == null ? 0 : task.getNotifyTimes()) + 1;
        PayNotifyTask updateTask = new PayNotifyTask();
        updateTask.setId(task.getId());
        updateTask.setLastExecuteTime(LocalDateTime.now());
        updateTask.setNotifyTimes(nextNotifyTimes);
        updateTask.setUpdateBy(UserUtils.getCurUserIdStr());
        updateTask.setUpdateTime(LocalDateTime.now());

        if (invokeResult != null && invokeResult.isSuccess()) {
            updateTask.setStatus(PayNotifyStatusEnum.SUCCESS.getStatus());
            notifyTaskMapper.updateById(updateTask);
            return updateTask.getStatus();
        }
        if (nextNotifyTimes >= PayNotifyTask.NOTIFY_FREQUENCY.length) {
            updateTask.setStatus(PayNotifyStatusEnum.FAILURE.getStatus());
            notifyTaskMapper.updateById(updateTask);
            return updateTask.getStatus();
        }
        updateTask.setNextNotifyTime(LocalDateTime.now()
                .plus(Duration.ofSeconds(PayNotifyTask.NOTIFY_FREQUENCY[nextNotifyTimes])));
        updateTask.setStatus(invokeException != null ? PayNotifyStatusEnum.REQUEST_FAILURE.getStatus()
                : PayNotifyStatusEnum.REQUEST_SUCCESS.getStatus());
        notifyTaskMapper.updateById(updateTask);
        return updateTask.getStatus();
    }

    @Override
    public PayNotifyTask getNotifyTask(Long id) {
        return notifyTaskMapper.selectById(id);
    }

    @Override
    public PayNotifyTaskDetailVo getNotifyTaskDetail(Long id) {
        PayNotifyTask task = getNotifyTask(id);
        if (task == null) {
            return null;
        }
        PayNotifyTaskDetailVo detailVo = BeanUtils.toBean(task, PayNotifyTaskDetailVo.class);
        PayApp app = task.getAppId() == null ? null : payAppMapper.selectById(task.getAppId());
        if (app != null) {
            detailVo.setAppName(app.getName());
        }
        detailVo.setLogs(BeanUtils.toBean(getNotifyLogList(id), PayNotifyLogVo.class));
        return detailVo;
    }

    @Override
    public PageVo<PayNotifyTaskVo> queryPageList(PayNotifyTaskBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PayNotifyTask> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayNotifyTask::getId);
        Page<PayNotifyTaskVo> page = notifyTaskMapper.selectVoPage(pageBo.build(), lqw);
        fillAppName(page.getRecords());
        PageVo<PayNotifyTaskVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 构建通知任务查询条件。
     */
    private LambdaQueryWrapper<PayNotifyTask> buildQueryWrapper(PayNotifyTaskBo bo) {
        LambdaQueryWrapper<PayNotifyTask> lqw = new LambdaQueryWrapper<>();
        lqw.eq(PayNotifyTask::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getAppId() != null, PayNotifyTask::getAppId, bo.getAppId());
        lqw.eq(bo.getType() != null, PayNotifyTask::getType, bo.getType());
        lqw.eq(bo.getDataId() != null, PayNotifyTask::getDataId, bo.getDataId());
        lqw.eq(bo.getStatus() != null, PayNotifyTask::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getMerchantOrderId()), PayNotifyTask::getMerchantOrderId, bo.getMerchantOrderId());
        lqw.eq(StringUtils.isNotBlank(bo.getMerchantRefundId()), PayNotifyTask::getMerchantRefundId, bo.getMerchantRefundId());
        lqw.eq(StringUtils.isNotBlank(bo.getMerchantTransferId()), PayNotifyTask::getMerchantTransferId, bo.getMerchantTransferId());
        if (bo.getCreateTime() != null && bo.getCreateTime().length == 2) {
            lqw.between(PayNotifyTask::getCreateTime, bo.getCreateTime()[0], bo.getCreateTime()[1]);
        }
        return lqw;
    }

    /**
     * 补充支付应用名称。
     */
    private void fillAppName(List<PayNotifyTaskVo> records) {
        if (CollUtil.isEmpty(records)) {
            return;
        }
        List<Long> appIds = records.stream().map(PayNotifyTaskVo::getAppId).filter(Objects::nonNull).distinct().toList();
        if (CollUtil.isEmpty(appIds)) {
            return;
        }
        Map<Long, PayApp> appMap = payAppMapper.selectBatchIds(appIds).stream()
                .collect(Collectors.toMap(PayApp::getId, Function.identity(), (a, b) -> a));
        records.forEach(record -> {
            PayApp app = appMap.get(record.getAppId());
            if (app != null) {
                record.setAppName(app.getName());
            }
        });
    }

    @Override
    public List<PayNotifyLog> getNotifyLogList(Long taskId) {
        return notifyLogMapper.selectListByTaskId(taskId);
    }

    /**
     * 事务提交后执行任务，避免通知早于业务数据落库。
     */
    private void executeAfterCommit(Runnable runnable) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            runnable.run();
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                runnable.run();
            }
        });
    }

}

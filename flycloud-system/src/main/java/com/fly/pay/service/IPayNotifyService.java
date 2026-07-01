package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.PayNotifyLog;
import com.fly.system.api.pay.domain.PayNotifyTask;
import com.fly.system.api.pay.domain.bo.PayNotifyTaskBo;
import com.fly.system.api.pay.domain.vo.PayNotifyTaskDetailVo;
import com.fly.system.api.pay.domain.vo.PayNotifyTaskVo;

import java.util.List;

/**
 * 支付通知 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IPayNotifyService {

    /**
     * 创建支付通知任务。
     */
    void createPayNotifyTask(Integer type, Long dataId);

    /**
     * 执行到期通知任务。
     */
    int executeNotify();

    /**
     * 执行单个通知任务。
     */
    void executeNotify(PayNotifyTask task);

    /**
     * 查询通知任务。
     */
    PayNotifyTask getNotifyTask(Long id);

    /**
     * 查询通知任务详情。
     */
    PayNotifyTaskDetailVo getNotifyTaskDetail(Long id);

    /**
     * 分页查询通知任务。
     */
    PageVo<PayNotifyTaskVo> queryPageList(PayNotifyTaskBo bo, PageBo pageBo);

    /**
     * 查询通知日志列表。
     */
    List<PayNotifyLog> getNotifyLogList(Long taskId);

}

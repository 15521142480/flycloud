package com.fly.pay.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.pay.enums.PayNotifyStatusEnum;
import com.fly.system.api.pay.domain.PayNotifyTask;
import com.fly.system.api.pay.domain.vo.PayNotifyTaskVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付通知任务 Mapper。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface PayNotifyTaskMapper extends BaseMapperPlus<PayNotifyTaskMapper, PayNotifyTask, PayNotifyTaskVo> {

    /**
     * 查询当前需要执行的通知任务。
     */
    default List<PayNotifyTask> selectListByNotify() {
        return selectList(new LambdaQueryWrapper<PayNotifyTask>()
                .in(PayNotifyTask::getStatus,
                        PayNotifyStatusEnum.WAITING.getStatus(),
                        PayNotifyStatusEnum.REQUEST_SUCCESS.getStatus(),
                        PayNotifyStatusEnum.REQUEST_FAILURE.getStatus())
                .le(PayNotifyTask::getNextNotifyTime, LocalDateTime.now()));
    }

}

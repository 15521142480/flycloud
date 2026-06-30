package com.fly.pay.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.system.api.pay.domain.PayNotifyLog;
import com.fly.system.api.pay.domain.vo.PayNotifyLogVo;

import java.util.List;

/**
 * 支付通知日志 Mapper。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface PayNotifyLogMapper extends BaseMapperPlus<PayNotifyLogMapper, PayNotifyLog, PayNotifyLogVo> {

    /**
     * 查询任务的通知日志。
     */
    default List<PayNotifyLog> selectListByTaskId(Long taskId) {
        return selectList(new LambdaQueryWrapper<PayNotifyLog>()
                .eq(PayNotifyLog::getTaskId, taskId)
                .orderByAsc(PayNotifyLog::getId));
    }

}

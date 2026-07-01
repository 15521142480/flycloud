package com.fly.pay.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.system.api.pay.domain.PayDemoOrder;
import com.fly.system.api.pay.domain.vo.PayDemoOrderVo;

/**
 * 支付示例订单 Mapper。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface PayDemoOrderMapper extends BaseMapperPlus<PayDemoOrderMapper, PayDemoOrder, PayDemoOrderVo> {

    /**
     * 按支付状态乐观更新示例订单。
     */
    default int updateByIdAndPayStatus(Long id, Boolean payStatus, PayDemoOrder updateObj) {
        return update(updateObj, Wrappers.<PayDemoOrder>lambdaUpdate()
                .eq(PayDemoOrder::getId, id)
                .eq(PayDemoOrder::getPayStatus, payStatus)
                .eq(PayDemoOrder::getIsDeleted, false));
    }

}

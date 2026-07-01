package com.fly.pay.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.system.api.pay.domain.PayDemoWithdraw;
import com.fly.system.api.pay.domain.vo.PayDemoWithdrawVo;

/**
 * 支付示例提现单 Mapper。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface PayDemoWithdrawMapper extends BaseMapperPlus<PayDemoWithdrawMapper, PayDemoWithdraw, PayDemoWithdrawVo> {

    /**
     * 按提现状态乐观更新示例提现单。
     */
    default int updateByIdAndStatus(Long id, Integer status, PayDemoWithdraw updateObj) {
        return update(updateObj, Wrappers.<PayDemoWithdraw>lambdaUpdate()
                .eq(PayDemoWithdraw::getId, id)
                .eq(PayDemoWithdraw::getStatus, status)
                .eq(PayDemoWithdraw::getIsDeleted, false));
    }

}

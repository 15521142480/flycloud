package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.bo.PayDemoWithdrawCreateReqBo;
import com.fly.system.api.pay.domain.vo.PayDemoWithdrawVo;

/**
 * 支付示例提现单 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IPayDemoWithdrawService {

    /**
     * 创建支付示例提现单。
     */
    Long createDemoWithdraw(PayDemoWithdrawCreateReqBo createReqBo);

    /**
     * 发起支付示例提现单转账。
     */
    Long transferDemoWithdraw(Long id, Long userId, String userIp);

    /**
     * 分页查询支付示例提现单。
     */
    PageVo<PayDemoWithdrawVo> getDemoWithdrawPage(PageBo pageBo);

    /**
     * 更新支付示例提现单的转账状态。
     */
    void updateDemoWithdrawTransferred(Long id, Long payTransferId);

}

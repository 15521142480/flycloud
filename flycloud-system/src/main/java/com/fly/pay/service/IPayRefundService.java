package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.bo.PayRefundBo;
import com.fly.system.api.pay.domain.vo.PayRefundVo;

import java.util.List;
import java.util.Map;

/**
 * 支付退款单 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IPayRefundService {
    PayRefundVo queryById(Long id);
    PageVo<PayRefundVo> queryPageList(PayRefundBo bo, PageBo pageBo);
    List<PayRefundVo> queryList(PayRefundBo bo);

    /**
     * 支付渠道退款回调后更新退款单。
     */
    void notifyRefund(Long channelId, Map<String, String> params, String body, Map<String, String> headers);
}

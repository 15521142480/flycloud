package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.bo.PayRefundBo;
import com.fly.system.api.pay.domain.vo.PayRefundVo;

/**
 * 支付退款单 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IPayRefundService {
    PayRefundVo queryById(Long id);
    PageVo<PayRefundVo> queryPageList(PayRefundBo bo, PageBo pageBo);
}

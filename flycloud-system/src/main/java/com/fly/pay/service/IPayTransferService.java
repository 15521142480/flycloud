package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.bo.PayTransferBo;
import com.fly.system.api.pay.domain.vo.PayTransferVo;

/**
 * 支付转账单 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IPayTransferService {
    PayTransferVo queryById(Long id);
    PageVo<PayTransferVo> queryPageList(PayTransferBo bo, PageBo pageBo);
}

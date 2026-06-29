package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.bo.AppPayOrderSubmitReqVo;
import com.fly.system.api.pay.domain.bo.PayOrderBo;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import com.fly.system.api.pay.domain.vo.AppPayOrderSubmitRespVo;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;

import java.util.List;

/**
 * 支付订单 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IPayOrderService {

    /**
     * 创建支付订单。
     */
    Long createPayOrder(PayOrderCreateReqDto createReqDto);

    /**
     * 根据编号查询支付订单。
     */
    PayOrderRespVo getOrder(Long id);

    /**
     * 根据支付订单号查询支付订单。
     */
    PayOrderRespVo getOrder(String no);

    /**
     * 分页查询支付订单。
     */
    PageVo<PayOrderRespVo> queryPageList(PayOrderBo bo, PageBo pageBo);

    /**
     * 查询支付订单列表。
     */
    List<PayOrderRespVo> queryList(PayOrderBo bo);

    /**
     * 提交支付订单。
     */
    AppPayOrderSubmitRespVo submitOrder(Long userId, AppPayOrderSubmitReqVo submitReqVo, String userIp);

}

package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.bo.PayTransferBo;
import com.fly.system.api.pay.domain.vo.PayTransferVo;

import java.util.List;
import java.util.Map;

/**
 * 支付转账单 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IPayTransferService {
    PayTransferVo queryById(Long id);
    PayTransferVo getTransferByNo(String no);
    PageVo<PayTransferVo> queryPageList(PayTransferBo bo, PageBo pageBo);
    List<PayTransferVo> queryList(PayTransferBo bo);

    /**
     * 创建转账单。
     */
    Long createTransfer(PayTransferBo bo, Long userId, String userIp);

    /**
     * 同步全部待处理转账单。
     */
    int syncTransfer();

    /**
     * 同步单个转账单。
     */
    void syncTransfer(Long id);

    /**
     * 支付渠道转账回调后更新转账单。
     */
    void notifyTransfer(Long channelId, Map<String, String> params, String body, Map<String, String> headers);
}

package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.TradeOrderLogBo;
import com.fly.mall.api.trade.domain.vo.TradeOrderLogVo;

import java.util.Collection;
import java.util.List;

/**
 * 交易订单日志 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface ITradeOrderLogService {

    /**
     * 查询交易订单日志详情。
     */
    TradeOrderLogVo queryById(Long id);

    /**
     * 分页查询交易订单日志。
     */
    PageVo<TradeOrderLogVo> queryPageList(TradeOrderLogBo bo, PageBo pageBo);

    /**
     * 查询交易订单日志列表。
     */
    List<TradeOrderLogVo> queryList(TradeOrderLogBo bo);

    /**
     * 新增或修改交易订单日志。
     */
    Boolean saveOrUpdate(TradeOrderLogBo bo);

    /**
     * 校验并批量删除交易订单日志。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

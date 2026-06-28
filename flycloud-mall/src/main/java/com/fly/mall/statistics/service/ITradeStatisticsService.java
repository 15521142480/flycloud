package com.fly.mall.statistics.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.statistics.bo.TradeStatisticsBo;
import com.fly.mall.api.domain.statistics.vo.TradeStatisticsVo;

import java.util.Collection;
import java.util.List;

/**
 * 交易统计 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface ITradeStatisticsService {

    /**
     * 查询交易统计详情。
     */
    TradeStatisticsVo queryById(Long id);

    /**
     * 分页查询交易统计。
     */
    PageVo<TradeStatisticsVo> queryPageList(TradeStatisticsBo bo, PageBo pageBo);

    /**
     * 查询交易统计列表。
     */
    List<TradeStatisticsVo> queryList(TradeStatisticsBo bo);

    /**
     * 新增或修改交易统计。
     */
    Boolean saveOrUpdate(TradeStatisticsBo bo);

    /**
     * 校验并批量删除交易统计。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

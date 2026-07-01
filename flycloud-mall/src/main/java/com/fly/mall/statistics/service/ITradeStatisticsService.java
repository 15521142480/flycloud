package com.fly.mall.statistics.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.statistics.domain.bo.TradeStatisticsBo;
import com.fly.mall.api.statistics.domain.bo.StatisticsTimeRangeBo;
import com.fly.mall.api.statistics.domain.bo.TradeOrderTrendBo;
import com.fly.mall.api.statistics.domain.vo.DataComparisonRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeOrderCountRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeOrderSummaryRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeOrderTrendRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeSummaryRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeStatisticsVo;
import com.fly.mall.api.statistics.domain.vo.TradeTrendSummaryRespVo;

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
     * 查询交易统计汇总。
     */
    DataComparisonRespVo<TradeSummaryRespVo> getTradeSummaryComparison();

    /**
     * 查询交易状况分析。
     */
    DataComparisonRespVo<TradeTrendSummaryRespVo> getTradeStatisticsAnalyse(StatisticsTimeRangeBo bo);

    /**
     * 查询交易状况明细。
     */
    List<TradeTrendSummaryRespVo> getTradeStatisticsList(StatisticsTimeRangeBo bo);

    /**
     * 查询交易待处理数量。
     */
    TradeOrderCountRespVo getOrderCount();

    /**
     * 查询交易订单对照数据。
     */
    DataComparisonRespVo<TradeOrderSummaryRespVo> getOrderComparison();

    /**
     * 查询订单数量趋势。
     */
    List<DataComparisonRespVo<TradeOrderTrendRespVo>> getOrderCountTrendComparison(TradeOrderTrendBo bo);

    /**
     * 新增或修改交易统计。
     */
    Boolean saveOrUpdate(TradeStatisticsBo bo);

    /**
     * 校验并批量删除交易统计。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

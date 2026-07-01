package com.fly.mall.statistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.statistics.domain.TradeStatistics;
import com.fly.mall.api.statistics.domain.bo.StatisticsTimeRangeBo;
import com.fly.mall.api.statistics.domain.bo.TradeStatisticsBo;
import com.fly.mall.api.statistics.domain.bo.TradeOrderTrendBo;
import com.fly.mall.api.statistics.domain.vo.DataComparisonRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeOrderCountRespVo;
import com.fly.mall.api.statistics.domain.vo.StatisticsTradeOrderSummaryRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeOrderTrendRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeSummaryRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeStatisticsVo;
import com.fly.mall.api.statistics.domain.vo.TradeTrendSummaryRespVo;
import com.fly.mall.statistics.mapper.TradeStatisticsMapper;
import com.fly.mall.statistics.service.ITradeStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 交易统计 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class TradeStatisticsServiceImpl extends BaseServiceImpl<TradeStatisticsMapper, TradeStatistics> implements ITradeStatisticsService {

    private final TradeStatisticsMapper baseMapper;
    private final JdbcTemplate jdbcTemplate;

    /**
     * 查询交易统计详情。
     */
    @Override
    public TradeStatisticsVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询交易统计。
     */
    @Override
    public PageVo<TradeStatisticsVo> queryPageList(TradeStatisticsBo bo, PageBo pageBo) {
        LambdaQueryWrapper<TradeStatistics> lqw = buildQueryWrapper(bo);
        Page<TradeStatisticsVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询交易统计列表。
     */
    @Override
    public List<TradeStatisticsVo> queryList(TradeStatisticsBo bo) {
        LambdaQueryWrapper<TradeStatistics> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询交易统计汇总。
     */
    @Override
    public DataComparisonRespVo<TradeSummaryRespVo> getTradeSummaryComparison() {
        LocalDateTime yesterdayBegin = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime todayBegin = LocalDate.now().atStartOfDay();
        LocalDateTime beforeYesterdayBegin = LocalDate.now().minusDays(2).atStartOfDay();
        LocalDateTime monthBegin = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime lastMonthBegin = monthBegin.minusMonths(1);

        TradeSummaryRespVo value = new TradeSummaryRespVo();
        TradeTrendSummaryRespVo yesterday = summaryTradeStatistics(listByRange(yesterdayBegin, todayBegin.minusSeconds(1)));
        TradeTrendSummaryRespVo month = summaryTradeStatistics(listByRange(monthBegin, LocalDateTime.now()));
        value.setYesterdayOrderCount(sumOrderPayCount(listByRange(yesterdayBegin, todayBegin.minusSeconds(1))));
        value.setYesterdayPayPrice(yesterday.getOrderPayPrice());
        value.setMonthOrderCount(sumOrderPayCount(listByRange(monthBegin, LocalDateTime.now())));
        value.setMonthPayPrice(month.getOrderPayPrice());

        TradeSummaryRespVo reference = new TradeSummaryRespVo();
        TradeTrendSummaryRespVo beforeYesterday = summaryTradeStatistics(listByRange(beforeYesterdayBegin, yesterdayBegin.minusSeconds(1)));
        TradeTrendSummaryRespVo lastMonth = summaryTradeStatistics(listByRange(lastMonthBegin, monthBegin.minusSeconds(1)));
        reference.setYesterdayOrderCount(sumOrderPayCount(listByRange(beforeYesterdayBegin, yesterdayBegin.minusSeconds(1))));
        reference.setYesterdayPayPrice(beforeYesterday.getOrderPayPrice());
        reference.setMonthOrderCount(sumOrderPayCount(listByRange(lastMonthBegin, monthBegin.minusSeconds(1))));
        reference.setMonthPayPrice(lastMonth.getOrderPayPrice());
        return new DataComparisonRespVo<>(value, reference);
    }

    /**
     * 查询交易状况分析。
     */
    @Override
    public DataComparisonRespVo<TradeTrendSummaryRespVo> getTradeStatisticsAnalyse(StatisticsTimeRangeBo bo) {
        return new DataComparisonRespVo<>(
                summaryTradeStatistics(getTradeStatisticsList(bo)),
                summaryTradeStatistics(getTradeStatisticsList(previousRange(bo))));
    }

    /**
     * 查询交易状况明细。
     */
    @Override
    public List<TradeTrendSummaryRespVo> getTradeStatisticsList(StatisticsTimeRangeBo bo) {
        return baseMapper.selectVoList(buildRangeWrapper(bo)).stream()
                .map(this::convertTrendRespVo)
                .toList();
    }

    /**
     * 查询交易待处理数量。
     */
    @Override
    public TradeOrderCountRespVo getOrderCount() {
        TradeOrderCountRespVo respVo = new TradeOrderCountRespVo();
        respVo.setUndelivered(queryCount("select count(1) from trade_order where is_deleted = 0 and status = ? and delivery_type = ?", 10, 1));
        respVo.setPickUp(queryCount("select count(1) from trade_order where is_deleted = 0 and status = ? and delivery_type = ?", 20, 2));
        respVo.setAfterSaleApply(queryCount("select count(1) from trade_after_sale where is_deleted = 0 and status = ?", 10));
        respVo.setAuditingWithdraw(queryCount("select count(1) from trade_brokerage_withdraw where is_deleted = 0 and status = ?", 0));
        return respVo;
    }

    /**
     * 查询交易订单对照数据。
     */
    @Override
    public DataComparisonRespVo<StatisticsTradeOrderSummaryRespVo> getOrderComparison() {
        LocalDateTime yesterdayBegin = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime todayBegin = LocalDate.now().atStartOfDay();
        LocalDateTime beforeYesterdayBegin = LocalDate.now().minusDays(2).atStartOfDay();
        return new DataComparisonRespVo<>(
                summaryTradeOrder(listByRange(yesterdayBegin, todayBegin.minusSeconds(1))),
                summaryTradeOrder(listByRange(beforeYesterdayBegin, yesterdayBegin.minusSeconds(1))));
    }

    /**
     * 查询订单数量趋势。
     */
    @Override
    public List<DataComparisonRespVo<TradeOrderTrendRespVo>> getOrderCountTrendComparison(TradeOrderTrendBo bo) {
        StatisticsTimeRangeBo rangeBo = new StatisticsTimeRangeBo();
        rangeBo.setTimes(new LocalDateTime[]{bo == null ? null : bo.getBeginTime(), bo == null ? null : bo.getEndTime()});
        return getTradeStatisticsList(rangeBo).stream()
                .map(item -> {
                    TradeOrderTrendRespVo value = new TradeOrderTrendRespVo();
                    value.setDate(item.getDate() == null ? null : item.getDate().toString());
                    value.setOrderPayCount(sumOrderPayCountByDate(item.getDate()));
                    value.setOrderPayPrice(item.getOrderPayPrice());
                    TradeOrderTrendRespVo reference = new TradeOrderTrendRespVo();
                    reference.setDate(value.getDate());
                    reference.setOrderPayCount(0);
                    reference.setOrderPayPrice(0);
                    return new DataComparisonRespVo<>(value, reference);
                })
                .toList();
    }

    /**
     * 新增或修改交易统计。
     */
    @Override
    public Boolean saveOrUpdate(TradeStatisticsBo bo) {
        TradeStatistics entity = BeanUtil.toBean(bo, TradeStatistics.class);
        boolean isUpdate = entity.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(false);
        }
        if (isUpdate) {
            return baseMapper.updateById(entity) > 0;
        }
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        return baseMapper.insert(entity) > 0;
    }

    /**
     * 校验并批量删除交易统计。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            TradeStatistics entity = new TradeStatistics();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建交易统计查询条件。
     */
    private LambdaQueryWrapper<TradeStatistics> buildQueryWrapper(TradeStatisticsBo bo) {
        LambdaQueryWrapper<TradeStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(TradeStatistics::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, TradeStatistics::getId, bo.getId());
        lqw.eq(bo.getTime() != null, TradeStatistics::getTime, bo.getTime());
        lqw.eq(bo.getOrderCreateCount() != null, TradeStatistics::getOrderCreateCount, bo.getOrderCreateCount());
        lqw.eq(bo.getOrderPayCount() != null, TradeStatistics::getOrderPayCount, bo.getOrderPayCount());
        lqw.eq(bo.getOrderPayPrice() != null, TradeStatistics::getOrderPayPrice, bo.getOrderPayPrice());
        lqw.eq(bo.getAfterSaleCount() != null, TradeStatistics::getAfterSaleCount, bo.getAfterSaleCount());
        lqw.eq(bo.getAfterSaleRefundPrice() != null, TradeStatistics::getAfterSaleRefundPrice, bo.getAfterSaleRefundPrice());
        lqw.eq(bo.getBrokerageSettlementPrice() != null, TradeStatistics::getBrokerageSettlementPrice, bo.getBrokerageSettlementPrice());
        lqw.eq(bo.getWalletPayPrice() != null, TradeStatistics::getWalletPayPrice, bo.getWalletPayPrice());
        lqw.eq(bo.getRechargePayCount() != null, TradeStatistics::getRechargePayCount, bo.getRechargePayCount());
        lqw.eq(bo.getRechargePayPrice() != null, TradeStatistics::getRechargePayPrice, bo.getRechargePayPrice());
        lqw.eq(bo.getRechargeRefundCount() != null, TradeStatistics::getRechargeRefundCount, bo.getRechargeRefundCount());
        lqw.eq(bo.getRechargeRefundPrice() != null, TradeStatistics::getRechargeRefundPrice, bo.getRechargeRefundPrice());
        return lqw;
    }

    /**
     * 构建交易统计时间范围查询条件。
     */
    private LambdaQueryWrapper<TradeStatistics> buildRangeWrapper(StatisticsTimeRangeBo bo) {
        LambdaQueryWrapper<TradeStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(TradeStatistics::getIsDeleted, false);
        LocalDateTime[] times = bo == null ? null : bo.getTimes();
        if (times != null && times.length > 0 && times[0] != null) {
            lqw.ge(TradeStatistics::getTime, times[0]);
        }
        if (times != null && times.length > 1 && times[1] != null) {
            lqw.le(TradeStatistics::getTime, times[1]);
        }
        lqw.orderByAsc(TradeStatistics::getTime);
        return lqw;
    }

    /**
     * 生成对照时间范围。
     */
    private StatisticsTimeRangeBo previousRange(StatisticsTimeRangeBo bo) {
        LocalDateTime[] times = bo == null ? null : bo.getTimes();
        if (times == null || times.length < 2 || times[0] == null || times[1] == null) {
            return null;
        }
        long seconds = Math.max(1, Duration.between(times[0], times[1]).getSeconds());
        StatisticsTimeRangeBo previous = new StatisticsTimeRangeBo();
        previous.setTimes(new LocalDateTime[]{times[0].minusSeconds(seconds), times[0].minusSeconds(1)});
        return previous;
    }

    /**
     * 查询指定范围内的交易统计。
     */
    private List<TradeTrendSummaryRespVo> listByRange(LocalDateTime beginTime, LocalDateTime endTime) {
        StatisticsTimeRangeBo bo = new StatisticsTimeRangeBo();
        bo.setTimes(new LocalDateTime[]{beginTime, endTime});
        return getTradeStatisticsList(bo);
    }

    /**
     * 转换交易趋势响应对象。
     */
    private TradeTrendSummaryRespVo convertTrendRespVo(TradeStatisticsVo vo) {
        TradeTrendSummaryRespVo respVo = new TradeTrendSummaryRespVo();
        respVo.setDate(vo.getTime() == null ? null : vo.getTime().toLocalDate());
        respVo.setOrderPayPrice(value(vo.getOrderPayPrice()));
        respVo.setWalletPayPrice(value(vo.getWalletPayPrice()));
        respVo.setAfterSaleRefundPrice(value(vo.getAfterSaleRefundPrice()));
        respVo.setBrokerageSettlementPrice(value(vo.getBrokerageSettlementPrice()));
        respVo.setRechargePrice(value(vo.getRechargePayPrice()));
        respVo.setTurnoverPrice(respVo.getOrderPayPrice() + respVo.getRechargePrice());
        respVo.setExpensePrice(respVo.getWalletPayPrice() + respVo.getBrokerageSettlementPrice() + respVo.getAfterSaleRefundPrice());
        return respVo;
    }

    /**
     * 汇总交易趋势数据。
     */
    private TradeTrendSummaryRespVo summaryTradeStatistics(List<TradeTrendSummaryRespVo> list) {
        TradeTrendSummaryRespVo respVo = new TradeTrendSummaryRespVo();
        respVo.setDate(LocalDate.now());
        respVo.setOrderPayPrice(sum(list, TradeTrendSummaryRespVo::getOrderPayPrice));
        respVo.setWalletPayPrice(sum(list, TradeTrendSummaryRespVo::getWalletPayPrice));
        respVo.setAfterSaleRefundPrice(sum(list, TradeTrendSummaryRespVo::getAfterSaleRefundPrice));
        respVo.setBrokerageSettlementPrice(sum(list, TradeTrendSummaryRespVo::getBrokerageSettlementPrice));
        respVo.setRechargePrice(sum(list, TradeTrendSummaryRespVo::getRechargePrice));
        respVo.setTurnoverPrice(sum(list, TradeTrendSummaryRespVo::getTurnoverPrice));
        respVo.setExpensePrice(sum(list, TradeTrendSummaryRespVo::getExpensePrice));
        return respVo;
    }

    /**
     * 汇总订单支付数据。
     */
    private StatisticsTradeOrderSummaryRespVo summaryTradeOrder(List<TradeTrendSummaryRespVo> list) {
        StatisticsTradeOrderSummaryRespVo respVo = new StatisticsTradeOrderSummaryRespVo();
        respVo.setOrderPayCount(list.stream().map(TradeTrendSummaryRespVo::getDate).filter(Objects::nonNull)
                .mapToInt(this::sumOrderPayCountByDate).sum());
        respVo.setOrderPayPrice(sum(list, TradeTrendSummaryRespVo::getOrderPayPrice));
        return respVo;
    }

    /**
     * 汇总订单支付件数。
     */
    private Integer sumOrderPayCount(List<TradeTrendSummaryRespVo> list) {
        return list.stream().map(TradeTrendSummaryRespVo::getDate).filter(Objects::nonNull)
                .mapToInt(this::sumOrderPayCountByDate).sum();
    }

    /**
     * 根据统计日期汇总订单支付件数。
     */
    private Integer sumOrderPayCountByDate(LocalDate date) {
        if (date == null) {
            return 0;
        }
        List<TradeStatisticsVo> list = baseMapper.selectVoList(Wrappers.lambdaQuery(TradeStatistics.class)
                .eq(TradeStatistics::getIsDeleted, false)
                .ge(TradeStatistics::getTime, date.atStartOfDay())
                .lt(TradeStatistics::getTime, date.plusDays(1).atStartOfDay()));
        return list.stream().map(TradeStatisticsVo::getOrderPayCount).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
    }

    /**
     * 求和统计字段。
     */
    private Integer sum(List<TradeTrendSummaryRespVo> list, java.util.function.Function<TradeTrendSummaryRespVo, Integer> getter) {
        return list.stream().map(getter).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
    }

    /**
     * 处理空数值。
     */
    private Integer value(Integer value) {
        return value == null ? 0 : value;
    }

    /**
     * 查询数量，表结构不存在时返回 0，避免统计页影响主流程。
     */
    private Long queryCount(String sql, Object... args) {
        try {
            Long count = jdbcTemplate.queryForObject(sql, Long.class, args);
            return count == null ? 0L : count;
        } catch (DataAccessException ex) {
            return 0L;
        }
    }

}

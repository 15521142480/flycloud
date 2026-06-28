package com.fly.mall.statistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.domain.statistics.TradeStatistics;
import com.fly.mall.api.domain.statistics.bo.TradeStatisticsBo;
import com.fly.mall.api.domain.statistics.vo.TradeStatisticsVo;
import com.fly.mall.statistics.mapper.TradeStatisticsMapper;
import com.fly.mall.statistics.service.ITradeStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 交易统计 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class TradeStatisticsServiceImpl extends BaseServiceImpl<TradeStatisticsMapper, TradeStatistics> implements ITradeStatisticsService {

    private final TradeStatisticsMapper baseMapper;

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

}

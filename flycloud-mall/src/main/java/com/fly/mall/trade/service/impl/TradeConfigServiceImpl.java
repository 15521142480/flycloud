package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.domain.trade.TradeConfig;
import com.fly.mall.api.domain.trade.bo.TradeConfigBo;
import com.fly.mall.api.domain.trade.vo.TradeConfigVo;
import com.fly.mall.trade.mapper.TradeConfigMapper;
import com.fly.mall.trade.service.ITradeConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 交易配置 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class TradeConfigServiceImpl extends BaseServiceImpl<TradeConfigMapper, TradeConfig> implements ITradeConfigService {

    private final TradeConfigMapper baseMapper;

    /**
     * 查询交易配置详情。
     */
    @Override
    public TradeConfigVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询交易配置。
     */
    @Override
    public PageVo<TradeConfigVo> queryPageList(TradeConfigBo bo, PageBo pageBo) {
        LambdaQueryWrapper<TradeConfig> lqw = buildQueryWrapper(bo);
        Page<TradeConfigVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询交易配置列表。
     */
    @Override
    public List<TradeConfigVo> queryList(TradeConfigBo bo) {
        LambdaQueryWrapper<TradeConfig> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改交易配置。
     */
    @Override
    public Boolean saveOrUpdate(TradeConfigBo bo) {
        TradeConfig entity = BeanUtil.toBean(bo, TradeConfig.class);
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
     * 校验并批量删除交易配置。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            TradeConfig entity = new TradeConfig();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建交易配置查询条件。
     */
    private LambdaQueryWrapper<TradeConfig> buildQueryWrapper(TradeConfigBo bo) {
        LambdaQueryWrapper<TradeConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(TradeConfig::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, TradeConfig::getId, bo.getId());
        lqw.eq(bo.getDeliveryExpressFreeEnabled() != null, TradeConfig::getDeliveryExpressFreeEnabled, bo.getDeliveryExpressFreeEnabled());
        lqw.eq(bo.getDeliveryExpressFreePrice() != null, TradeConfig::getDeliveryExpressFreePrice, bo.getDeliveryExpressFreePrice());
        lqw.eq(bo.getDeliveryPickUpEnabled() != null, TradeConfig::getDeliveryPickUpEnabled, bo.getDeliveryPickUpEnabled());
        lqw.eq(bo.getBrokerageEnabled() != null, TradeConfig::getBrokerageEnabled, bo.getBrokerageEnabled());
        lqw.eq(bo.getBrokerageEnabledCondition() != null, TradeConfig::getBrokerageEnabledCondition, bo.getBrokerageEnabledCondition());
        lqw.eq(bo.getBrokerageBindMode() != null, TradeConfig::getBrokerageBindMode, bo.getBrokerageBindMode());
        lqw.eq(bo.getBrokerageFirstPercent() != null, TradeConfig::getBrokerageFirstPercent, bo.getBrokerageFirstPercent());
        lqw.eq(bo.getBrokerageSecondPercent() != null, TradeConfig::getBrokerageSecondPercent, bo.getBrokerageSecondPercent());
        lqw.eq(bo.getBrokerageWithdrawMinPrice() != null, TradeConfig::getBrokerageWithdrawMinPrice, bo.getBrokerageWithdrawMinPrice());
        lqw.eq(bo.getBrokerageWithdrawFeePercent() != null, TradeConfig::getBrokerageWithdrawFeePercent, bo.getBrokerageWithdrawFeePercent());
        lqw.eq(bo.getBrokerageFrozenDays() != null, TradeConfig::getBrokerageFrozenDays, bo.getBrokerageFrozenDays());
        return lqw;
    }

}

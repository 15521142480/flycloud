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
import com.fly.mall.api.trade.domain.TradeOrderItem;
import com.fly.mall.api.trade.domain.bo.TradeOrderItemBo;
import com.fly.mall.api.trade.domain.vo.TradeOrderItemVo;
import com.fly.mall.trade.mapper.TradeOrderItemMapper;
import com.fly.mall.trade.service.ITradeOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 交易订单项 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class TradeOrderItemServiceImpl extends BaseServiceImpl<TradeOrderItemMapper, TradeOrderItem> implements ITradeOrderItemService {

    private final TradeOrderItemMapper baseMapper;

    /**
     * 查询交易订单项详情。
     */
    @Override
    public TradeOrderItemVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询交易订单项。
     */
    @Override
    public PageVo<TradeOrderItemVo> queryPageList(TradeOrderItemBo bo, PageBo pageBo) {
        LambdaQueryWrapper<TradeOrderItem> lqw = buildQueryWrapper(bo);
        Page<TradeOrderItemVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询交易订单项列表。
     */
    @Override
    public List<TradeOrderItemVo> queryList(TradeOrderItemBo bo) {
        LambdaQueryWrapper<TradeOrderItem> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 根据订单编号查询订单项列表。
     */
    @Override
    public List<TradeOrderItemVo> queryListByOrderId(Long orderId) {
        TradeOrderItemBo bo = new TradeOrderItemBo();
        bo.setOrderId(orderId);
        return queryList(bo);
    }

    /**
     * 根据订单编号集合查询订单项列表。
     */
    @Override
    public List<TradeOrderItemVo> queryListByOrderIds(Collection<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return List.of();
        }
        LambdaQueryWrapper<TradeOrderItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(TradeOrderItem::getIsDeleted, false);
        lqw.in(TradeOrderItem::getOrderId, orderIds);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改交易订单项。
     */
    @Override
    public Boolean saveOrUpdate(TradeOrderItemBo bo) {
        TradeOrderItem entity = BeanUtil.toBean(bo, TradeOrderItem.class);
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
     * 校验并批量删除交易订单项。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            TradeOrderItem entity = new TradeOrderItem();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建交易订单项查询条件。
     */
    private LambdaQueryWrapper<TradeOrderItem> buildQueryWrapper(TradeOrderItemBo bo) {
        LambdaQueryWrapper<TradeOrderItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(TradeOrderItem::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, TradeOrderItem::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, TradeOrderItem::getUserId, bo.getUserId());
        lqw.eq(bo.getOrderId() != null, TradeOrderItem::getOrderId, bo.getOrderId());
        lqw.eq(bo.getCartId() != null, TradeOrderItem::getCartId, bo.getCartId());
        lqw.eq(bo.getSpuId() != null, TradeOrderItem::getSpuId, bo.getSpuId());
        lqw.like(StringUtils.isNotBlank(bo.getSpuName()), TradeOrderItem::getSpuName, bo.getSpuName());
        lqw.eq(bo.getSkuId() != null, TradeOrderItem::getSkuId, bo.getSkuId());
        lqw.like(StringUtils.isNotBlank(bo.getPicUrl()), TradeOrderItem::getPicUrl, bo.getPicUrl());
        lqw.eq(bo.getCount() != null, TradeOrderItem::getCount, bo.getCount());
        lqw.eq(bo.getCommentStatus() != null, TradeOrderItem::getCommentStatus, bo.getCommentStatus());
        lqw.eq(bo.getPrice() != null, TradeOrderItem::getPrice, bo.getPrice());
        lqw.eq(bo.getDiscountPrice() != null, TradeOrderItem::getDiscountPrice, bo.getDiscountPrice());
        lqw.eq(bo.getDeliveryPrice() != null, TradeOrderItem::getDeliveryPrice, bo.getDeliveryPrice());
        lqw.eq(bo.getAdjustPrice() != null, TradeOrderItem::getAdjustPrice, bo.getAdjustPrice());
        lqw.eq(bo.getPayPrice() != null, TradeOrderItem::getPayPrice, bo.getPayPrice());
        lqw.eq(bo.getCouponPrice() != null, TradeOrderItem::getCouponPrice, bo.getCouponPrice());
        lqw.eq(bo.getPointPrice() != null, TradeOrderItem::getPointPrice, bo.getPointPrice());
        lqw.eq(bo.getUsePoint() != null, TradeOrderItem::getUsePoint, bo.getUsePoint());
        lqw.eq(bo.getGivePoint() != null, TradeOrderItem::getGivePoint, bo.getGivePoint());
        lqw.eq(bo.getVipPrice() != null, TradeOrderItem::getVipPrice, bo.getVipPrice());
        lqw.eq(bo.getAfterSaleId() != null, TradeOrderItem::getAfterSaleId, bo.getAfterSaleId());
        lqw.eq(bo.getAfterSaleStatus() != null, TradeOrderItem::getAfterSaleStatus, bo.getAfterSaleStatus());

//        lqw.eq(bo.getPropertyId() != null, TradeOrderItem::getPropertyId, bo.getPropertyId());
//        lqw.like(StringUtils.isNotBlank(bo.getPropertyName()), TradeOrderItem::getPropertyName, bo.getPropertyName());
//        lqw.eq(bo.getValueId() != null, TradeOrderItem::getValueId, bo.getValueId());
//        lqw.like(StringUtils.isNotBlank(bo.getValueName()), TradeOrderItem::getValueName, bo.getValueName());

        return lqw;
    }

}

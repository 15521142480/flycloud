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
import com.fly.mall.api.trade.domain.AfterSale;
import com.fly.mall.api.trade.domain.bo.AfterSaleBo;
import com.fly.mall.api.trade.domain.vo.AfterSaleVo;
import com.fly.mall.trade.mapper.AfterSaleMapper;
import com.fly.mall.trade.service.IAfterSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 售后单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class AfterSaleServiceImpl extends BaseServiceImpl<AfterSaleMapper, AfterSale> implements IAfterSaleService {

    private final AfterSaleMapper baseMapper;

    /**
     * 查询售后单详情。
     */
    @Override
    public AfterSaleVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询售后单。
     */
    @Override
    public PageVo<AfterSaleVo> queryPageList(AfterSaleBo bo, PageBo pageBo) {
        LambdaQueryWrapper<AfterSale> lqw = buildQueryWrapper(bo);
        Page<AfterSaleVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询售后单列表。
     */
    @Override
    public List<AfterSaleVo> queryList(AfterSaleBo bo) {
        LambdaQueryWrapper<AfterSale> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改售后单。
     */
    @Override
    public Boolean saveOrUpdate(AfterSaleBo bo) {
        AfterSale entity = BeanUtil.toBean(bo, AfterSale.class);
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
     * 校验并批量删除售后单。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            AfterSale entity = new AfterSale();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建售后单查询条件。
     */
    private LambdaQueryWrapper<AfterSale> buildQueryWrapper(AfterSaleBo bo) {
        LambdaQueryWrapper<AfterSale> lqw = Wrappers.lambdaQuery();
        lqw.eq(AfterSale::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, AfterSale::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getNo()), AfterSale::getNo, bo.getNo());
        lqw.eq(bo.getStatus() != null, AfterSale::getStatus, bo.getStatus());
        lqw.eq(bo.getWay() != null, AfterSale::getWay, bo.getWay());
        lqw.eq(bo.getType() != null, AfterSale::getType, bo.getType());
        lqw.eq(bo.getUserId() != null, AfterSale::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getApplyReason()), AfterSale::getApplyReason, bo.getApplyReason());
        lqw.like(StringUtils.isNotBlank(bo.getApplyDescription()), AfterSale::getApplyDescription, bo.getApplyDescription());
        lqw.eq(bo.getOrderId() != null, AfterSale::getOrderId, bo.getOrderId());
        lqw.like(StringUtils.isNotBlank(bo.getOrderNo()), AfterSale::getOrderNo, bo.getOrderNo());
        lqw.eq(bo.getOrderItemId() != null, AfterSale::getOrderItemId, bo.getOrderItemId());
        lqw.eq(bo.getSpuId() != null, AfterSale::getSpuId, bo.getSpuId());
        lqw.like(StringUtils.isNotBlank(bo.getSpuName()), AfterSale::getSpuName, bo.getSpuName());
        lqw.eq(bo.getSkuId() != null, AfterSale::getSkuId, bo.getSkuId());
        lqw.like(StringUtils.isNotBlank(bo.getPicUrl()), AfterSale::getPicUrl, bo.getPicUrl());
        lqw.eq(bo.getCount() != null, AfterSale::getCount, bo.getCount());
        lqw.eq(bo.getAuditTime() != null, AfterSale::getAuditTime, bo.getAuditTime());
        lqw.eq(bo.getAuditUserId() != null, AfterSale::getAuditUserId, bo.getAuditUserId());
        lqw.like(StringUtils.isNotBlank(bo.getAuditReason()), AfterSale::getAuditReason, bo.getAuditReason());
        lqw.eq(bo.getRefundPrice() != null, AfterSale::getRefundPrice, bo.getRefundPrice());
        lqw.eq(bo.getPayRefundId() != null, AfterSale::getPayRefundId, bo.getPayRefundId());
        lqw.eq(bo.getRefundTime() != null, AfterSale::getRefundTime, bo.getRefundTime());
        lqw.eq(bo.getLogisticsId() != null, AfterSale::getLogisticsId, bo.getLogisticsId());
        lqw.like(StringUtils.isNotBlank(bo.getLogisticsNo()), AfterSale::getLogisticsNo, bo.getLogisticsNo());
        lqw.eq(bo.getDeliveryTime() != null, AfterSale::getDeliveryTime, bo.getDeliveryTime());
        lqw.eq(bo.getReceiveTime() != null, AfterSale::getReceiveTime, bo.getReceiveTime());
        lqw.like(StringUtils.isNotBlank(bo.getReceiveReason()), AfterSale::getReceiveReason, bo.getReceiveReason());
        return lqw;
    }

}

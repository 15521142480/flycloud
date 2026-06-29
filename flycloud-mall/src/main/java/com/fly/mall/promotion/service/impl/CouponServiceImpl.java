package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.Coupon;
import com.fly.mall.api.promotion.domain.bo.CouponBo;
import com.fly.mall.api.promotion.domain.vo.CouponVo;
import com.fly.mall.promotion.mapper.CouponMapper;
import com.fly.mall.promotion.service.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 优惠券 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class CouponServiceImpl extends BaseServiceImpl<CouponMapper, Coupon> implements ICouponService {

    private final CouponMapper baseMapper;

    /**
     * 查询优惠券详情。
     */
    @Override
    public CouponVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询优惠券。
     */
    @Override
    public PageVo<CouponVo> queryPageList(CouponBo bo, PageBo pageBo) {
        LambdaQueryWrapper<Coupon> lqw = buildQueryWrapper(bo);
        Page<CouponVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询优惠券列表。
     */
    @Override
    public List<CouponVo> queryList(CouponBo bo) {
        LambdaQueryWrapper<Coupon> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改优惠券。
     */
    @Override
    public Boolean saveOrUpdate(CouponBo bo) {
        Coupon entity = BeanUtil.toBean(bo, Coupon.class);
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
     * 校验并批量删除优惠券。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            Coupon entity = new Coupon();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建优惠券查询条件。
     */
    private LambdaQueryWrapper<Coupon> buildQueryWrapper(CouponBo bo) {
        LambdaQueryWrapper<Coupon> lqw = Wrappers.lambdaQuery();
        lqw.eq(Coupon::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, Coupon::getId, bo.getId());
        lqw.eq(bo.getTemplateId() != null, Coupon::getTemplateId, bo.getTemplateId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Coupon::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, Coupon::getStatus, bo.getStatus());
        lqw.eq(bo.getUserId() != null, Coupon::getUserId, bo.getUserId());
        lqw.eq(bo.getTakeType() != null, Coupon::getTakeType, bo.getTakeType());
        lqw.eq(bo.getUsePrice() != null, Coupon::getUsePrice, bo.getUsePrice());
        lqw.eq(bo.getValidStartTime() != null, Coupon::getValidStartTime, bo.getValidStartTime());
        lqw.eq(bo.getValidEndTime() != null, Coupon::getValidEndTime, bo.getValidEndTime());
        lqw.eq(bo.getProductScope() != null, Coupon::getProductScope, bo.getProductScope());
        lqw.eq(bo.getDiscountType() != null, Coupon::getDiscountType, bo.getDiscountType());
        lqw.eq(bo.getDiscountPercent() != null, Coupon::getDiscountPercent, bo.getDiscountPercent());
        lqw.eq(bo.getDiscountPrice() != null, Coupon::getDiscountPrice, bo.getDiscountPrice());
        lqw.eq(bo.getDiscountLimitPrice() != null, Coupon::getDiscountLimitPrice, bo.getDiscountLimitPrice());
        lqw.eq(bo.getUseOrderId() != null, Coupon::getUseOrderId, bo.getUseOrderId());
        lqw.eq(bo.getUseTime() != null, Coupon::getUseTime, bo.getUseTime());
        return lqw;
    }

}

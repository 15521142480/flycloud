package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.Coupon;
import com.fly.mall.api.promotion.domain.CouponTemplate;
import com.fly.mall.api.promotion.domain.bo.CouponBo;
import com.fly.mall.api.promotion.domain.vo.CouponVo;
import com.fly.mall.api.promotion.domain.vo.CouponTemplateVo;
import com.fly.mall.promotion.mapper.CouponMapper;
import com.fly.mall.promotion.mapper.CouponTemplateMapper;
import com.fly.mall.promotion.service.ICouponService;
import com.fly.mall.promotion.service.ICouponTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 优惠券 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class CouponServiceImpl extends BaseServiceImpl<CouponMapper, Coupon> implements ICouponService {

    private static final int STATUS_UNUSED = 1;
    private static final int STATUS_USED = 2;
    private static final int STATUS_EXPIRE = 3;
    private static final int TAKE_TYPE_USER = 1;
    private static final int VALIDITY_TYPE_DATE = 1;
    private static final int VALIDITY_TYPE_TERM = 2;

    private final CouponMapper baseMapper;
    private final CouponTemplateMapper couponTemplateMapper;
    private final ICouponTemplateService couponTemplateService;

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
        lqw.orderByDesc(Coupon::getCreateTime);
        Page<CouponVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询优惠券列表。
     */
    @Override
    public List<CouponVo> queryList(CouponBo bo) {
        LambdaQueryWrapper<Coupon> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(Coupon::getCreateTime);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 领取优惠券。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long takeCoupon(Long templateId, Long userId, Integer takeType) {
        if (userId == null) {
            throw new ServiceException("请先登录后再领取优惠券");
        }
        CouponTemplate template = validateCouponTemplateCanTake(templateId, userId, takeType == null ? TAKE_TYPE_USER : takeType);
        Coupon entity = buildCouponFromTemplate(template, userId, takeType == null ? TAKE_TYPE_USER : takeType);
        baseMapper.insert(entity);
        couponTemplateService.updateCouponTemplateTakeCount(templateId, 1);
        return entity.getId();
    }

    /**
     * 使用优惠券。
     */
    @Override
    public Boolean useCoupon(Long id, Long userId, Long orderId) {
        Coupon coupon = validateUserCoupon(id, userId);
        if (!Objects.equals(coupon.getStatus(), STATUS_UNUSED)) {
            throw new ServiceException("优惠券不是未使用状态");
        }
        if (!isCouponInValidTime(coupon)) {
            throw new ServiceException("优惠券不在有效期内");
        }
        Coupon entity = new Coupon();
        entity.setStatus(STATUS_USED);
        entity.setUseOrderId(orderId);
        entity.setUseTime(LocalDateTime.now());
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(LocalDateTime.now());
        LambdaUpdateWrapper<Coupon> luw = Wrappers.lambdaUpdate();
        luw.eq(Coupon::getId, id);
        luw.eq(Coupon::getUserId, userId);
        luw.eq(Coupon::getStatus, STATUS_UNUSED);
        boolean success = baseMapper.update(entity, luw) > 0;
        if (!success) {
            throw new ServiceException("优惠券状态已变化，请刷新后重试");
        }
        updateTemplateUseCount(coupon.getTemplateId(), 1);
        return true;
    }

    /**
     * 退回已使用优惠券。
     */
    @Override
    public Boolean returnUsedCoupon(Long id) {
        Coupon coupon = baseMapper.selectById(id);
        if (coupon == null || Boolean.TRUE.equals(coupon.getIsDeleted())) {
            throw new ServiceException("优惠券不存在");
        }
        if (!Objects.equals(coupon.getStatus(), STATUS_USED)) {
            throw new ServiceException("优惠券不是已使用状态");
        }
        Coupon entity = new Coupon();
        entity.setStatus(LocalDateTime.now().isAfter(coupon.getValidEndTime()) ? STATUS_EXPIRE : STATUS_UNUSED);
        entity.setUseOrderId(null);
        entity.setUseTime(null);
        entity.setUpdateBy(String.valueOf(coupon.getUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        LambdaUpdateWrapper<Coupon> luw = Wrappers.lambdaUpdate();
        luw.eq(Coupon::getId, id);
        luw.eq(Coupon::getStatus, STATUS_USED);
        boolean success = baseMapper.update(entity, luw) > 0;
        if (success) {
            updateTemplateUseCount(coupon.getTemplateId(), -1);
        }
        return success;
    }

    /**
     * 过期未使用优惠券。
     */
    @Override
    public Integer expireCoupon() {
        List<Coupon> coupons = baseMapper.selectList(new LambdaQueryWrapper<Coupon>()
                .eq(Coupon::getIsDeleted, false)
                .eq(Coupon::getStatus, STATUS_UNUSED)
                .le(Coupon::getValidEndTime, LocalDateTime.now()));
        int count = 0;
        for (Coupon coupon : coupons) {
            Coupon entity = new Coupon();
            entity.setStatus(STATUS_EXPIRE);
            LambdaUpdateWrapper<Coupon> luw = Wrappers.lambdaUpdate();
            luw.eq(Coupon::getId, coupon.getId());
            luw.eq(Coupon::getStatus, STATUS_UNUSED);
            count += baseMapper.update(entity, luw);
        }
        return count;
    }

    /**
     * 查询未使用优惠券数量。
     */
    @Override
    public Long getUnusedCouponCount(Long userId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<Coupon>()
                .eq(Coupon::getIsDeleted, false)
                .eq(Coupon::getUserId, userId)
                .eq(Coupon::getStatus, STATUS_UNUSED)
                .le(Coupon::getValidStartTime, LocalDateTime.now())
                .ge(Coupon::getValidEndTime, LocalDateTime.now()));
    }

    /**
     * 查询用户领取指定模板的数量。
     */
    @Override
    public Integer getTakeCount(Long templateId, Long userId) {
        return Math.toIntExact(baseMapper.selectCount(new LambdaQueryWrapper<Coupon>()
                .eq(Coupon::getIsDeleted, false)
                .eq(Coupon::getTemplateId, templateId)
                .eq(Coupon::getUserId, userId)));
    }

    /**
     * 判断用户是否可继续领取模板优惠券。
     */
    @Override
    public Boolean canTake(Long templateId, Long userId) {
        if (userId == null) {
            return true;
        }
        CouponTemplate template = couponTemplateMapper.selectById(templateId);
        if (template == null || Boolean.TRUE.equals(template.getIsDeleted())) {
            return false;
        }
        if (couponTemplateService.isTakeLimitCountUnlimited(template.getTakeLimitCount())) {
            return true;
        }
        return getTakeCount(templateId, userId) < template.getTakeLimitCount();
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
            Coupon oldCoupon = baseMapper.selectById(id);
            if (oldCoupon == null || Boolean.TRUE.equals(oldCoupon.getIsDeleted())) {
                continue;
            }
            if (Objects.equals(oldCoupon.getStatus(), STATUS_USED)) {
                throw new ServiceException("已使用优惠券不能删除");
            }
            couponTemplateService.updateCouponTemplateTakeCount(oldCoupon.getTemplateId(), -1);
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 校验优惠券模板可领取。
     */
    private CouponTemplate validateCouponTemplateCanTake(Long templateId, Long userId, Integer takeType) {
        CouponTemplate template = couponTemplateMapper.selectById(templateId);
        if (template == null || Boolean.TRUE.equals(template.getIsDeleted())) {
            throw new ServiceException("优惠券模板不存在");
        }
        if (!StatusEnum.isEnable(template.getStatus())) {
            throw new ServiceException("优惠券模板已禁用");
        }
        if (!Objects.equals(template.getTakeType(), takeType)) {
            throw new ServiceException("优惠券模板不支持当前领取方式");
        }
        if (!couponTemplateService.isTotalCountUnlimited(template.getTotalCount())
                && (template.getTakeCount() == null ? 0 : template.getTakeCount()) >= template.getTotalCount()) {
            throw new ServiceException("优惠券模板剩余数量不足");
        }
        if (!couponTemplateService.isTakeLimitCountUnlimited(template.getTakeLimitCount())
                && getTakeCount(templateId, userId) >= template.getTakeLimitCount()) {
            throw new ServiceException("已达到该优惠券领取上限");
        }
        if (Objects.equals(template.getValidityType(), VALIDITY_TYPE_DATE)
                && template.getValidEndTime() != null
                && LocalDateTime.now().isAfter(template.getValidEndTime())) {
            throw new ServiceException("优惠券模板已过期");
        }
        return template;
    }

    /**
     * 基于优惠券模板创建用户优惠券快照。
     */
    private Coupon buildCouponFromTemplate(CouponTemplate template, Long userId, Integer takeType) {
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = new Coupon();
        coupon.setTemplateId(template.getId());
        coupon.setName(template.getName());
        coupon.setStatus(STATUS_UNUSED);
        coupon.setUserId(userId);
        coupon.setTakeType(takeType);
        coupon.setUsePrice(template.getUsePrice());
        fillCouponValidTime(coupon, template, now);
        coupon.setProductScope(template.getProductScope());
        coupon.setProductScopeValues(template.getProductScopeValues());
        coupon.setDiscountType(template.getDiscountType());
        coupon.setDiscountPercent(template.getDiscountPercent());
        coupon.setDiscountPrice(template.getDiscountPrice());
        coupon.setDiscountLimitPrice(template.getDiscountLimitPrice());
        coupon.setIsDeleted(false);
        coupon.setCreateBy(String.valueOf(userId));
        coupon.setCreateTime(now);
        coupon.setUpdateBy(String.valueOf(userId));
        coupon.setUpdateTime(now);
        return coupon;
    }

    /**
     * 根据模板有效期类型计算优惠券有效期。
     */
    private void fillCouponValidTime(Coupon coupon, CouponTemplate template, LocalDateTime now) {
        if (Objects.equals(template.getValidityType(), VALIDITY_TYPE_TERM)) {
            coupon.setValidStartTime(now.plusDays(template.getFixedStartTerm() == null ? 0 : template.getFixedStartTerm()));
            coupon.setValidEndTime(now.plusDays(template.getFixedEndTerm() == null ? 0 : template.getFixedEndTerm()));
            return;
        }
        coupon.setValidStartTime(template.getValidStartTime() == null ? now : template.getValidStartTime());
        coupon.setValidEndTime(template.getValidEndTime());
    }

    /**
     * 校验用户优惠券存在。
     */
    private Coupon validateUserCoupon(Long id, Long userId) {
        Coupon coupon = baseMapper.selectOne(new LambdaQueryWrapper<Coupon>()
                .eq(Coupon::getIsDeleted, false)
                .eq(Coupon::getId, id)
                .eq(Coupon::getUserId, userId));
        if (coupon == null) {
            throw new ServiceException("优惠券不存在");
        }
        return coupon;
    }

    /**
     * 判断优惠券是否处于有效期。
     */
    private boolean isCouponInValidTime(Coupon coupon) {
        LocalDateTime now = LocalDateTime.now();
        return (coupon.getValidStartTime() == null || !now.isBefore(coupon.getValidStartTime()))
                && (coupon.getValidEndTime() == null || !now.isAfter(coupon.getValidEndTime()));
    }

    /**
     * 增减模板使用数量。
     */
    private void updateTemplateUseCount(Long templateId, int incrCount) {
        CouponTemplate template = couponTemplateMapper.selectById(templateId);
        if (template == null) {
            return;
        }
        CouponTemplate entity = new CouponTemplate();
        entity.setId(templateId);
        entity.setUseCount(Math.max(0, (template.getUseCount() == null ? 0 : template.getUseCount()) + incrCount));
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        couponTemplateMapper.updateById(entity);
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

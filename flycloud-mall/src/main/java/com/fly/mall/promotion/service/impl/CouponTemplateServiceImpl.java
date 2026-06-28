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
import com.fly.mall.api.domain.promotion.CouponTemplate;
import com.fly.mall.api.domain.promotion.bo.CouponTemplateBo;
import com.fly.mall.api.domain.promotion.vo.CouponTemplateVo;
import com.fly.mall.promotion.mapper.CouponTemplateMapper;
import com.fly.mall.promotion.service.ICouponTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 优惠券模板 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class CouponTemplateServiceImpl extends BaseServiceImpl<CouponTemplateMapper, CouponTemplate> implements ICouponTemplateService {

    private final CouponTemplateMapper baseMapper;

    /**
     * 查询优惠券模板详情。
     */
    @Override
    public CouponTemplateVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询优惠券模板。
     */
    @Override
    public PageVo<CouponTemplateVo> queryPageList(CouponTemplateBo bo, PageBo pageBo) {
        LambdaQueryWrapper<CouponTemplate> lqw = buildQueryWrapper(bo);
        Page<CouponTemplateVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询优惠券模板列表。
     */
    @Override
    public List<CouponTemplateVo> queryList(CouponTemplateBo bo) {
        LambdaQueryWrapper<CouponTemplate> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改优惠券模板。
     */
    @Override
    public Boolean saveOrUpdate(CouponTemplateBo bo) {
        CouponTemplate entity = BeanUtil.toBean(bo, CouponTemplate.class);
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
     * 校验并批量删除优惠券模板。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            CouponTemplate entity = new CouponTemplate();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建优惠券模板查询条件。
     */
    private LambdaQueryWrapper<CouponTemplate> buildQueryWrapper(CouponTemplateBo bo) {
        LambdaQueryWrapper<CouponTemplate> lqw = Wrappers.lambdaQuery();
        lqw.eq(CouponTemplate::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, CouponTemplate::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), CouponTemplate::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getDescription()), CouponTemplate::getDescription, bo.getDescription());
        lqw.eq(bo.getStatus() != null, CouponTemplate::getStatus, bo.getStatus());
        lqw.eq(bo.getTotalCount() != null, CouponTemplate::getTotalCount, bo.getTotalCount());
        lqw.eq(bo.getTakeLimitCount() != null, CouponTemplate::getTakeLimitCount, bo.getTakeLimitCount());
        lqw.eq(bo.getTakeType() != null, CouponTemplate::getTakeType, bo.getTakeType());
        lqw.eq(bo.getUsePrice() != null, CouponTemplate::getUsePrice, bo.getUsePrice());
        lqw.eq(bo.getProductScope() != null, CouponTemplate::getProductScope, bo.getProductScope());
        lqw.eq(bo.getValidityType() != null, CouponTemplate::getValidityType, bo.getValidityType());
        lqw.eq(bo.getValidStartTime() != null, CouponTemplate::getValidStartTime, bo.getValidStartTime());
        lqw.eq(bo.getValidEndTime() != null, CouponTemplate::getValidEndTime, bo.getValidEndTime());
        lqw.eq(bo.getFixedStartTerm() != null, CouponTemplate::getFixedStartTerm, bo.getFixedStartTerm());
        lqw.eq(bo.getFixedEndTerm() != null, CouponTemplate::getFixedEndTerm, bo.getFixedEndTerm());
        lqw.eq(bo.getDiscountType() != null, CouponTemplate::getDiscountType, bo.getDiscountType());
        lqw.eq(bo.getDiscountPercent() != null, CouponTemplate::getDiscountPercent, bo.getDiscountPercent());
        lqw.eq(bo.getDiscountPrice() != null, CouponTemplate::getDiscountPrice, bo.getDiscountPrice());
        lqw.eq(bo.getDiscountLimitPrice() != null, CouponTemplate::getDiscountLimitPrice, bo.getDiscountLimitPrice());
        lqw.eq(bo.getTakeCount() != null, CouponTemplate::getTakeCount, bo.getTakeCount());
        lqw.eq(bo.getUseCount() != null, CouponTemplate::getUseCount, bo.getUseCount());
        return lqw;
    }

}

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
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.promotion.domain.CouponTemplate;
import com.fly.mall.api.promotion.domain.bo.CouponTemplateBo;
import com.fly.mall.api.promotion.domain.vo.CouponTemplateVo;
import com.fly.mall.product.service.IProductCategoryService;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.promotion.mapper.CouponTemplateMapper;
import com.fly.mall.promotion.service.ICouponTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 优惠券模板 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class CouponTemplateServiceImpl extends BaseServiceImpl<CouponTemplateMapper, CouponTemplate> implements ICouponTemplateService {

    private static final int TAKE_LIMIT_COUNT_MAX = -1;
    private static final int TOTAL_COUNT_MAX = -1;
    private static final int TAKE_TYPE_USER = 1;
    private static final int PRODUCT_SCOPE_ALL = 1;
    private static final int PRODUCT_SCOPE_SPU = 2;
    private static final int PRODUCT_SCOPE_CATEGORY = 3;

    private final CouponTemplateMapper baseMapper;
    private final IProductSpuService productSpuService;
    private final IProductCategoryService productCategoryService;

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

    @Override
    public List<CouponTemplateVo> queryList(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }
        return baseMapper.selectVoBatchIds(ids);
    }

    /**
     * 查询可领取的优惠券模板列表。
     */
    @Override
    public List<CouponTemplateVo> queryCanTakeList(Integer productScope, Long productScopeValue, Integer count) {
        LambdaQueryWrapper<CouponTemplate> lqw = Wrappers.lambdaQuery();
        lqw.eq(CouponTemplate::getIsDeleted, false);
        lqw.eq(CouponTemplate::getStatus, StatusEnum.ENABLE.getStatus());
        lqw.eq(CouponTemplate::getTakeType, TAKE_TYPE_USER);
        lqw.le(CouponTemplate::getValidStartTime, LocalDateTime.now())
                .and(wrapper -> wrapper.ge(CouponTemplate::getValidEndTime, LocalDateTime.now())
                        .or().isNull(CouponTemplate::getValidEndTime));
        if (productScope != null && !Objects.equals(productScope, PRODUCT_SCOPE_ALL) && productScopeValue != null) {
            lqw.and(wrapper -> wrapper.eq(CouponTemplate::getProductScope, PRODUCT_SCOPE_ALL)
                    .or().eq(CouponTemplate::getProductScope, productScope)
                    .apply("JSON_CONTAINS(product_scope_values, JSON_ARRAY({0}))", productScopeValue));
        }
        lqw.orderByDesc(CouponTemplate::getCreateTime);
        if (count != null && count > 0) {
            return baseMapper.selectVoPage(new Page<>(1, count), lqw).getRecords();
        }
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 判断每人限领数量是否不限制。
     */
    @Override
    public boolean isTakeLimitCountUnlimited(Integer takeLimitCount) {
        return Objects.equals(TAKE_LIMIT_COUNT_MAX, takeLimitCount);
    }

    /**
     * 判断总发放数量是否不限制。
     */
    @Override
    public boolean isTotalCountUnlimited(Integer totalCount) {
        return Objects.equals(TOTAL_COUNT_MAX, totalCount);
    }

    /**
     * 增减优惠券模板已领取数量。
     */
    @Override
    public Boolean updateCouponTemplateTakeCount(Long id, int incrCount) {
        CouponTemplate template = baseMapper.selectById(id);
        if (template == null || Boolean.TRUE.equals(template.getIsDeleted())) {
            throw new ServiceException("优惠券模板不存在");
        }
        int takeCount = Math.max(0, (template.getTakeCount() == null ? 0 : template.getTakeCount()) + incrCount);
        if (incrCount > 0 && !isTotalCountUnlimited(template.getTotalCount()) && takeCount > template.getTotalCount()) {
            throw new ServiceException("优惠券模板剩余数量不足");
        }
        CouponTemplate entity = new CouponTemplate();
        entity.setId(id);
        entity.setTakeCount(takeCount);
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 新增或修改优惠券模板。
     */
    @Override
    public Boolean saveOrUpdate(CouponTemplateBo bo) {
        if (bo.getId() == null) {
            createCouponTemplate(bo);
            return true;
        }
        return updateCouponTemplate(bo);
    }

    /**
     * 新增优惠券模板并返回编号。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCouponTemplate(CouponTemplateBo bo) {
        validateCouponTemplateForSave(bo, null);
        CouponTemplate entity = BeanUtil.toBean(bo, CouponTemplate.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setId(null);
        entity.setStatus(entity.getStatus() == null ? StatusEnum.ENABLE.getStatus() : entity.getStatus());
        entity.setTakeCount(entity.getTakeCount() == null ? 0 : entity.getTakeCount());
        entity.setUseCount(entity.getUseCount() == null ? 0 : entity.getUseCount());
        entity.setIsDeleted(false);
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        baseMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 修改优惠券模板。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCouponTemplate(CouponTemplateBo bo) {
        CouponTemplate oldTemplate = validateCouponTemplateExists(bo.getId());
        validateCouponTemplateForSave(bo, oldTemplate);
        CouponTemplate entity = BeanUtil.toBean(bo, CouponTemplate.class);
        entity.setStatus(oldTemplate.getStatus());
        entity.setTakeCount(oldTemplate.getTakeCount());
        entity.setUseCount(oldTemplate.getUseCount());
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 修改优惠券模板状态。
     */
    @Override
    public Boolean updateCouponTemplateStatus(Long id, Integer status) {
        validateCouponTemplateExists(id);
        CouponTemplate entity = new CouponTemplate();
        entity.setId(id);
        entity.setStatus(status);
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 校验并批量删除优惠券模板。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            validateCouponTemplateExists(id);
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 校验优惠券模板保存参数。
     */
    private void validateCouponTemplateForSave(CouponTemplateBo bo, CouponTemplate oldTemplate) {
        if (bo == null) {
            throw new ServiceException("优惠券模板不能为空");
        }
        if (StringUtils.isBlank(bo.getName())) {
            throw new ServiceException("优惠券模板名称不能为空");
        }
        if (Objects.equals(bo.getTakeType(), TAKE_TYPE_USER)
                && !isTakeLimitCountUnlimited(bo.getTakeLimitCount())
                && !isTotalCountUnlimited(bo.getTotalCount())
                && bo.getTakeLimitCount() != null && bo.getTotalCount() != null
                && bo.getTakeLimitCount() > bo.getTotalCount()) {
            throw new ServiceException("总发放数量不能小于每人限领数量");
        }
        if (oldTemplate != null && Objects.equals(bo.getTakeType(), TAKE_TYPE_USER)
                && !isTotalCountUnlimited(bo.getTotalCount())
                && bo.getTotalCount() != null
                && bo.getTotalCount() < (oldTemplate.getTakeCount() == null ? 0 : oldTemplate.getTakeCount())) {
            throw new ServiceException("总发放数量不能小于已领取数量");
        }
        validateProductScope(bo.getProductScope(), bo.getProductScopeValues());
    }

    /**
     * 校验优惠券商品使用范围。
     */
    private void validateProductScope(Integer productScope, List<Long> productScopeValues) {
        if (Objects.equals(PRODUCT_SCOPE_SPU, productScope)) {
            if (CollectionUtils.isEmpty(productScopeValues)
                    || productSpuService.queryListByIds(productScopeValues).size() != productScopeValues.size()) {
                throw new ServiceException("优惠券适用商品不存在");
            }
            return;
        }
        if (Objects.equals(PRODUCT_SCOPE_CATEGORY, productScope)) {
            if (CollectionUtils.isEmpty(productScopeValues)
                    || productCategoryService.queryEnableList(productScopeValues).size() != productScopeValues.size()) {
                throw new ServiceException("优惠券适用分类不存在或已禁用");
            }
        }
    }

    /**
     * 校验优惠券模板存在。
     */
    private CouponTemplate validateCouponTemplateExists(Long id) {
        CouponTemplate template = baseMapper.selectById(id);
        if (template == null || Boolean.TRUE.equals(template.getIsDeleted())) {
            throw new ServiceException("优惠券模板不存在");
        }
        return template;
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

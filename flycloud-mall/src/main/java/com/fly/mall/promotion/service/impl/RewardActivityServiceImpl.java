package com.fly.mall.promotion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.utils.BeanUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.promotion.domain.RewardActivity;
import com.fly.mall.api.promotion.domain.bo.RewardActivityBo;
import com.fly.mall.api.promotion.domain.vo.RewardActivityVo;
import com.fly.mall.api.product.domain.vo.ProductCategoryVo;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;
import com.fly.mall.product.service.IProductCategoryService;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.promotion.mapper.RewardActivityMapper;
import com.fly.mall.promotion.service.IRewardActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 满减送活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class RewardActivityServiceImpl extends BaseServiceImpl<RewardActivityMapper, RewardActivity> implements IRewardActivityService {

    private static final int PRODUCT_SCOPE_ALL = 1;
    private static final int PRODUCT_SCOPE_SPU = 2;
    private static final int PRODUCT_SCOPE_CATEGORY = 3;

    private final RewardActivityMapper baseMapper;
    private final IProductSpuService productSpuService;
    private final IProductCategoryService productCategoryService;

    /**
     * 查询满减送活动详情。
     */
    @Override
    public RewardActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询满减送活动。
     */
    @Override
    public PageVo<RewardActivityVo> queryPageList(RewardActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<RewardActivity> lqw = buildQueryWrapper(bo);
        Page<RewardActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询满减送活动列表。
     */
    @Override
    public List<RewardActivityVo> queryList(RewardActivityBo bo) {
        LambdaQueryWrapper<RewardActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询指定商品当前匹配的满减送活动。
     */
    @Override
    public List<RewardActivityVo> getMatchRewardActivityListBySpuIds(Collection<Long> spuIds) {
        if (CollectionUtils.isEmpty(spuIds)) {
            return List.of();
        }
        List<ProductSpuVo> spus = productSpuService.queryListByIds(spuIds);
        if (CollectionUtils.isEmpty(spus)) {
            return List.of();
        }
        Map<Long, ProductSpuVo> spuMap = CollectionUtils.convertMap(spus, ProductSpuVo::getId);
        LocalDateTime now = LocalDateTime.now();
        List<RewardActivity> activities = baseMapper.selectList(new LambdaQueryWrapper<RewardActivity>()
                .eq(RewardActivity::getIsDeleted, false)
                .eq(RewardActivity::getStatus, StatusEnum.ENABLE.getStatus())
                .lt(RewardActivity::getStartTime, now)
                .gt(RewardActivity::getEndTime, now)
                .orderByDesc(RewardActivity::getId));
        if (CollectionUtils.isEmpty(activities)) {
            return List.of();
        }

        List<RewardActivityVo> result = new ArrayList<>();
        for (RewardActivity activity : activities) {
            List<Long> matchedSpuIds = new ArrayList<>();
            for (Long spuId : spuIds) {
                if (matchesRewardScope(activity, spuMap.get(spuId), spuId)) {
                    matchedSpuIds.add(spuId);
                }
            }
            if (!matchedSpuIds.isEmpty()) {
                RewardActivityVo vo = BeanUtils.toBean(activity, RewardActivityVo.class);
                vo.setSpuIds(matchedSpuIds);
                result.add(vo);
            }
        }
        return result;
    }

    /**
     * 新增或修改满减送活动。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(RewardActivityBo bo) {
        if (bo != null && bo.getId() != null) {
            return updateRewardActivity(bo);
        }
        createRewardActivity(bo);
        return true;
    }

    /**
     * 创建满减送活动。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRewardActivity(RewardActivityBo bo) {
        validateRewardActivityBase(bo);
        validateProductScope(bo.getProductScope(), bo.getProductScopeValues());
        validateRewardActivitySpuConflicts(null, bo);

        RewardActivity entity = BeanUtils.toBean(bo, RewardActivity.class);
        entity.setStatus(StatusEnum.ENABLE.getStatus());
        fillCreateFields(entity);
        baseMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 更新满减送活动。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRewardActivity(RewardActivityBo bo) {
        validateRewardActivityBase(bo);
        RewardActivity dbActivity = validateRewardActivityExists(bo.getId());
        if (Objects.equals(dbActivity.getStatus(), StatusEnum.DISABLE.getStatus())) {
            throw new ServiceException("已关闭的满减送活动不能修改");
        }
        validateProductScope(bo.getProductScope(), bo.getProductScopeValues());
        validateRewardActivitySpuConflicts(bo.getId(), bo);

        RewardActivity entity = BeanUtils.toBean(bo, RewardActivity.class);
        fillUpdateFields(entity);
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 关闭满减送活动。
     */
    @Override
    public Boolean closeRewardActivity(Long id) {
        RewardActivity dbActivity = validateRewardActivityExists(id);
        if (Objects.equals(dbActivity.getStatus(), StatusEnum.DISABLE.getStatus())) {
            throw new ServiceException("满减送活动已关闭，不能重复关闭");
        }
        RewardActivity entity = new RewardActivity();
        entity.setId(id);
        entity.setStatus(StatusEnum.DISABLE.getStatus());
        fillUpdateFields(entity);
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 删除满减送活动。
     */
    @Override
    public Boolean deleteRewardActivity(Long id) {
        RewardActivity dbActivity = validateRewardActivityExists(id);
        if (Objects.equals(dbActivity.getStatus(), StatusEnum.ENABLE.getStatus())) {
            throw new ServiceException("请先关闭满减送活动，再删除");
        }
        RewardActivity entity = new RewardActivity();
        entity.setId(id);
        entity.setIsDeleted(true);
        fillUpdateFields(entity);
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 填充创建字段。
     */
    private void fillCreateFields(RewardActivity entity) {
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setIsDeleted(false);
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
    }

    /**
     * 填充更新字段。
     */
    private void fillUpdateFields(RewardActivity entity) {
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 校验并批量删除满减送活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            deleteRewardActivity(id);
        }
        return true;
    }

    /**
     * 构建满减送活动查询条件。
     */
    private LambdaQueryWrapper<RewardActivity> buildQueryWrapper(RewardActivityBo bo) {
        LambdaQueryWrapper<RewardActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(RewardActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, RewardActivity::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), RewardActivity::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, RewardActivity::getStatus, bo.getStatus());
        lqw.eq(bo.getStartTime() != null, RewardActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, RewardActivity::getEndTime, bo.getEndTime());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), RewardActivity::getRemark, bo.getRemark());
        lqw.eq(bo.getConditionType() != null, RewardActivity::getConditionType, bo.getConditionType());
        lqw.eq(bo.getProductScope() != null, RewardActivity::getProductScope, bo.getProductScope());
        lqw.eq(bo.getLimit() != null, RewardActivity::getLimit, bo.getLimit());
        lqw.eq(bo.getDiscountPrice() != null, RewardActivity::getDiscountPrice, bo.getDiscountPrice());
        lqw.eq(bo.getFreeDelivery() != null, RewardActivity::getFreeDelivery, bo.getFreeDelivery());
        lqw.eq(bo.getPoint() != null, RewardActivity::getPoint, bo.getPoint());
        return lqw;
    }

    /**
     * 校验满减送活动基础字段。
     */
    private void validateRewardActivityBase(RewardActivityBo bo) {
        if (bo == null) {
            throw new ServiceException("满减送活动不能为空");
        }
        if (StringUtils.isBlank(bo.getName())) {
            throw new ServiceException("满减送活动名称不能为空");
        }
        if (bo.getStartTime() == null || bo.getEndTime() == null || !bo.getStartTime().isBefore(bo.getEndTime())) {
            throw new ServiceException("满减送活动时间不正确");
        }
        if (bo.getConditionType() == null) {
            throw new ServiceException("满减送活动条件类型不能为空");
        }
        if (bo.getProductScope() == null) {
            throw new ServiceException("满减送活动商品范围不能为空");
        }
        if (CollectionUtils.isEmpty(bo.getRules())) {
            throw new ServiceException("满减送活动优惠规则不能为空");
        }
    }

    /**
     * 校验满减送活动存在。
     */
    private RewardActivity validateRewardActivityExists(Long id) {
        RewardActivity activity = id == null ? null : baseMapper.selectById(id);
        if (activity == null || Boolean.TRUE.equals(activity.getIsDeleted())) {
            throw new ServiceException("满减送活动不存在");
        }
        return activity;
    }

    /**
     * 校验商品范围对应的商品或分类存在。
     */
    private void validateProductScope(Integer productScope, List<Long> productScopeValues) {
        if (Objects.equals(productScope, PRODUCT_SCOPE_ALL)) {
            return;
        }
        if (!Objects.equals(productScope, PRODUCT_SCOPE_SPU) && !Objects.equals(productScope, PRODUCT_SCOPE_CATEGORY)) {
            throw new ServiceException("满减送活动商品范围不正确");
        }
        if (CollectionUtils.isEmpty(productScopeValues)) {
            throw new ServiceException("满减送活动商品范围值不能为空");
        }
        Set<Long> scopeValueSet = new HashSet<>(productScopeValues);
        if (Objects.equals(productScope, PRODUCT_SCOPE_SPU)) {
            List<ProductSpuVo> spus = productSpuService.queryListByIds(scopeValueSet);
            if (spus.size() != scopeValueSet.size()) {
                throw new ServiceException("满减送活动选择的商品不存在");
            }
            return;
        }
        List<ProductCategoryVo> categories = productCategoryService.queryEnableList(scopeValueSet);
        if (categories.size() != scopeValueSet.size()) {
            throw new ServiceException("满减送活动选择的商品分类不存在或已禁用");
        }
    }

    /**
     * 校验满减送活动商品范围在同一时间段内是否冲突。
     */
    private void validateRewardActivitySpuConflicts(Long id, RewardActivityBo bo) {
        List<RewardActivity> activities = baseMapper.selectList(new LambdaQueryWrapper<RewardActivity>()
                .eq(RewardActivity::getIsDeleted, false)
                .eq(RewardActivity::getStatus, StatusEnum.ENABLE.getStatus()));
        if (CollectionUtils.isEmpty(activities)) {
            return;
        }
        for (RewardActivity activity : activities) {
            if (Objects.equals(activity.getId(), id) || !isTimeOverlap(activity.getStartTime(), activity.getEndTime(), bo.getStartTime(), bo.getEndTime())) {
                continue;
            }
            validateRewardActivityScopeConflict(activity, bo);
        }
    }

    /**
     * 校验单个已开启活动与本次提交活动的商品范围是否冲突。
     */
    private void validateRewardActivityScopeConflict(RewardActivity activity, RewardActivityBo bo) {
        if (Objects.equals(activity.getProductScope(), PRODUCT_SCOPE_ALL) || Objects.equals(bo.getProductScope(), PRODUCT_SCOPE_ALL)) {
            throw new ServiceException("满减送活动【" + activity.getName() + "】的商品范围与当前活动冲突");
        }
        if (Objects.equals(activity.getProductScope(), PRODUCT_SCOPE_CATEGORY)) {
            if (Objects.equals(bo.getProductScope(), PRODUCT_SCOPE_CATEGORY)
                    && hasIntersection(activity.getProductScopeValues(), bo.getProductScopeValues())) {
                throw new ServiceException("满减送活动【" + activity.getName() + "】的商品分类范围与当前活动重叠");
            }
            if (Objects.equals(bo.getProductScope(), PRODUCT_SCOPE_SPU)
                    && hasIntersection(activity.getProductScopeValues(), getCategoryIdsBySpuIds(bo.getProductScopeValues()))) {
                throw new ServiceException("满减送活动【" + activity.getName() + "】的商品分类范围已包含当前活动所选商品");
            }
        }
        if (Objects.equals(activity.getProductScope(), PRODUCT_SCOPE_SPU)) {
            if (Objects.equals(bo.getProductScope(), PRODUCT_SCOPE_SPU)
                    && hasIntersection(activity.getProductScopeValues(), bo.getProductScopeValues())) {
                throw new ServiceException("满减送活动【" + activity.getName() + "】的商品范围与当前活动重叠");
            }
            if (Objects.equals(bo.getProductScope(), PRODUCT_SCOPE_CATEGORY)
                    && hasIntersection(bo.getProductScopeValues(), getCategoryIdsBySpuIds(activity.getProductScopeValues()))) {
                throw new ServiceException("当前活动商品分类范围已包含满减送活动【" + activity.getName() + "】所选商品");
            }
        }
    }

    /**
     * 查询商品对应的分类编号集合。
     */
    private Set<Long> getCategoryIdsBySpuIds(Collection<Long> spuIds) {
        return CollectionUtils.convertSet(productSpuService.queryListByIds(spuIds), ProductSpuVo::getCategoryId);
    }

    /**
     * 判断两个时间段是否存在重叠。
     */
    private boolean isTimeOverlap(LocalDateTime startTime, LocalDateTime endTime,
                                  LocalDateTime otherStartTime, LocalDateTime otherEndTime) {
        return startTime != null && endTime != null && otherStartTime != null && otherEndTime != null
                && startTime.isBefore(otherEndTime) && otherStartTime.isBefore(endTime);
    }

    /**
     * 判断两个集合是否存在交集。
     */
    private boolean hasIntersection(Collection<Long> left, Collection<Long> right) {
        if (CollectionUtils.isEmpty(left) || CollectionUtils.isEmpty(right)) {
            return false;
        }
        Set<Long> rightSet = new HashSet<>(right);
        return left.stream().anyMatch(rightSet::contains);
    }

    /**
     * 判断商品是否命中满减送活动商品范围。
     */
    private boolean matchesRewardScope(RewardActivity activity, ProductSpuVo spu, Long spuId) {
        if (activity == null) {
            return false;
        }
        if (Objects.equals(activity.getProductScope(), PRODUCT_SCOPE_ALL)) {
            return true;
        }
        if (spu == null || CollectionUtils.isEmpty(activity.getProductScopeValues())) {
            return false;
        }
        if (Objects.equals(activity.getProductScope(), PRODUCT_SCOPE_SPU)) {
            return activity.getProductScopeValues().contains(spuId);
        }
        if (Objects.equals(activity.getProductScope(), PRODUCT_SCOPE_CATEGORY)) {
            return activity.getProductScopeValues().contains(spu.getCategoryId());
        }
        return false;
    }

}

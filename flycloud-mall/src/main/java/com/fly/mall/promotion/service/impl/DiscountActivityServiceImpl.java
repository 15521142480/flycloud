package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.utils.BeanUtils;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.promotion.domain.DiscountActivity;
import com.fly.mall.api.promotion.domain.DiscountProduct;
import com.fly.mall.api.promotion.domain.bo.DiscountActivityBo;
import com.fly.mall.api.promotion.domain.bo.DiscountProductBo;
import com.fly.mall.api.promotion.domain.vo.DiscountProductVo;
import com.fly.mall.api.promotion.domain.vo.DiscountActivityVo;
import com.fly.mall.api.product.domain.vo.ProductSkuVo;
import com.fly.mall.promotion.mapper.DiscountActivityMapper;
import com.fly.mall.promotion.mapper.DiscountProductMapper;
import com.fly.mall.promotion.service.IDiscountActivityService;
import com.fly.mall.product.service.IProductSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 限时折扣活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class DiscountActivityServiceImpl extends BaseServiceImpl<DiscountActivityMapper, DiscountActivity> implements IDiscountActivityService {

    private final DiscountActivityMapper baseMapper;
    private final DiscountProductMapper discountProductMapper;
    private final IProductSkuService productSkuService;

    /**
     * 查询限时折扣活动详情。
     */
    @Override
    public DiscountActivityVo queryById(Long id) {
        DiscountActivityVo vo = baseMapper.selectVoById(id);
        fillProducts(vo);
        return vo;
    }

    /**
     * 分页查询限时折扣活动。
     */
    @Override
    public PageVo<DiscountActivityVo> queryPageList(DiscountActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DiscountActivity> lqw = buildQueryWrapper(bo);
        Page<DiscountActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<DiscountActivityVo> page = this.build(result);
        fillProducts(page.getList());
        return page;
    }

    /**
     * 查询限时折扣活动列表。
     */
    @Override
    public List<DiscountActivityVo> queryList(DiscountActivityBo bo) {
        LambdaQueryWrapper<DiscountActivity> lqw = buildQueryWrapper(bo);
        List<DiscountActivityVo> list = baseMapper.selectVoList(lqw);
        fillProducts(list);
        return list;
    }

    /**
     * 创建限时折扣活动。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDiscountActivity(DiscountActivityBo bo) {
        validateDiscountProducts(null, bo);
        DiscountActivity activity = BeanUtil.toBean(bo, DiscountActivity.class);
        activity.setId(null);
        activity.setStatus(StatusEnum.ENABLE.getStatus());
        fillCreateFields(activity);
        baseMapper.insert(activity);
        saveDiscountProducts(activity, bo.getProducts());
        return activity.getId();
    }

    /**
     * 更新限时折扣活动。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDiscountActivity(DiscountActivityBo bo) {
        DiscountActivity oldActivity = validateDiscountActivityExists(bo == null ? null : bo.getId());
        if (StatusEnum.isDisable(oldActivity.getStatus())) {
            throw new ServiceException("已关闭的限时折扣活动不能修改");
        }
        validateDiscountProducts(bo.getId(), bo);

        DiscountActivity activity = BeanUtil.toBean(bo, DiscountActivity.class);
        activity.setStatus(oldActivity.getStatus());
        fillUpdateFields(activity);
        baseMapper.updateById(activity);

        discountProductMapper.deleteByActivityId(activity.getId());
        saveDiscountProducts(activity, bo.getProducts());
        return true;
    }

    /**
     * 关闭限时折扣活动。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean closeDiscountActivity(Long id) {
        DiscountActivity activity = validateDiscountActivityExists(id);
        if (StatusEnum.isDisable(activity.getStatus())) {
            throw new ServiceException("限时折扣活动已经关闭");
        }
        DiscountActivity updateActivity = new DiscountActivity();
        updateActivity.setId(id);
        updateActivity.setStatus(StatusEnum.DISABLE.getStatus());
        fillUpdateFields(updateActivity);
        baseMapper.updateById(updateActivity);

        DiscountProduct updateProduct = new DiscountProduct();
        updateProduct.setActivityId(id);
        updateProduct.setActivityStatus(StatusEnum.DISABLE.getStatus());
        fillUpdateFields(updateProduct);
        discountProductMapper.updateByActivityId(updateProduct);
        return true;
    }

    /**
     * 删除限时折扣活动。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDiscountActivity(Long id) {
        DiscountActivity activity = validateDiscountActivityExists(id);
        if (StatusEnum.isEnable(activity.getStatus())) {
            throw new ServiceException("未关闭的限时折扣活动不能删除");
        }
        boolean success = baseMapper.deleteById(id) > 0;
        discountProductMapper.deleteByActivityId(id);
        return success;
    }

    /**
     * 查询活动下的限时折扣商品。
     */
    @Override
    public List<DiscountProductVo> getDiscountProductsByActivityId(Long activityId) {
        return BeanUtils.toBean(discountProductMapper.selectListByActivityId(activityId), DiscountProductVo.class);
    }

    /**
     * 查询多个活动下的限时折扣商品。
     */
    @Override
    public List<DiscountProductVo> getDiscountProductsByActivityIds(Collection<Long> activityIds) {
        return BeanUtils.toBean(discountProductMapper.selectListByActivityId(activityIds), DiscountProductVo.class);
    }

    /**
     * 新增或修改限时折扣活动。
     */
    @Override
    public Boolean saveOrUpdate(DiscountActivityBo bo) {
        if (bo != null && bo.getId() != null) {
            return updateDiscountActivity(bo);
        }
        createDiscountActivity(bo);
        return true;
    }

    /**
     * 校验并批量删除限时折扣活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            deleteDiscountActivity(id);
        }
        return true;
    }

    /**
     * 构建限时折扣活动查询条件。
     */
    private LambdaQueryWrapper<DiscountActivity> buildQueryWrapper(DiscountActivityBo bo) {
        LambdaQueryWrapper<DiscountActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(DiscountActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DiscountActivity::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DiscountActivity::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, DiscountActivity::getStatus, bo.getStatus());
        lqw.eq(bo.getStartTime() != null, DiscountActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, DiscountActivity::getEndTime, bo.getEndTime());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), DiscountActivity::getRemark, bo.getRemark());
        return lqw;
    }

    /**
     * 校验活动存在。
     */
    private DiscountActivity validateDiscountActivityExists(Long id) {
        if (id == null) {
            throw new ServiceException("限时折扣活动编号不能为空");
        }
        DiscountActivity activity = baseMapper.selectById(id);
        if (activity == null || Boolean.TRUE.equals(activity.getIsDeleted())) {
            throw new ServiceException("限时折扣活动不存在");
        }
        return activity;
    }

    /**
     * 校验活动商品存在且没有和其它开启活动冲突。
     */
    private void validateDiscountProducts(Long activityId, DiscountActivityBo bo) {
        if (bo == null) {
            throw new ServiceException("限时折扣活动不能为空");
        }
        if (CollectionUtils.isEmpty(bo.getProducts())) {
            throw new ServiceException("限时折扣商品不能为空");
        }
        validateProductExists(bo.getProducts());
        validateDiscountActivityProductConflicts(activityId, bo.getProducts());
    }

    /**
     * 校验活动商品 SKU 存在且属于指定 SPU。
     */
    private void validateProductExists(List<DiscountProductBo> products) {
        List<Long> spuIds = CollectionUtils.convertList(products, DiscountProductBo::getSpuId);
        Map<Long, ProductSkuVo> skuMap = productSkuService.queryListBySpuIds(spuIds)
                .stream().collect(Collectors.toMap(ProductSkuVo::getId, item -> item, (first, second) -> first));
        for (DiscountProductBo product : products) {
            ProductSkuVo sku = skuMap.get(product.getSkuId());
            if (sku == null || !Objects.equals(sku.getSpuId(), product.getSpuId())) {
                throw new ServiceException("商品 SKU 不存在");
            }
        }
    }

    /**
     * 校验开启中的限时折扣活动商品不冲突。
     */
    private void validateDiscountActivityProductConflicts(Long activityId, List<DiscountProductBo> products) {
        List<DiscountActivity> activityList = baseMapper.selectList(new LambdaQueryWrapper<DiscountActivity>()
                .eq(DiscountActivity::getIsDeleted, false)
                .eq(DiscountActivity::getStatus, StatusEnum.ENABLE.getStatus()));
        activityList.removeIf(activity -> Objects.equals(activity.getId(), activityId));
        if (CollectionUtils.isEmpty(activityList)) {
            return;
        }
        List<DiscountProduct> productList = discountProductMapper.selectListByActivityId(
                CollectionUtils.convertList(activityList, DiscountActivity::getId));
        Map<Long, String> activityNameMap = activityList.stream()
                .collect(Collectors.toMap(DiscountActivity::getId, DiscountActivity::getName));
        List<Long> newSpuIds = CollectionUtils.convertList(products, DiscountProductBo::getSpuId);
        for (DiscountProduct product : productList) {
            if (newSpuIds.contains(product.getSpuId())) {
                throw new ServiceException("商品已参加限时折扣活动：" + activityNameMap.get(product.getActivityId()));
            }
        }
    }

    /**
     * 保存活动商品快照。
     */
    private void saveDiscountProducts(DiscountActivity activity, List<DiscountProductBo> products) {
        List<DiscountProduct> entities = new ArrayList<>(products.size());
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        for (DiscountProductBo product : products) {
            DiscountProduct entity = BeanUtil.toBean(product, DiscountProduct.class);
            entity.setId(null);
            entity.setActivityId(activity.getId());
            entity.setActivityName(activity.getName());
            entity.setActivityStatus(activity.getStatus());
            entity.setActivityStartTime(activity.getStartTime());
            entity.setActivityEndTime(activity.getEndTime());
            entity.setIsDeleted(false);
            entity.setCreateBy(userId);
            entity.setCreateTime(now);
            entity.setUpdateBy(userId);
            entity.setUpdateTime(now);
            entities.add(entity);
        }
        discountProductMapper.insertBatch(entities);
    }

    /**
     * 补充活动商品列表。
     */
    private void fillProducts(DiscountActivityVo vo) {
        if (vo == null) {
            return;
        }
        vo.setProducts(getDiscountProductsByActivityId(vo.getId()));
    }

    /**
     * 批量补充活动商品列表。
     */
    private void fillProducts(List<DiscountActivityVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<Long, List<DiscountProductVo>> productMap = getDiscountProductsByActivityIds(
                CollectionUtils.convertList(list, DiscountActivityVo::getId))
                .stream().collect(Collectors.groupingBy(DiscountProductVo::getActivityId));
        for (DiscountActivityVo activity : list) {
            activity.setProducts(productMap.getOrDefault(activity.getId(), List.of()));
        }
    }

    /**
     * 填充创建字段。
     */
    private void fillCreateFields(DiscountActivity activity) {
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        activity.setIsDeleted(false);
        activity.setCreateBy(userId);
        activity.setCreateTime(now);
        activity.setUpdateBy(userId);
        activity.setUpdateTime(now);
    }

    /**
     * 填充更新字段。
     */
    private void fillUpdateFields(DiscountActivity activity) {
        activity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        activity.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 填充更新字段。
     */
    private void fillUpdateFields(DiscountProduct product) {
        product.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        product.setUpdateTime(LocalDateTime.now());
    }

}

package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;
import com.fly.mall.api.promotion.domain.PointActivity;
import com.fly.mall.api.promotion.domain.bo.PointActivityBo;
import com.fly.mall.api.promotion.domain.bo.PointProductBo;
import com.fly.mall.api.promotion.domain.vo.AppPointActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppPointActivityRespVo;
import com.fly.mall.api.promotion.domain.vo.PointActivityVo;
import com.fly.mall.api.promotion.domain.vo.PointProductVo;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.promotion.mapper.PointActivityMapper;
import com.fly.mall.promotion.service.IPointActivityService;
import com.fly.mall.promotion.service.IPointProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 积分商城活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PointActivityServiceImpl extends BaseServiceImpl<PointActivityMapper, PointActivity> implements IPointActivityService {

    private final PointActivityMapper baseMapper;
    private final IPointProductService pointProductService;
    private final IProductSpuService productSpuService;

    /**
     * 查询积分商城活动详情。
     */
    @Override
    public PointActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询积分商城活动。
     */
    @Override
    public PageVo<PointActivityVo> queryPageList(PointActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PointActivity> lqw = buildQueryWrapper(bo);
        Page<PointActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询积分商城活动列表。
     */
    @Override
    public List<PointActivityVo> queryList(PointActivityBo bo) {
        LambdaQueryWrapper<PointActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 移动端分页查询积分商城活动。
     */
    @Override
    public PageVo<AppPointActivityRespVo> queryAppPageList(PageBo pageBo) {
        PointActivityBo bo = new PointActivityBo();
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        PageVo<PointActivityVo> page = queryPageList(bo, pageBo);
        PageVo<AppPointActivityRespVo> result = new PageVo<>();
        result.setList(buildAppActivityList(page.getList()));
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        return result;
    }

    /**
     * 移动端查询积分商城活动详情。
     */
    @Override
    public AppPointActivityDetailRespVo queryAppDetail(Long id) {
        PointActivityVo activity = queryById(id);
        if (activity == null || StatusEnum.isDisable(activity.getStatus())) {
            return null;
        }
        List<PointProductVo> products = queryProductListByActivityId(activity.getId());
        PointProductVo minProduct = getMinPointProduct(products);
        AppPointActivityDetailRespVo respVo = new AppPointActivityDetailRespVo();
        respVo.setId(activity.getId());
        respVo.setSpuId(activity.getSpuId());
        respVo.setStatus(activity.getStatus());
        respVo.setStock(activity.getStock());
        respVo.setTotalStock(activity.getTotalStock());
        respVo.setRemark(activity.getRemark());
        respVo.setPoint(minProduct == null ? null : minProduct.getPoint());
        respVo.setPrice(minProduct == null ? null : minProduct.getPrice());
        respVo.setProducts(products.stream()
                .map(product -> {
                    AppPointActivityDetailRespVo.Product item = new AppPointActivityDetailRespVo.Product();
                    item.setId(product.getId());
                    item.setSkuId(product.getSkuId());
                    item.setCount(product.getCount());
                    item.setPoint(product.getPoint());
                    item.setPrice(product.getPrice());
                    item.setStock(product.getStock());
                    return item;
                })
                .toList());
        return respVo;
    }

    /**
     * 移动端根据编号列表查询积分商城活动。
     */
    @Override
    public List<AppPointActivityRespVo> queryAppListByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<PointActivityVo> list = queryList(new PointActivityBo()).stream()
                .filter(activity -> ids.contains(activity.getId()))
                .filter(activity -> StatusEnum.isEnable(activity.getStatus()))
                .toList();
        Map<Long, PointActivityVo> activityMap = CollectionUtils.convertMap(list, PointActivityVo::getId);
        return buildAppActivityList(ids.stream()
                .map(activityMap::get)
                .filter(Objects::nonNull)
                .toList());
    }

    /**
     * 新增或修改积分商城活动。
     */
    @Override
    public Boolean saveOrUpdate(PointActivityBo bo) {
        PointActivity entity = BeanUtil.toBean(bo, PointActivity.class);
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
     * 校验并批量删除积分商城活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 构建积分商城活动查询条件。
     */
    private LambdaQueryWrapper<PointActivity> buildQueryWrapper(PointActivityBo bo) {
        LambdaQueryWrapper<PointActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(PointActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, PointActivity::getId, bo.getId());
        lqw.eq(bo.getSpuId() != null, PointActivity::getSpuId, bo.getSpuId());
        lqw.eq(bo.getStatus() != null, PointActivity::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), PointActivity::getRemark, bo.getRemark());
        lqw.eq(bo.getSort() != null, PointActivity::getSort, bo.getSort());
        lqw.eq(bo.getStock() != null, PointActivity::getStock, bo.getStock());
        lqw.eq(bo.getTotalStock() != null, PointActivity::getTotalStock, bo.getTotalStock());
        lqw.orderByDesc(PointActivity::getSort).orderByDesc(PointActivity::getCreateTime);
        return lqw;
    }

    /**
     * 拼接移动端积分商城活动列表返回对象。
     */
    private List<AppPointActivityRespVo> buildAppActivityList(List<PointActivityVo> activityList) {
        if (CollectionUtils.isEmpty(activityList)) {
            return Collections.emptyList();
        }
        Set<Long> activityIds = CollectionUtils.convertSet(activityList, PointActivityVo::getId);
        Map<Long, List<PointProductVo>> productMap = CollectionUtils.convertMultiMap(
                queryProductListByActivityIds(activityIds), PointProductVo::getActivityId);
        Map<Long, ProductSpuVo> spuMap = CollectionUtils.convertMap(
                productSpuService.queryListByIds(CollectionUtils.convertSet(activityList, PointActivityVo::getSpuId)),
                ProductSpuVo::getId);
        List<AppPointActivityRespVo> result = new ArrayList<>(activityList.size());
        for (PointActivityVo activity : activityList) {
            AppPointActivityRespVo item = new AppPointActivityRespVo();
            item.setId(activity.getId());
            item.setSpuId(activity.getSpuId());
            item.setStatus(activity.getStatus());
            item.setStock(activity.getStock());
            item.setTotalStock(activity.getTotalStock());
            ProductSpuVo spu = spuMap.get(activity.getSpuId());
            if (spu != null) {
                item.setSpuName(spu.getName());
                item.setPicUrl(spu.getPicUrl());
                item.setMarketPrice(spu.getMarketPrice());
            }
            PointProductVo minProduct = getMinPointProduct(productMap.get(activity.getId()));
            if (minProduct != null) {
                item.setPoint(minProduct.getPoint());
                item.setPrice(minProduct.getPrice());
            }
            result.add(item);
        }
        return result;
    }

    /**
     * 查询指定活动的积分商品。
     */
    private List<PointProductVo> queryProductListByActivityId(Long activityId) {
        PointProductBo bo = new PointProductBo();
        bo.setActivityId(activityId);
        return pointProductService.queryList(bo);
    }

    /**
     * 查询多个活动的积分商品。
     */
    private List<PointProductVo> queryProductListByActivityIds(Collection<Long> activityIds) {
        if (CollectionUtils.isEmpty(activityIds)) {
            return Collections.emptyList();
        }
        return pointProductService.queryList(new PointProductBo()).stream()
                .filter(product -> activityIds.contains(product.getActivityId()))
                .toList();
    }

    /**
     * 获取积分要求最低的 SKU。
     */
    private PointProductVo getMinPointProduct(List<PointProductVo> products) {
        if (CollectionUtils.isEmpty(products)) {
            return null;
        }
        return products.stream()
                .filter(product -> product.getPoint() != null)
                .min(Comparator.comparing(PointProductVo::getPoint))
                .orElse(null);
    }

}

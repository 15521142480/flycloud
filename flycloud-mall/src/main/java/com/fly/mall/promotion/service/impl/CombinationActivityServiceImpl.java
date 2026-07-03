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
import com.fly.mall.api.promotion.domain.CombinationActivity;
import com.fly.mall.api.promotion.domain.bo.CombinationProductBo;
import com.fly.mall.api.promotion.domain.bo.CombinationRecordBo;
import com.fly.mall.api.promotion.domain.bo.CombinationActivityBo;
import com.fly.mall.api.promotion.domain.vo.AppCombinationActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppCombinationActivityRespVo;
import com.fly.mall.api.promotion.domain.vo.CombinationActivityVo;
import com.fly.mall.api.promotion.domain.vo.CombinationProductVo;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.promotion.mapper.CombinationActivityMapper;
import com.fly.mall.promotion.service.ICombinationActivityService;
import com.fly.mall.promotion.service.ICombinationProductService;
import com.fly.mall.promotion.service.ICombinationRecordService;
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
 * 拼团活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class CombinationActivityServiceImpl extends BaseServiceImpl<CombinationActivityMapper, CombinationActivity> implements ICombinationActivityService {

    private final CombinationActivityMapper baseMapper;
    private final ICombinationProductService combinationProductService;
    private final ICombinationRecordService combinationRecordService;
    private final IProductSpuService productSpuService;

    /**
     * 查询拼团活动详情。
     */
    @Override
    public CombinationActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询拼团活动。
     */
    @Override
    public PageVo<CombinationActivityVo> queryPageList(CombinationActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<CombinationActivity> lqw = buildQueryWrapper(bo);
        Page<CombinationActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询拼团活动列表。
     */
    @Override
    public List<CombinationActivityVo> queryList(CombinationActivityBo bo) {
        LambdaQueryWrapper<CombinationActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 移动端分页查询拼团活动。
     */
    @Override
    public PageVo<AppCombinationActivityRespVo> queryAppPageList(PageBo pageBo) {
        CombinationActivityBo bo = new CombinationActivityBo();
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        PageVo<CombinationActivityVo> page = queryPageList(bo, pageBo);
        PageVo<AppCombinationActivityRespVo> result = new PageVo<>();
        result.setList(buildAppActivityList(page.getList()));
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        return result;
    }

    /**
     * 移动端根据编号列表查询拼团活动。
     */
    @Override
    public List<AppCombinationActivityRespVo> queryAppListByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<CombinationActivityVo> list = queryList(new CombinationActivityBo()).stream()
                .filter(activity -> ids.contains(activity.getId()))
                .filter(activity -> StatusEnum.isEnable(activity.getStatus()))
                .toList();
        Map<Long, CombinationActivityVo> activityMap = CollectionUtils.convertMap(list, CombinationActivityVo::getId);
        return buildAppActivityList(ids.stream()
                .map(activityMap::get)
                .filter(Objects::nonNull)
                .toList());
    }

    /**
     * 移动端查询拼团活动详情。
     */
    @Override
    public AppCombinationActivityDetailRespVo queryAppDetail(Long id) {
        CombinationActivityVo activity = queryById(id);
        if (activity == null || StatusEnum.isDisable(activity.getStatus())) {
            return null;
        }
        AppCombinationActivityDetailRespVo respVo = new AppCombinationActivityDetailRespVo();
        respVo.setId(activity.getId());
        respVo.setName(activity.getName());
        respVo.setStatus(activity.getStatus());
        respVo.setStartTime(activity.getStartTime());
        respVo.setEndTime(activity.getEndTime());
        respVo.setUserSize(activity.getUserSize());
        respVo.setSuccessCount(getSuccessCount(activity.getId()));
        respVo.setSpuId(activity.getSpuId());
        respVo.setTotalLimitCount(activity.getTotalLimitCount());
        respVo.setSingleLimitCount(activity.getSingleLimitCount());
        respVo.setProducts(queryProductListByActivityId(activity.getId()).stream()
                .map(product -> {
                    AppCombinationActivityDetailRespVo.Product item = new AppCombinationActivityDetailRespVo.Product();
                    item.setSkuId(product.getSkuId());
                    item.setCombinationPrice(product.getCombinationPrice());
                    return item;
                })
                .toList());
        return respVo;
    }

    /**
     * 新增或修改拼团活动。
     */
    @Override
    public Boolean saveOrUpdate(CombinationActivityBo bo) {
        CombinationActivity entity = BeanUtil.toBean(bo, CombinationActivity.class);
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
     * 校验并批量删除拼团活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            CombinationActivity entity = new CombinationActivity();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建拼团活动查询条件。
     */
    private LambdaQueryWrapper<CombinationActivity> buildQueryWrapper(CombinationActivityBo bo) {
        LambdaQueryWrapper<CombinationActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(CombinationActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, CombinationActivity::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), CombinationActivity::getName, bo.getName());
        lqw.eq(bo.getSpuId() != null, CombinationActivity::getSpuId, bo.getSpuId());
        lqw.eq(bo.getTotalLimitCount() != null, CombinationActivity::getTotalLimitCount, bo.getTotalLimitCount());
        lqw.eq(bo.getSingleLimitCount() != null, CombinationActivity::getSingleLimitCount, bo.getSingleLimitCount());
        lqw.eq(bo.getStartTime() != null, CombinationActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, CombinationActivity::getEndTime, bo.getEndTime());
        lqw.eq(bo.getUserSize() != null, CombinationActivity::getUserSize, bo.getUserSize());
        lqw.eq(bo.getVirtualGroup() != null, CombinationActivity::getVirtualGroup, bo.getVirtualGroup());
        lqw.eq(bo.getStatus() != null, CombinationActivity::getStatus, bo.getStatus());
        lqw.eq(bo.getLimitDuration() != null, CombinationActivity::getLimitDuration, bo.getLimitDuration());
        lqw.orderByDesc(CombinationActivity::getCreateTime);
        return lqw;
    }

    /**
     * 拼接移动端拼团活动列表返回对象。
     */
    private List<AppCombinationActivityRespVo> buildAppActivityList(List<CombinationActivityVo> activityList) {
        if (CollectionUtils.isEmpty(activityList)) {
            return Collections.emptyList();
        }
        Set<Long> activityIds = CollectionUtils.convertSet(activityList, CombinationActivityVo::getId);
        Map<Long, List<CombinationProductVo>> productMap = CollectionUtils.convertMultiMap(
                queryProductListByActivityIds(activityIds), CombinationProductVo::getActivityId);
        Map<Long, ProductSpuVo> spuMap = CollectionUtils.convertMap(
                productSpuService.queryListByIds(CollectionUtils.convertSet(activityList, CombinationActivityVo::getSpuId)),
                ProductSpuVo::getId);
        List<AppCombinationActivityRespVo> result = new ArrayList<>(activityList.size());
        for (CombinationActivityVo activity : activityList) {
            AppCombinationActivityRespVo item = new AppCombinationActivityRespVo();
            item.setId(activity.getId());
            item.setName(activity.getName());
            item.setUserSize(activity.getUserSize());
            item.setSpuId(activity.getSpuId());
            ProductSpuVo spu = spuMap.get(activity.getSpuId());
            if (spu != null) {
                item.setSpuName(spu.getName());
                item.setPicUrl(spu.getPicUrl());
                item.setMarketPrice(spu.getMarketPrice());
            }
            item.setCombinationPrice(productMap.getOrDefault(activity.getId(), Collections.emptyList()).stream()
                    .map(CombinationProductVo::getCombinationPrice)
                    .filter(Objects::nonNull)
                    .min(Comparator.naturalOrder())
                    .orElse(null));
            result.add(item);
        }
        return result;
    }

    /**
     * 查询指定活动的拼团商品。
     */
    private List<CombinationProductVo> queryProductListByActivityId(Long activityId) {
        CombinationProductBo bo = new CombinationProductBo();
        bo.setActivityId(activityId);
        return combinationProductService.queryList(bo);
    }

    /**
     * 查询多个活动的拼团商品。
     */
    private List<CombinationProductVo> queryProductListByActivityIds(Collection<Long> activityIds) {
        if (CollectionUtils.isEmpty(activityIds)) {
            return Collections.emptyList();
        }
        return combinationProductService.queryList(new CombinationProductBo()).stream()
                .filter(product -> activityIds.contains(product.getActivityId()))
                .toList();
    }

    /**
     * 查询成功拼团数量。
     */
    private Integer getSuccessCount(Long activityId) {
        CombinationRecordBo bo = new CombinationRecordBo();
        bo.setActivityId(activityId);
        bo.setStatus(20);
        return combinationRecordService.queryList(bo).size();
    }

}

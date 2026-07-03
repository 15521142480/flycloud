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
import com.fly.mall.api.promotion.domain.SeckillActivity;
import com.fly.mall.api.promotion.domain.bo.SeckillActivityBo;
import com.fly.mall.api.promotion.domain.bo.SeckillProductBo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillActivityNowRespVo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillActivityRespVo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillConfigRespVo;
import com.fly.mall.api.promotion.domain.vo.SeckillActivityVo;
import com.fly.mall.api.promotion.domain.vo.SeckillConfigVo;
import com.fly.mall.api.promotion.domain.vo.SeckillProductVo;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.promotion.mapper.SeckillActivityMapper;
import com.fly.mall.promotion.service.ISeckillActivityService;
import com.fly.mall.promotion.service.ISeckillConfigService;
import com.fly.mall.promotion.service.ISeckillProductService;
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
 * 秒杀活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class SeckillActivityServiceImpl extends BaseServiceImpl<SeckillActivityMapper, SeckillActivity> implements ISeckillActivityService {

    private final SeckillActivityMapper baseMapper;
    private final ISeckillConfigService seckillConfigService;
    private final ISeckillProductService seckillProductService;
    private final IProductSpuService productSpuService;

    /**
     * 查询秒杀活动详情。
     */
    @Override
    public SeckillActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询秒杀活动。
     */
    @Override
    public PageVo<SeckillActivityVo> queryPageList(SeckillActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<SeckillActivity> lqw = buildQueryWrapper(bo);
        Page<SeckillActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询秒杀活动列表。
     */
    @Override
    public List<SeckillActivityVo> queryList(SeckillActivityBo bo) {
        LambdaQueryWrapper<SeckillActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询当前秒杀活动，提供移动端首页使用。
     */
    @Override
    public AppSeckillActivityNowRespVo queryAppNow() {
        AppSeckillActivityNowRespVo respVo = new AppSeckillActivityNowRespVo();
        SeckillConfigVo config = seckillConfigService.queryCurrentConfig();
        if (config == null) {
            respVo.setActivities(Collections.emptyList());
            return respVo;
        }
        respVo.setConfig(convertAppConfig(config));
        respVo.setActivities(buildAppActivityList(queryCurrentActivityList(config.getId())));
        return respVo;
    }

    /**
     * 移动端分页查询秒杀活动。
     */
    @Override
    public PageVo<AppSeckillActivityRespVo> queryAppPageList(Long configId, PageBo pageBo) {
        List<SeckillActivityVo> list = queryAppActivityList(configId);
        long total = list.size();
        List<SeckillActivityVo> pageList = subPage(list, pageBo);
        PageVo<AppSeckillActivityRespVo> pageVo = new PageVo<>();
        pageVo.setList(buildAppActivityList(pageList));
        pageVo.setTotal(total);
        pageVo.setPages(calcPages(total, pageBo));
        return pageVo;
    }

    /**
     * 移动端查询秒杀活动详情。
     */
    @Override
    public AppSeckillActivityDetailRespVo queryAppDetail(Long id) {
        SeckillActivityVo activity = queryById(id);
        if (activity == null || StatusEnum.isDisable(activity.getStatus())) {
            return null;
        }
        AppSeckillActivityDetailRespVo respVo = new AppSeckillActivityDetailRespVo();
        respVo.setId(activity.getId());
        respVo.setName(activity.getName());
        respVo.setStatus(activity.getStatus());
        respVo.setStartTime(activity.getStartTime());
        respVo.setEndTime(activity.getEndTime());
        respVo.setSpuId(activity.getSpuId());
        respVo.setTotalLimitCount(activity.getTotalLimitCount());
        respVo.setSingleLimitCount(activity.getSingleLimitCount());
        respVo.setStock(activity.getStock());
        respVo.setTotalStock(activity.getTotalStock());
        respVo.setProducts(queryProductListByActivityId(id).stream()
                .map(product -> {
                    AppSeckillActivityDetailRespVo.Product item = new AppSeckillActivityDetailRespVo.Product();
                    item.setSkuId(product.getSkuId());
                    item.setSeckillPrice(product.getSeckillPrice());
                    item.setStock(product.getStock());
                    return item;
                })
                .toList());
        return respVo;
    }

    /**
     * 移动端根据编号列表查询秒杀活动。
     */
    @Override
    public List<AppSeckillActivityRespVo> queryAppListByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<SeckillActivityVo> list = queryList(new SeckillActivityBo()).stream()
                .filter(activity -> ids.contains(activity.getId()))
                .filter(this::isEnableActivity)
                .toList();
        Map<Long, SeckillActivityVo> activityMap = CollectionUtils.convertMap(list, SeckillActivityVo::getId);
        List<SeckillActivityVo> sortedList = ids.stream()
                .map(activityMap::get)
                .filter(Objects::nonNull)
                .toList();
        return buildAppActivityList(sortedList);
    }

    /**
     * 新增或修改秒杀活动。
     */
    @Override
    public Boolean saveOrUpdate(SeckillActivityBo bo) {
        SeckillActivity entity = BeanUtil.toBean(bo, SeckillActivity.class);
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
     * 校验并批量删除秒杀活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            SeckillActivity entity = new SeckillActivity();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建秒杀活动查询条件。
     */
    private LambdaQueryWrapper<SeckillActivity> buildQueryWrapper(SeckillActivityBo bo) {
        LambdaQueryWrapper<SeckillActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(SeckillActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, SeckillActivity::getId, bo.getId());
        lqw.eq(bo.getSpuId() != null, SeckillActivity::getSpuId, bo.getSpuId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), SeckillActivity::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, SeckillActivity::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), SeckillActivity::getRemark, bo.getRemark());
        lqw.eq(bo.getStartTime() != null, SeckillActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, SeckillActivity::getEndTime, bo.getEndTime());
        lqw.eq(bo.getSort() != null, SeckillActivity::getSort, bo.getSort());
        lqw.eq(bo.getTotalLimitCount() != null, SeckillActivity::getTotalLimitCount, bo.getTotalLimitCount());
        lqw.eq(bo.getSingleLimitCount() != null, SeckillActivity::getSingleLimitCount, bo.getSingleLimitCount());
        lqw.eq(bo.getStock() != null, SeckillActivity::getStock, bo.getStock());
        lqw.eq(bo.getTotalStock() != null, SeckillActivity::getTotalStock, bo.getTotalStock());
        lqw.orderByDesc(SeckillActivity::getSort).orderByDesc(SeckillActivity::getCreateTime);
        return lqw;
    }

    /**
     * 查询指定时段下可在移动端展示的秒杀活动。
     */
    private List<SeckillActivityVo> queryAppActivityList(Long configId) {
        return queryList(new SeckillActivityBo()).stream()
                .filter(this::isEnableActivity)
                .filter(activity -> configId == null || (activity.getConfigIds() != null && activity.getConfigIds().contains(configId)))
                .toList();
    }

    /**
     * 查询当前时段进行中的秒杀活动。
     */
    private List<SeckillActivityVo> queryCurrentActivityList(Long configId) {
        LocalDateTime now = LocalDateTime.now();
        return queryAppActivityList(configId).stream()
                .filter(activity -> activity.getStartTime() == null || !activity.getStartTime().isAfter(now))
                .filter(activity -> activity.getEndTime() == null || !activity.getEndTime().isBefore(now))
                .toList();
    }

    /**
     * 判断秒杀活动是否启用。
     */
    private boolean isEnableActivity(SeckillActivityVo activity) {
        return activity != null && StatusEnum.isEnable(activity.getStatus());
    }

    /**
     * 拼接移动端秒杀活动列表返回对象。
     */
    private List<AppSeckillActivityRespVo> buildAppActivityList(List<SeckillActivityVo> activityList) {
        if (CollectionUtils.isEmpty(activityList)) {
            return Collections.emptyList();
        }
        Set<Long> activityIds = CollectionUtils.convertSet(activityList, SeckillActivityVo::getId);
        Map<Long, List<SeckillProductVo>> productMap = CollectionUtils.convertMultiMap(
                queryProductListByActivityIds(activityIds), SeckillProductVo::getActivityId);
        Map<Long, ProductSpuVo> spuMap = CollectionUtils.convertMap(
                productSpuService.queryListByIds(CollectionUtils.convertSet(activityList, SeckillActivityVo::getSpuId)),
                ProductSpuVo::getId);
        List<AppSeckillActivityRespVo> result = new ArrayList<>(activityList.size());
        for (SeckillActivityVo activity : activityList) {
            AppSeckillActivityRespVo item = new AppSeckillActivityRespVo();
            item.setId(activity.getId());
            item.setName(activity.getName());
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
            item.setSeckillPrice(productMap.getOrDefault(activity.getId(), Collections.emptyList()).stream()
                    .map(SeckillProductVo::getSeckillPrice)
                    .filter(Objects::nonNull)
                    .min(Comparator.naturalOrder())
                    .orElse(null));
            result.add(item);
        }
        return result;
    }

    /**
     * 查询指定活动的秒杀商品。
     */
    private List<SeckillProductVo> queryProductListByActivityId(Long activityId) {
        SeckillProductBo bo = new SeckillProductBo();
        bo.setActivityId(activityId);
        return seckillProductService.queryList(bo);
    }

    /**
     * 查询多个活动的秒杀商品。
     */
    private List<SeckillProductVo> queryProductListByActivityIds(Collection<Long> activityIds) {
        if (CollectionUtils.isEmpty(activityIds)) {
            return Collections.emptyList();
        }
        return seckillProductService.queryList(new SeckillProductBo()).stream()
                .filter(product -> activityIds.contains(product.getActivityId()))
                .toList();
    }

    /**
     * 转换移动端秒杀时间段返回对象。
     */
    private AppSeckillConfigRespVo convertAppConfig(SeckillConfigVo config) {
        AppSeckillConfigRespVo respVo = new AppSeckillConfigRespVo();
        respVo.setId(config.getId());
        respVo.setStartTime(config.getStartTime());
        respVo.setEndTime(config.getEndTime());
        respVo.setSliderPicUrls(config.getSliderPicUrls());
        return respVo;
    }

    /**
     * 内存分页，适配按 JSON 配置编号过滤后的活动集合。
     */
    private List<SeckillActivityVo> subPage(List<SeckillActivityVo> list, PageBo pageBo) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        int pageNum = pageBo != null && pageBo.getPageNum() != null ? pageBo.getPageNum() : PageBo.MIN_PAGE_NUM;
        int pageSize = pageBo != null && pageBo.getPageSize() != null ? pageBo.getPageSize() : 10;
        int from = Math.max(0, (pageNum - 1) * pageSize);
        if (from >= list.size()) {
            return Collections.emptyList();
        }
        int to = Math.min(list.size(), from + pageSize);
        return list.subList(from, to);
    }

    /**
     * 计算内存分页总页数。
     */
    private Long calcPages(long total, PageBo pageBo) {
        int pageSize = pageBo != null && pageBo.getPageSize() != null ? pageBo.getPageSize() : 10;
        if (pageSize <= 0) {
            pageSize = 10;
        }
        return (total + pageSize - 1) / pageSize;
    }

}

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
import com.fly.mall.api.promotion.domain.BargainActivity;
import com.fly.mall.api.promotion.domain.bo.BargainActivityBo;
import com.fly.mall.api.promotion.domain.bo.BargainRecordBo;
import com.fly.mall.api.promotion.domain.vo.AppBargainActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppBargainActivityRespVo;
import com.fly.mall.api.promotion.domain.vo.BargainActivityVo;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.promotion.mapper.BargainActivityMapper;
import com.fly.mall.promotion.service.IBargainActivityService;
import com.fly.mall.promotion.service.IBargainRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 砍价活动 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class BargainActivityServiceImpl extends BaseServiceImpl<BargainActivityMapper, BargainActivity> implements IBargainActivityService {

    private final BargainActivityMapper baseMapper;
    private final IBargainRecordService bargainRecordService;
    private final IProductSpuService productSpuService;

    /**
     * 查询砍价活动详情。
     */
    @Override
    public BargainActivityVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询砍价活动。
     */
    @Override
    public PageVo<BargainActivityVo> queryPageList(BargainActivityBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BargainActivity> lqw = buildQueryWrapper(bo);
        Page<BargainActivityVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询砍价活动列表。
     */
    @Override
    public List<BargainActivityVo> queryList(BargainActivityBo bo) {
        LambdaQueryWrapper<BargainActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 移动端查询砍价活动列表。
     */
    @Override
    public List<AppBargainActivityRespVo> queryAppList(Integer count) {
        BargainActivityBo bo = new BargainActivityBo();
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        List<BargainActivityVo> list = queryList(bo);
        if (count != null && count > 0 && list.size() > count) {
            list = list.subList(0, count);
        }
        return buildAppActivityList(list);
    }

    /**
     * 移动端分页查询砍价活动。
     */
    @Override
    public PageVo<AppBargainActivityRespVo> queryAppPageList(PageBo pageBo) {
        BargainActivityBo bo = new BargainActivityBo();
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        PageVo<BargainActivityVo> page = queryPageList(bo, pageBo);
        PageVo<AppBargainActivityRespVo> result = new PageVo<>();
        result.setList(buildAppActivityList(page.getList()));
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        return result;
    }

    /**
     * 移动端查询砍价活动详情。
     */
    @Override
    public AppBargainActivityDetailRespVo queryAppDetail(Long id) {
        BargainActivityVo activity = queryById(id);
        if (activity == null || StatusEnum.isDisable(activity.getStatus())) {
            return null;
        }
        ProductSpuVo spu = productSpuService.queryById(activity.getSpuId());
        AppBargainActivityDetailRespVo respVo = new AppBargainActivityDetailRespVo();
        respVo.setId(activity.getId());
        respVo.setName(activity.getName());
        respVo.setStartTime(activity.getStartTime());
        respVo.setEndTime(activity.getEndTime());
        respVo.setSpuId(activity.getSpuId());
        respVo.setSkuId(activity.getSkuId());
        respVo.setStock(activity.getStock());
        respVo.setBargainFirstPrice(activity.getBargainFirstPrice());
        respVo.setBargainMinPrice(activity.getBargainMinPrice());
        respVo.setSuccessUserCount(getSuccessUserCount(activity.getId()));
        if (spu != null) {
            respVo.setPrice(spu.getPrice());
            respVo.setDescription(spu.getDescription());
            respVo.setPicUrl(spu.getPicUrl());
            respVo.setMarketPrice(spu.getMarketPrice());
        }
        return respVo;
    }

    /**
     * 新增或修改砍价活动。
     */
    @Override
    public Boolean saveOrUpdate(BargainActivityBo bo) {
        BargainActivity entity = BeanUtil.toBean(bo, BargainActivity.class);
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
     * 校验并批量删除砍价活动。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 构建砍价活动查询条件。
     */
    private LambdaQueryWrapper<BargainActivity> buildQueryWrapper(BargainActivityBo bo) {
        LambdaQueryWrapper<BargainActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(BargainActivity::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, BargainActivity::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), BargainActivity::getName, bo.getName());
        lqw.eq(bo.getStartTime() != null, BargainActivity::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, BargainActivity::getEndTime, bo.getEndTime());
        lqw.eq(bo.getStatus() != null, BargainActivity::getStatus, bo.getStatus());
        lqw.eq(bo.getSpuId() != null, BargainActivity::getSpuId, bo.getSpuId());
        lqw.eq(bo.getSkuId() != null, BargainActivity::getSkuId, bo.getSkuId());
        lqw.eq(bo.getBargainFirstPrice() != null, BargainActivity::getBargainFirstPrice, bo.getBargainFirstPrice());
        lqw.eq(bo.getBargainMinPrice() != null, BargainActivity::getBargainMinPrice, bo.getBargainMinPrice());
        lqw.eq(bo.getStock() != null, BargainActivity::getStock, bo.getStock());
        lqw.eq(bo.getTotalStock() != null, BargainActivity::getTotalStock, bo.getTotalStock());
        lqw.eq(bo.getHelpMaxCount() != null, BargainActivity::getHelpMaxCount, bo.getHelpMaxCount());
        lqw.eq(bo.getBargainCount() != null, BargainActivity::getBargainCount, bo.getBargainCount());
        lqw.eq(bo.getTotalLimitCount() != null, BargainActivity::getTotalLimitCount, bo.getTotalLimitCount());
        lqw.eq(bo.getRandomMinPrice() != null, BargainActivity::getRandomMinPrice, bo.getRandomMinPrice());
        lqw.eq(bo.getRandomMaxPrice() != null, BargainActivity::getRandomMaxPrice, bo.getRandomMaxPrice());
        lqw.orderByDesc(BargainActivity::getCreateTime);
        return lqw;
    }

    /**
     * 拼接移动端砍价活动列表返回对象。
     */
    private List<AppBargainActivityRespVo> buildAppActivityList(List<BargainActivityVo> activityList) {
        if (CollectionUtils.isEmpty(activityList)) {
            return Collections.emptyList();
        }
        Map<Long, ProductSpuVo> spuMap = CollectionUtils.convertMap(
                productSpuService.queryListByIds(CollectionUtils.convertSet(activityList, BargainActivityVo::getSpuId)),
                ProductSpuVo::getId);
        List<AppBargainActivityRespVo> result = new ArrayList<>(activityList.size());
        for (BargainActivityVo activity : activityList) {
            AppBargainActivityRespVo item = new AppBargainActivityRespVo();
            item.setId(activity.getId());
            item.setName(activity.getName());
            item.setStartTime(activity.getStartTime());
            item.setEndTime(activity.getEndTime());
            item.setSpuId(activity.getSpuId());
            item.setSkuId(activity.getSkuId());
            item.setStock(activity.getStock());
            item.setBargainMinPrice(activity.getBargainMinPrice());
            ProductSpuVo spu = spuMap.get(activity.getSpuId());
            if (spu != null) {
                item.setPicUrl(spu.getPicUrl());
                item.setMarketPrice(spu.getMarketPrice());
            }
            result.add(item);
        }
        return result;
    }

    /**
     * 查询砍价成功用户数量。
     */
    private Integer getSuccessUserCount(Long activityId) {
        BargainRecordBo bo = new BargainRecordBo();
        bo.setActivityId(activityId);
        bo.setStatus(20);
        return (int) bargainRecordService.queryList(bo).stream()
                .map(record -> record.getUserId())
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

}

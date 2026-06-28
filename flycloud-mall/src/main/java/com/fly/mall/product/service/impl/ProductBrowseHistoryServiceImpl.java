package com.fly.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.domain.product.ProductBrowseHistory;
import com.fly.mall.api.domain.product.bo.ProductBrowseHistoryBo;
import com.fly.mall.api.domain.product.vo.ProductBrowseHistoryVo;
import com.fly.mall.api.domain.product.vo.ProductSpuVo;
import com.fly.mall.product.mapper.ProductBrowseHistoryMapper;
import com.fly.mall.product.service.IProductBrowseHistoryService;
import com.fly.mall.product.service.IProductSpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 商品浏览记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class ProductBrowseHistoryServiceImpl extends BaseServiceImpl<ProductBrowseHistoryMapper, ProductBrowseHistory> implements IProductBrowseHistoryService {

    private static final int USER_STORE_MAXIMUM = 100;

    private final ProductBrowseHistoryMapper baseMapper;
    private final IProductSpuService productSpuService;

    /**
     * 查询商品浏览记录详情。
     */
    @Override
    public ProductBrowseHistoryVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品浏览记录。
     */
    @Override
    public PageVo<ProductBrowseHistoryVo> queryPageList(ProductBrowseHistoryBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductBrowseHistory> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(ProductBrowseHistory::getCreateTime);
        Page<ProductBrowseHistoryVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 分页查询用户商品浏览记录，并补充商品信息。
     */
    @Override
    public PageVo<ProductBrowseHistoryVo> queryUserBrowseHistoryPage(Long userId, ProductBrowseHistoryBo bo, PageBo pageBo) {
        if (bo == null) {
            bo = new ProductBrowseHistoryBo();
        }
        bo.setUserId(userId);
        bo.setUserDeleted(false);
        PageVo<ProductBrowseHistoryVo> page = queryPageList(bo, pageBo);
        fillSpuInfo(page.getList());
        return page;
    }

    /**
     * 查询商品浏览记录列表。
     */
    @Override
    public List<ProductBrowseHistoryVo> queryList(ProductBrowseHistoryBo bo) {
        LambdaQueryWrapper<ProductBrowseHistory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 创建商品浏览记录。
     */
    @Override
    public Boolean createBrowseHistory(Long userId, Long spuId) {
        if (userId == null || spuId == null) {
            return true;
        }

        ProductBrowseHistory old = selectByUserIdAndSpuId(userId, spuId);
        if (old != null) {
            baseMapper.deleteById(old.getId());
        } else {
            Page<ProductBrowseHistory> oldestPage = new Page<>(1, 1);
            LambdaQueryWrapper<ProductBrowseHistory> oldestWrapper = Wrappers.lambdaQuery();
            oldestWrapper.eq(ProductBrowseHistory::getUserId, userId);
            oldestWrapper.eq(ProductBrowseHistory::getIsDeleted, false);
            oldestWrapper.eq(ProductBrowseHistory::getUserDeleted, false);
            oldestWrapper.orderByAsc(ProductBrowseHistory::getCreateTime);
            Page<ProductBrowseHistory> page = baseMapper.selectPage(oldestPage, oldestWrapper);
            if (page.getTotal() >= USER_STORE_MAXIMUM && !CollectionUtils.isEmpty(page.getRecords())) {
                baseMapper.deleteById(page.getRecords().get(0).getId());
            }
        }

        LocalDateTime now = LocalDateTime.now();
        ProductBrowseHistory entity = new ProductBrowseHistory();
        entity.setUserId(userId);
        entity.setSpuId(spuId);
        entity.setUserDeleted(false);
        entity.setIsDeleted(false);
        entity.setCreateBy(String.valueOf(userId));
        entity.setCreateTime(now);
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(now);
        return baseMapper.insert(entity) > 0;
    }

    /**
     * 隐藏用户商品浏览记录。
     */
    @Override
    public Boolean hideUserBrowseHistory(Long userId, Collection<Long> spuIds) {
        ProductBrowseHistory entity = new ProductBrowseHistory();
        entity.setUserDeleted(true);
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(LocalDateTime.now());

        LambdaQueryWrapper<ProductBrowseHistory> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductBrowseHistory::getUserId, userId);
        lqw.eq(ProductBrowseHistory::getIsDeleted, false);
        lqw.eq(ProductBrowseHistory::getUserDeleted, false);
        lqw.in(!CollectionUtils.isEmpty(spuIds), ProductBrowseHistory::getSpuId, spuIds);
        return baseMapper.update(entity, lqw) >= 0;
    }

    /**
     * 新增或修改商品浏览记录。
     */
    @Override
    public Boolean saveOrUpdate(ProductBrowseHistoryBo bo) {
        ProductBrowseHistory entity = BeanUtil.toBean(bo, ProductBrowseHistory.class);
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
     * 校验并批量删除商品浏览记录。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            ProductBrowseHistory entity = new ProductBrowseHistory();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建商品浏览记录查询条件。
     */
    private LambdaQueryWrapper<ProductBrowseHistory> buildQueryWrapper(ProductBrowseHistoryBo bo) {
        LambdaQueryWrapper<ProductBrowseHistory> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductBrowseHistory::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, ProductBrowseHistory::getId, bo.getId());
        lqw.eq(bo.getSpuId() != null, ProductBrowseHistory::getSpuId, bo.getSpuId());
        lqw.eq(bo.getUserId() != null, ProductBrowseHistory::getUserId, bo.getUserId());
        lqw.eq(bo.getUserDeleted() != null, ProductBrowseHistory::getUserDeleted, bo.getUserDeleted());
        return lqw;
    }

    /**
     * 查询用户指定商品浏览记录。
     */
    private ProductBrowseHistory selectByUserIdAndSpuId(Long userId, Long spuId) {
        LambdaQueryWrapper<ProductBrowseHistory> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductBrowseHistory::getUserId, userId);
        lqw.eq(ProductBrowseHistory::getSpuId, spuId);
        lqw.last("LIMIT 1");
        return baseMapper.selectOne(lqw);
    }

    /**
     * 补充浏览记录列表里的商品展示信息。
     */
    private void fillSpuInfo(List<ProductBrowseHistoryVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> spuIds = CollectionUtils.convertList(list, ProductBrowseHistoryVo::getSpuId);
        Map<Long, ProductSpuVo> spuMap = CollectionUtils.convertMap(productSpuService.queryListByIds(spuIds), ProductSpuVo::getId);
        for (ProductBrowseHistoryVo history : list) {
            Optional.ofNullable(spuMap.get(history.getSpuId())).ifPresent(spu -> {
                history.setSpuName(spu.getName());
                history.setPicUrl(spu.getPicUrl());
                history.setPrice(spu.getPrice());
                history.setSalesCount(spu.getSalesCount());
                history.setStock(spu.getStock());
            });
        }
    }

}

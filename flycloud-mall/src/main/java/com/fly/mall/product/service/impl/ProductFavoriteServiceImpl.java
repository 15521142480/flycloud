package com.fly.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.exception.ServiceException;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.product.domain.ProductFavorite;
import com.fly.mall.api.product.domain.bo.ProductFavoriteBo;
import com.fly.mall.api.product.domain.vo.ProductFavoriteVo;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;
import com.fly.mall.product.mapper.ProductFavoriteMapper;
import com.fly.mall.product.service.IProductFavoriteService;
import com.fly.mall.product.service.IProductSpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 商品收藏 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class ProductFavoriteServiceImpl extends BaseServiceImpl<ProductFavoriteMapper, ProductFavorite> implements IProductFavoriteService {

    private final ProductFavoriteMapper baseMapper;
    private final IProductSpuService productSpuService;

    /**
     * 查询商品收藏详情。
     */
    @Override
    public ProductFavoriteVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品收藏。
     */
    @Override
    public PageVo<ProductFavoriteVo> queryPageList(ProductFavoriteBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductFavorite> lqw = buildQueryWrapper(bo);
        Page<ProductFavoriteVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 分页查询当前用户的商品收藏，并补充商品信息。
     */
    @Override
    public PageVo<ProductFavoriteVo> queryUserFavoritePage(Long userId, ProductFavoriteBo bo, PageBo pageBo) {
        if (bo == null) {
            bo = new ProductFavoriteBo();
        }
        bo.setUserId(userId);
        PageVo<ProductFavoriteVo> page = queryPageList(bo, pageBo);
        fillSpuInfo(page.getList());
        return page;
    }

    /**
     * 查询商品收藏列表。
     */
    @Override
    public List<ProductFavoriteVo> queryList(ProductFavoriteBo bo) {
        LambdaQueryWrapper<ProductFavorite> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 添加商品收藏。
     */
    @Override
    public Long createFavorite(Long userId, Long spuId) {
        ProductFavorite favorite = selectByUserIdAndSpuId(userId, spuId);
        if (favorite != null && !Boolean.TRUE.equals(favorite.getIsDeleted())) {
            throw new ServiceException("商品已收藏");
        }

        LocalDateTime now = LocalDateTime.now();
        if (favorite != null) {
            favorite.setIsDeleted(false);
            favorite.setUpdateBy(String.valueOf(userId));
            favorite.setUpdateTime(now);
            baseMapper.updateById(favorite);
            return favorite.getId();
        }

        ProductFavorite entity = new ProductFavorite();
        entity.setUserId(userId);
        entity.setSpuId(spuId);
        entity.setIsDeleted(false);
        entity.setCreateBy(String.valueOf(userId));
        entity.setCreateTime(now);
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(now);
        baseMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 取消商品收藏。
     */
    @Override
    public Boolean deleteFavorite(Long userId, Long spuId) {
        ProductFavorite favorite = selectByUserIdAndSpuId(userId, spuId);
        if (favorite == null || Boolean.TRUE.equals(favorite.getIsDeleted())) {
            throw new ServiceException("商品收藏不存在");
        }
        return baseMapper.deleteById(favorite.getId()) > 0;
    }

    /**
     * 判断用户是否已经收藏商品。
     */
    @Override
    public Boolean isFavoriteExists(Long userId, Long spuId) {
        ProductFavorite favorite = selectByUserIdAndSpuId(userId, spuId);
        return favorite != null && !Boolean.TRUE.equals(favorite.getIsDeleted());
    }

    /**
     * 查询用户商品收藏数量。
     */
    @Override
    public Long getFavoriteCount(Long userId) {
        LambdaQueryWrapper<ProductFavorite> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductFavorite::getUserId, userId);
        lqw.eq(ProductFavorite::getIsDeleted, false);
        return baseMapper.selectCount(lqw);
    }

    /**
     * 新增或修改商品收藏。
     */
    @Override
    public Boolean saveOrUpdate(ProductFavoriteBo bo) {
        ProductFavorite entity = BeanUtil.toBean(bo, ProductFavorite.class);
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
     * 校验并批量删除商品收藏。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 构建商品收藏查询条件。
     */
    private LambdaQueryWrapper<ProductFavorite> buildQueryWrapper(ProductFavoriteBo bo) {
        LambdaQueryWrapper<ProductFavorite> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductFavorite::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, ProductFavorite::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, ProductFavorite::getUserId, bo.getUserId());
        lqw.eq(bo.getSpuId() != null, ProductFavorite::getSpuId, bo.getSpuId());
        return lqw;
    }

    /**
     * 查询用户指定商品收藏记录。
     */
    private ProductFavorite selectByUserIdAndSpuId(Long userId, Long spuId) {
        LambdaQueryWrapper<ProductFavorite> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductFavorite::getUserId, userId);
        lqw.eq(ProductFavorite::getSpuId, spuId);
        lqw.last("LIMIT 1");
        return baseMapper.selectOne(lqw);
    }

    /**
     * 补充收藏列表里的商品展示信息。
     */
    private void fillSpuInfo(List<ProductFavoriteVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> spuIds = CollectionUtils.convertList(list, ProductFavoriteVo::getSpuId);
        Map<Long, ProductSpuVo> spuMap = CollectionUtils.convertMap(productSpuService.queryListByIds(spuIds), ProductSpuVo::getId);
        for (ProductFavoriteVo favorite : list) {
            Optional.ofNullable(spuMap.get(favorite.getSpuId())).ifPresent(spu -> {
                favorite.setSpuName(spu.getName());
                favorite.setPicUrl(spu.getPicUrl());
                favorite.setPrice(spu.getPrice());
                favorite.setSalesCount(spu.getSalesCount());
                favorite.setStock(spu.getStock());
            });
        }
    }

}

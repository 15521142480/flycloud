package com.fly.mall.product.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.product.domain.bo.ProductFavoriteBo;
import com.fly.mall.api.product.domain.vo.ProductFavoriteVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品收藏 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IProductFavoriteService {

    /**
     * 查询商品收藏详情。
     */
    ProductFavoriteVo queryById(Long id);

    /**
     * 分页查询商品收藏。
     */
    PageVo<ProductFavoriteVo> queryPageList(ProductFavoriteBo bo, PageBo pageBo);

    /**
     * 分页查询当前用户的商品收藏，并补充商品信息。
     */
    PageVo<ProductFavoriteVo> queryUserFavoritePage(Long userId, ProductFavoriteBo bo, PageBo pageBo);

    /**
     * 查询商品收藏列表。
     */
    List<ProductFavoriteVo> queryList(ProductFavoriteBo bo);

    /**
     * 添加商品收藏。
     */
    Long createFavorite(Long userId, Long spuId);

    /**
     * 取消商品收藏。
     */
    Boolean deleteFavorite(Long userId, Long spuId);

    /**
     * 判断用户是否已经收藏商品。
     */
    Boolean isFavoriteExists(Long userId, Long spuId);

    /**
     * 查询用户商品收藏数量。
     */
    Long getFavoriteCount(Long userId);

    /**
     * 新增或修改商品收藏。
     */
    Boolean saveOrUpdate(ProductFavoriteBo bo);

    /**
     * 校验并批量删除商品收藏。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.CartBo;
import com.fly.mall.api.trade.domain.vo.AppCartListRespVo;
import com.fly.mall.api.trade.domain.vo.CartVo;

import java.util.Collection;
import java.util.List;

/**
 * 购物车 Service 接口。
 *
 * @author lxs
 * @date 2026-06-29
 */
public interface ICartService {

    /**
     * 查询购物车详情。
     */
    CartVo queryById(Long id);

    /**
     * 分页查询购物车。
     */
    PageVo<CartVo> queryPageList(CartBo bo, PageBo pageBo);

    /**
     * 查询购物车列表。
     */
    List<CartVo> queryList(CartBo bo);

    /**
     * 查询用户购物车列表。
     */
    List<CartVo> queryUserCartList(Long userId);

    /**
     * 查询移动端用户购物车列表。
     */
    AppCartListRespVo queryAppCartList(Long userId);

    /**
     * 添加购物车商品。
     */
    Long addCart(Long userId, CartBo bo);

    /**
     * 更新购物车商品数量。
     */
    Boolean updateCartCount(Long userId, CartBo bo);

    /**
     * 更新购物车商品选中状态。
     */
    Boolean updateCartSelected(Long userId, CartBo bo);

    /**
     * 重置购物车商品规格和数量。
     */
    Boolean resetCart(Long userId, CartBo bo);

    /**
     * 删除用户购物车商品。
     */
    Boolean deleteCart(Long userId, Collection<Long> ids);

    /**
     * 查询用户购物车商品数量。
     */
    Integer getCartCount(Long userId);

    /**
     * 新增或修改购物车。
     */
    Boolean saveOrUpdate(CartBo bo);

    /**
     * 校验并批量删除购物车。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

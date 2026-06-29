package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.domain.product.ProductSku;
import com.fly.mall.api.domain.product.vo.AppProductPropertyValueDetailRespVo;
import com.fly.mall.api.domain.product.vo.AppProductSkuBaseRespVo;
import com.fly.mall.api.domain.product.vo.AppProductSpuBaseRespVo;
import com.fly.mall.api.domain.product.vo.ProductSkuVo;
import com.fly.mall.api.domain.product.vo.ProductSpuVo;
import com.fly.mall.api.domain.trade.Cart;
import com.fly.mall.api.domain.trade.bo.CartBo;
import com.fly.mall.api.domain.trade.vo.AppCartListRespVo;
import com.fly.mall.api.domain.trade.vo.CartVo;
import com.fly.mall.product.service.IProductSkuService;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.trade.mapper.CartMapper;
import com.fly.mall.trade.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 购物车 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-29
 */
@RequiredArgsConstructor
@Service
public class CartServiceImpl extends BaseServiceImpl<CartMapper, Cart> implements ICartService {

    private final CartMapper baseMapper;
    private final IProductSkuService productSkuService;
    private final IProductSpuService productSpuService;

    /**
     * 查询购物车详情。
     */
    @Override
    public CartVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询购物车。
     */
    @Override
    public PageVo<CartVo> queryPageList(CartBo bo, PageBo pageBo) {
        LambdaQueryWrapper<Cart> lqw = buildQueryWrapper(bo);
        Page<CartVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询购物车列表。
     */
    @Override
    public List<CartVo> queryList(CartBo bo) {
        LambdaQueryWrapper<Cart> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询用户购物车列表。
     */
    @Override
    public List<CartVo> queryUserCartList(Long userId) {
        CartBo bo = new CartBo();
        bo.setUserId(userId);
        List<CartVo> list = queryList(bo);
        list.sort(Comparator.comparing(CartVo::getId).reversed());
        fillProductInfo(list);
        return list;
    }

    /**
     * 查询移动端用户购物车列表。
     */
    @Override
    public AppCartListRespVo queryAppCartList(Long userId) {
        CartBo bo = new CartBo();
        bo.setUserId(userId);
        List<CartVo> carts = queryList(bo);
        carts.sort(Comparator.comparing(CartVo::getId).reversed());
        if (CollectionUtils.isEmpty(carts)) {
            AppCartListRespVo emptyRespVo = new AppCartListRespVo();
            emptyRespVo.setValidList(Collections.emptyList());
            emptyRespVo.setInvalidList(Collections.emptyList());
            return emptyRespVo;
        }

        List<Long> spuIds = CollectionUtils.convertList(carts, CartVo::getSpuId);
        List<Long> skuIds = CollectionUtils.convertList(carts, CartVo::getSkuId);
        Map<Long, ProductSpuVo> spuMap = CollectionUtils.convertMap(productSpuService.queryListByIds(spuIds), ProductSpuVo::getId);
        Map<Long, ProductSkuVo> skuMap = new java.util.HashMap<>();
        for (Long skuId : skuIds) {
            Optional.ofNullable(productSkuService.queryById(skuId)).ifPresent(sku -> skuMap.put(sku.getId(), sku));
        }
        return convertAppCartList(carts, spuMap, skuMap);
    }

    /**
     * 添加购物车商品。
     */
    @Override
    public Long addCart(Long userId, CartBo bo) {
        ProductSkuVo sku = checkProductSku(bo.getSkuId(), bo.getCount());
        Cart cart = selectByUserIdAndSkuId(userId, bo.getSkuId());
        if (cart != null) {
            Integer count = cart.getCount() == null ? 0 : cart.getCount();
            cart.setCount(count + bo.getCount());
            cart.setSelected(true);
            cart.setUpdateBy(String.valueOf(userId));
            cart.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(cart);
            return cart.getId();
        }

        LocalDateTime now = LocalDateTime.now();
        Cart entity = new Cart();
        entity.setUserId(userId);
        entity.setSpuId(sku.getSpuId());
        entity.setSkuId(sku.getId());
        entity.setCount(bo.getCount());
        entity.setSelected(true);
        entity.setIsDeleted(false);
        entity.setCreateBy(String.valueOf(userId));
        entity.setCreateTime(now);
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(now);
        baseMapper.insert(entity);
        return entity.getId();
    }

    /**
     * 更新购物车商品数量。
     */
    @Override
    public Boolean updateCartCount(Long userId, CartBo bo) {
        Cart cart = selectByIdAndUserId(bo.getId(), userId);
        if (cart == null) {
            throw new ServiceException("购物车商品不存在");
        }
        checkProductSku(cart.getSkuId(), bo.getCount());
        Cart entity = new Cart();
        entity.setId(cart.getId());
        entity.setCount(bo.getCount());
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 更新购物车商品选中状态。
     */
    @Override
    public Boolean updateCartSelected(Long userId, CartBo bo) {
        Cart entity = new Cart();
        entity.setSelected(bo.getSelected());
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(LocalDateTime.now());
        LambdaQueryWrapper<Cart> lqw = Wrappers.lambdaQuery();
        lqw.eq(Cart::getUserId, userId);
        lqw.eq(Cart::getIsDeleted, false);
        lqw.in(!CollectionUtils.isEmpty(bo.getIds()), Cart::getId, bo.getIds());
        return baseMapper.update(entity, lqw) >= 0;
    }

    /**
     * 重置购物车商品规格和数量。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetCart(Long userId, CartBo bo) {
        Cart oldCart = selectByIdAndUserId(bo.getId(), userId);
        if (oldCart == null) {
            throw new ServiceException("购物车商品不存在");
        }
        deleteCart(userId, List.of(oldCart.getId()));
        addCart(userId, bo);
        return true;
    }

    /**
     * 删除用户购物车商品。
     */
    @Override
    public Boolean deleteCart(Long userId, Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        Cart entity = new Cart();
        entity.setIsDeleted(true);
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(LocalDateTime.now());
        LambdaQueryWrapper<Cart> lqw = Wrappers.lambdaQuery();
        lqw.eq(Cart::getUserId, userId);
        lqw.eq(Cart::getIsDeleted, false);
        lqw.in(Cart::getId, ids);
        return baseMapper.update(entity, lqw) >= 0;
    }

    /**
     * 查询用户购物车商品数量。
     */
    @Override
    public Integer getCartCount(Long userId) {
        return queryUserCartList(userId).stream()
                .map(CartVo::getCount)
                .filter(java.util.Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    /**
     * 新增或修改购物车。
     */
    @Override
    public Boolean saveOrUpdate(CartBo bo) {
        Cart entity = BeanUtil.toBean(bo, Cart.class);
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
     * 校验并批量删除购物车。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            Cart entity = new Cart();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建购物车查询条件。
     */
    private LambdaQueryWrapper<Cart> buildQueryWrapper(CartBo bo) {
        LambdaQueryWrapper<Cart> lqw = Wrappers.lambdaQuery();
        lqw.eq(Cart::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, Cart::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, Cart::getUserId, bo.getUserId());
        lqw.eq(bo.getSpuId() != null, Cart::getSpuId, bo.getSpuId());
        lqw.eq(bo.getSkuId() != null, Cart::getSkuId, bo.getSkuId());
        lqw.eq(bo.getCount() != null, Cart::getCount, bo.getCount());
        lqw.eq(bo.getSelected() != null, Cart::getSelected, bo.getSelected());
        return lqw;
    }

    /**
     * 查询用户指定 SKU 的购物车商品。
     */
    private Cart selectByUserIdAndSkuId(Long userId, Long skuId) {
        LambdaQueryWrapper<Cart> lqw = Wrappers.lambdaQuery();
        lqw.eq(Cart::getUserId, userId);
        lqw.eq(Cart::getSkuId, skuId);
        lqw.eq(Cart::getIsDeleted, false);
        lqw.last("LIMIT 1");
        return baseMapper.selectOne(lqw);
    }

    /**
     * 查询用户购物车商品。
     */
    private Cart selectByIdAndUserId(Long id, Long userId) {
        LambdaQueryWrapper<Cart> lqw = Wrappers.lambdaQuery();
        lqw.eq(Cart::getId, id);
        lqw.eq(Cart::getUserId, userId);
        lqw.eq(Cart::getIsDeleted, false);
        lqw.last("LIMIT 1");
        return baseMapper.selectOne(lqw);
    }

    /**
     * 校验商品 SKU 是否可购买。
     */
    private ProductSkuVo checkProductSku(Long skuId, Integer count) {
        ProductSkuVo sku = productSkuService.queryById(skuId);
        if (sku == null) {
            throw new ServiceException("商品 SKU 不存在");
        }
        if (count == null || count <= 0) {
            throw new ServiceException("商品数量必须大于 0");
        }
        if (sku.getStock() != null && count > sku.getStock()) {
            throw new ServiceException("商品库存不足");
        }
        ProductSpuVo spu = productSpuService.queryById(sku.getSpuId());
        if (spu == null || !StatusEnum.isEnable(spu.getStatus())) {
            throw new ServiceException("商品不存在或已下架");
        }
        return sku;
    }

    /**
     * 补充购物车商品展示信息。
     */
    private void fillProductInfo(List<CartVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> spuIds = CollectionUtils.convertList(list, CartVo::getSpuId);
        List<Long> skuIds = CollectionUtils.convertList(list, CartVo::getSkuId);
        Map<Long, ProductSpuVo> spuMap = CollectionUtils.convertMap(productSpuService.queryListByIds(spuIds), ProductSpuVo::getId);
        Map<Long, ProductSkuVo> skuMap = new java.util.HashMap<>();
        for (Long skuId : skuIds) {
            Optional.ofNullable(productSkuService.queryById(skuId)).ifPresent(sku -> skuMap.put(sku.getId(), sku));
        }
        List<Long> deletedCartIds = new ArrayList<>();
        for (CartVo cart : list) {
            ProductSpuVo spu = spuMap.get(cart.getSpuId());
            ProductSkuVo sku = skuMap.get(cart.getSkuId());
            if (spu == null || sku == null) {
                deletedCartIds.add(cart.getId());
                continue;
            }
            cart.setSpuName(spu.getName());
            cart.setPicUrl(sku.getPicUrl() != null ? sku.getPicUrl() : spu.getPicUrl());
            cart.setPrice(sku.getPrice());
            cart.setStock(sku.getStock());
        }
        if (!CollectionUtils.isEmpty(deletedCartIds)) {
            deleteCart(UserUtils.getCurUserId(), deletedCartIds);
            list.removeIf(cart -> deletedCartIds.contains(cart.getId()));
        }
    }

    /**
     * 转换移动端购物车列表，并按商品有效性拆分。
     */
    private AppCartListRespVo convertAppCartList(List<CartVo> carts, Map<Long, ProductSpuVo> spuMap, Map<Long, ProductSkuVo> skuMap) {
        List<AppCartListRespVo.Cart> validList = new ArrayList<>(carts.size());
        List<AppCartListRespVo.Cart> invalidList = new ArrayList<>();
        for (CartVo cart : carts) {
            ProductSpuVo spu = spuMap.get(cart.getSpuId());
            ProductSkuVo sku = skuMap.get(cart.getSkuId());
            AppCartListRespVo.Cart cartRespVo = new AppCartListRespVo.Cart();
            cartRespVo.setId(cart.getId());
            cartRespVo.setCount(cart.getCount());
            cartRespVo.setSelected(cart.getSelected());
            cartRespVo.setSpu(convertAppProductSpu(spu));
            cartRespVo.setSku(convertAppProductSku(sku));
            if (isInvalidCart(spu, sku, cart)) {
                invalidList.add(cartRespVo);
            } else {
                validList.add(cartRespVo);
            }
        }

        AppCartListRespVo respVo = new AppCartListRespVo();
        respVo.setValidList(validList);
        respVo.setInvalidList(invalidList);
        return respVo;
    }

    /**
     * 判断购物项是否无效。
     */
    private boolean isInvalidCart(ProductSpuVo spu, ProductSkuVo sku, CartVo cart) {
        return spu == null
                || sku == null
                || !StatusEnum.isEnable(spu.getStatus())
                || Optional.ofNullable(spu.getStock()).orElse(0) <= 0
                || Optional.ofNullable(sku.getStock()).orElse(0) < Optional.ofNullable(cart.getCount()).orElse(0);
    }

    /**
     * 转换移动端商品 SPU 基础信息。
     */
    private AppProductSpuBaseRespVo convertAppProductSpu(ProductSpuVo spu) {
        if (spu == null) {
            return null;
        }
        AppProductSpuBaseRespVo respVo = new AppProductSpuBaseRespVo();
        respVo.setId(spu.getId());
        respVo.setName(spu.getName());
        respVo.setPicUrl(spu.getPicUrl());
        respVo.setCategoryId(spu.getCategoryId());
        respVo.setStock(spu.getStock());
        respVo.setStatus(spu.getStatus());
        return respVo;
    }

    /**
     * 转换移动端商品 SKU 基础信息。
     */
    private AppProductSkuBaseRespVo convertAppProductSku(ProductSkuVo sku) {
        if (sku == null) {
            return null;
        }
        AppProductSkuBaseRespVo respVo = new AppProductSkuBaseRespVo();
        respVo.setId(sku.getId());
        respVo.setPicUrl(sku.getPicUrl());
        respVo.setPrice(sku.getPrice());
        respVo.setStock(sku.getStock());
        respVo.setProperties(convertAppProductProperties(sku.getProperties()));
        return respVo;
    }

    /**
     * 转换移动端商品规格属性信息。
     */
    private List<AppProductPropertyValueDetailRespVo> convertAppProductProperties(List<ProductSku.Property> properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return Collections.emptyList();
        }
        List<AppProductPropertyValueDetailRespVo> respList = new ArrayList<>(properties.size());
        for (ProductSku.Property property : properties) {
            AppProductPropertyValueDetailRespVo respVo = new AppProductPropertyValueDetailRespVo();
            respVo.setPropertyId(property.getPropertyId());
            respVo.setPropertyName(property.getPropertyName());
            respVo.setValueId(property.getValueId());
            respVo.setValueName(property.getValueName());
            respList.add(respVo);
        }
        return respList;
    }

}

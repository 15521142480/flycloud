package com.fly.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.domain.product.ProductSku;
import com.fly.mall.api.domain.product.bo.ProductSkuBo;
import com.fly.mall.api.domain.product.vo.ProductSkuVo;
import com.fly.mall.product.mapper.ProductSkuMapper;
import com.fly.mall.product.service.IProductSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 商品 SKU Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class ProductSkuServiceImpl extends BaseServiceImpl<ProductSkuMapper, ProductSku> implements IProductSkuService {

    private final ProductSkuMapper baseMapper;

    /**
     * 查询商品 SKU 详情。
     */
    @Override
    public ProductSkuVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 根据 SPU 编号查询 SKU 列表。
     */
    @Override
    public List<ProductSkuVo> queryListBySpuId(Long spuId) {
        LambdaQueryWrapper<ProductSku> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductSku::getIsDeleted, false);
        lqw.eq(ProductSku::getSpuId, spuId);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 根据 SPU 编号集合查询 SKU 列表。
     */
    @Override
    public List<ProductSkuVo> queryListBySpuIds(Collection<Long> spuIds) {
        if (CollectionUtils.isEmpty(spuIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ProductSku> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductSku::getIsDeleted, false);
        lqw.in(ProductSku::getSpuId, spuIds);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 扣减商品 SKU 库存。
     */
    @Override
    public Boolean reduceStock(Long skuId, Integer count) {
        ProductSku oldSku = baseMapper.selectById(skuId);
        if (oldSku == null || Boolean.TRUE.equals(oldSku.getIsDeleted())) {
            throw new ServiceException("商品 SKU 不存在");
        }
        if (count == null || count <= 0) {
            throw new ServiceException("扣减库存数量必须大于 0");
        }
        Integer stock = oldSku.getStock() == null ? 0 : oldSku.getStock();
        if (stock < count) {
            throw new ServiceException("商品库存不足");
        }
        ProductSku entity = new ProductSku();
        entity.setId(skuId);
        entity.setStock(stock - count);
        entity.setSalesCount((oldSku.getSalesCount() == null ? 0 : oldSku.getSalesCount()) + count);
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 保存指定 SPU 的 SKU 列表。
     */
    @Override
    public Boolean saveSkuList(Long spuId, List<ProductSkuBo> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return true;
        }
        softDeleteBySpuId(spuId);
        List<ProductSku> entities = buildSkuList(spuId, skuList);
        return baseMapper.insertBatch(entities);
    }

    /**
     * 构建 SKU 实体列表。
     */
    @Override
    public List<ProductSku> buildSkuList(Long spuId, List<ProductSkuBo> skuList) {
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        List<ProductSku> entities = new ArrayList<>();
        for (ProductSkuBo skuBo : skuList) {
            ProductSku sku = BeanUtil.toBean(skuBo, ProductSku.class);
            sku.setId(null);
            sku.setSpuId(spuId);
            sku.setIsDeleted(false);
            sku.setCreateBy(userId);
            sku.setCreateTime(now);
            sku.setUpdateBy(userId);
            sku.setUpdateTime(now);
            entities.add(sku);
        }
        return entities;
    }

    /**
     * 根据 SPU 编号软删除历史 SKU。
     */
    private void softDeleteBySpuId(Long spuId) {
        List<ProductSkuVo> oldSkus = queryListBySpuId(spuId);
        for (ProductSkuVo oldSku : oldSkus) {
            ProductSku entity = new ProductSku();
            entity.setId(oldSku.getId());
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
    }

}

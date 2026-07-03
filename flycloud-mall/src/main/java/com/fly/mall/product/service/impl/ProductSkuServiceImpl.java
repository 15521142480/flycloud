package com.fly.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.product.domain.ProductProperty;
import com.fly.mall.api.product.domain.ProductPropertyValue;
import com.fly.mall.api.product.domain.ProductSku;
import com.fly.mall.api.product.domain.ProductSpu;
import com.fly.mall.api.product.domain.bo.ProductSkuBo;
import com.fly.mall.api.product.domain.vo.ProductSkuVo;
import com.fly.mall.product.mapper.ProductPropertyMapper;
import com.fly.mall.product.mapper.ProductPropertyValueMapper;
import com.fly.mall.product.mapper.ProductSkuMapper;
import com.fly.mall.product.mapper.ProductSpuMapper;
import com.fly.mall.product.service.IProductSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 商品 SKU Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class ProductSkuServiceImpl extends BaseServiceImpl<ProductSkuMapper, ProductSku> implements IProductSkuService {

    private final ProductSkuMapper baseMapper;
    private final ProductPropertyMapper productPropertyMapper;
    private final ProductPropertyValueMapper productPropertyValueMapper;
    private final ProductSpuMapper productSpuMapper;

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
        boolean success = baseMapper.updateById(entity) > 0;
        if (success) {
            updateSpuStockSummary(oldSku.getSpuId(), -count, count);
        }
        return success;
    }

    /**
     * 归还商品 SKU 库存，并回退已增加的销量。
     */
    @Override
    public Boolean increaseStock(Long skuId, Integer count) {
        ProductSku oldSku = baseMapper.selectById(skuId);
        if (oldSku == null || Boolean.TRUE.equals(oldSku.getIsDeleted())) {
            throw new ServiceException("商品 SKU 不存在");
        }
        if (count == null || count <= 0) {
            throw new ServiceException("归还库存数量必须大于 0");
        }
        ProductSku entity = new ProductSku();
        entity.setId(skuId);
        entity.setStock((oldSku.getStock() == null ? 0 : oldSku.getStock()) + count);
        entity.setSalesCount(Math.max(0, (oldSku.getSalesCount() == null ? 0 : oldSku.getSalesCount()) - count));
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        boolean success = baseMapper.updateById(entity) > 0;
        if (success) {
            updateSpuStockSummary(oldSku.getSpuId(), count, -count);
        }
        return success;
    }

    /**
     * 同步刷新 SPU 的汇总库存和销量。
     */
    private void updateSpuStockSummary(Long spuId, Integer stockIncr, Integer salesIncr) {
        if (spuId == null) {
            return;
        }
        ProductSpu oldSpu = productSpuMapper.selectById(spuId);
        if (oldSpu == null || Boolean.TRUE.equals(oldSpu.getIsDeleted())) {
            return;
        }
        ProductSpu entity = new ProductSpu();
        entity.setId(spuId);
        entity.setStock(Math.max(0, (oldSpu.getStock() == null ? 0 : oldSpu.getStock()) + stockIncr));
        entity.setSalesCount(Math.max(0, (oldSpu.getSalesCount() == null ? 0 : oldSpu.getSalesCount()) + salesIncr));
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        productSpuMapper.updateById(entity);
    }

    /**
     * 校验商品 SKU 列表。
     */
    @Override
    public void validateSkuList(List<ProductSkuBo> skuList, Boolean specType) {
        if (CollectionUtils.isEmpty(skuList)) {
            throw new ServiceException("商品 SKU 不存在");
        }
        if (Boolean.FALSE.equals(specType)) {
            setDefaultSkuProperty(skuList);
            return;
        }

        Set<Long> propertyIds = new HashSet<>();
        Set<Long> valueIds = new HashSet<>();
        for (ProductSkuBo sku : skuList) {
            if (CollectionUtils.isEmpty(sku.getProperties())) {
                throw new ServiceException("商品 SKU 不存在");
            }
            for (ProductSku.Property property : sku.getProperties()) {
                if (property == null || property.getPropertyId() == null || property.getValueId() == null) {
                    throw new ServiceException("属性项不存在");
                }
                propertyIds.add(property.getPropertyId());
                valueIds.add(property.getValueId());
            }
        }

        validatePropertiesExists(propertyIds);
        Map<Long, ProductPropertyValue> propertyValueMap = getPropertyValueMap(propertyIds, valueIds);
        refreshAndValidateSkuProperties(skuList, propertyValueMap);
    }

    /**
     * 单规格商品使用默认规格属性。
     */
    private void setDefaultSkuProperty(List<ProductSkuBo> skuList) {
        for (ProductSkuBo sku : skuList) {
            List<ProductSku.Property> properties = new ArrayList<>();
            properties.add(new ProductSku.Property(0L, "默认", 0L, "默认"));
            sku.setProperties(properties);
        }
    }

    /**
     * 校验规格属性项存在。
     */
    private void validatePropertiesExists(Set<Long> propertyIds) {
        if (CollectionUtils.isEmpty(propertyIds)) {
            throw new ServiceException("属性项不存在");
        }
        Long count = productPropertyMapper.selectCount(new LambdaQueryWrapper<ProductProperty>()
                .eq(ProductProperty::getIsDeleted, false)
                .in(ProductProperty::getId, propertyIds));
        if (!Objects.equals(count, (long) propertyIds.size())) {
            throw new ServiceException("属性项不存在");
        }
    }

    /**
     * 查询并校验规格属性值。
     */
    private Map<Long, ProductPropertyValue> getPropertyValueMap(Set<Long> propertyIds, Set<Long> valueIds) {
        List<ProductPropertyValue> values = productPropertyValueMapper.selectList(new LambdaQueryWrapper<ProductPropertyValue>()
                .eq(ProductPropertyValue::getIsDeleted, false)
                .in(ProductPropertyValue::getId, valueIds)
                .in(ProductPropertyValue::getPropertyId, propertyIds));
        if (values.size() != valueIds.size()) {
            throw new ServiceException("属性项不存在");
        }
        Map<Long, ProductPropertyValue> map = new HashMap<>(values.size());
        for (ProductPropertyValue value : values) {
            map.put(value.getId(), value);
        }
        return map;
    }

    /**
     * 校验 SKU 属性组合，并刷新属性值名称。
     */
    private void refreshAndValidateSkuProperties(List<ProductSkuBo> skuList, Map<Long, ProductPropertyValue> propertyValueMap) {
        int propertySize = skuList.get(0).getProperties().size();
        Set<Set<Long>> skuValueSets = new HashSet<>();
        for (ProductSkuBo sku : skuList) {
            if (sku.getProperties().size() != propertySize) {
                throw new ServiceException("一个 SPU 下的每个 SKU，其属性项必须一致");
            }
            Set<Long> skuPropertyIds = new HashSet<>();
            Set<Long> skuValueIds = new HashSet<>();
            for (ProductSku.Property property : sku.getProperties()) {
                ProductPropertyValue propertyValue = propertyValueMap.get(property.getValueId());
                if (propertyValue == null || !Objects.equals(propertyValue.getPropertyId(), property.getPropertyId())) {
                    throw new ServiceException("属性项不存在");
                }
                if (!skuPropertyIds.add(propertyValue.getPropertyId())) {
                    throw new ServiceException("商品 SKU 的属性组合存在重复");
                }
                skuValueIds.add(propertyValue.getId());
                property.setValueName(propertyValue.getName());
            }
            if (!skuValueSets.add(skuValueIds)) {
                throw new ServiceException("一个 SPU 下的每个 SKU，必须不重复");
            }
        }
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

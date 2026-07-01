package com.fly.mall.product.service.impl;

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
import com.fly.mall.api.product.domain.ProductSpu;
import com.fly.mall.api.product.domain.bo.ProductSkuBo;
import com.fly.mall.api.product.domain.bo.ProductSpuBo;
import com.fly.mall.api.product.domain.ProductSku;
import com.fly.mall.api.product.domain.vo.AppProductPropertyValueDetailRespVo;
import com.fly.mall.api.product.domain.vo.AppProductSpuDetailRespVo;
import com.fly.mall.api.product.domain.vo.AppProductSpuRespVo;
import com.fly.mall.api.product.domain.vo.ProductSkuVo;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;
import com.fly.mall.product.mapper.ProductSpuMapper;
import com.fly.mall.product.service.IProductSkuService;
import com.fly.mall.product.service.IProductSpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品 SPU Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-29
 */
@RequiredArgsConstructor
@Service
public class ProductSpuServiceImpl extends BaseServiceImpl<ProductSpuMapper, ProductSpu> implements IProductSpuService {

    private final ProductSpuMapper baseMapper;
    private final IProductSkuService productSkuService;

    /**
     * 查询商品 SPU 详情。
     */
    @Override
    public ProductSpuVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品 SPU 详情，并附带 SKU 列表。
     */
    @Override
    public ProductSpuVo queryDetailById(Long id) {
        ProductSpuVo spu = queryById(id);
        if (spu == null) {
            return null;
        }
        spu.setSkus(productSkuService.queryListBySpuId(id));
        return spu;
    }

    /**
     * 移动端查询商品 SPU 详情。
     */
    @Override
    public ProductSpuVo queryAppDetailById(Long id) {
        ProductSpuVo spu = queryDetailById(id);
        if (spu == null || !StatusEnum.isEnable(spu.getStatus())) {
            return null;
        }
        increaseBrowseCount(id);
        spu.setBrowseCount(spu.getBrowseCount() == null ? 1 : spu.getBrowseCount() + 1);
        if (spu.getSalesCount() != null && spu.getVirtualSalesCount() != null) {
            spu.setSalesCount(spu.getSalesCount() + spu.getVirtualSalesCount());
        }
        return spu;
    }

    /**
     * 移动端查询商品 SPU 明细返回对象。
     */
    @Override
    public AppProductSpuDetailRespVo queryAppDetailRespById(Long id) {
        ProductSpuVo spu = queryAppDetailById(id);
        if (spu == null) {
            return null;
        }
        return convertAppProductSpuDetail(spu);
    }

    /**
     * 修改商品 SPU 状态。
     */
    @Override
    public Boolean updateStatus(Long id, Integer status) {
        ProductSpu entity = new ProductSpu();
        entity.setId(id);
        entity.setStatus(status);
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 查询商品 SPU 状态数量。
     */
    @Override
    public Map<Integer, Long> queryStatusCount(ProductSpuBo bo) {
        LambdaQueryWrapper<ProductSpu> lqw = buildQueryWrapper(bo);
        lqw.select(ProductSpu::getStatus);
        return baseMapper.selectList(lqw).stream()
                .filter(item -> item.getStatus() != null)
                .collect(Collectors.groupingBy(ProductSpu::getStatus, Collectors.counting()));
    }

    /**
     * 分页查询商品 SPU。
     */
    @Override
    public PageVo<ProductSpuVo> queryPageList(ProductSpuBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductSpu> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(ProductSpu::getSort);
        Page<ProductSpuVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 移动端分页查询商品 SPU。
     */
    @Override
    public PageVo<AppProductSpuRespVo> queryAppPageList(ProductSpuBo bo, PageBo pageBo) {
        PageVo<ProductSpuVo> page = queryPageList(bo, pageBo);
        List<AppProductSpuRespVo> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(page.getList())) {
            for (ProductSpuVo spu : page.getList()) {
                list.add(convertAppProductSpu(spu));
            }
        }
        PageVo<AppProductSpuRespVo> respPage = new PageVo<>();
        respPage.setList(list);
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

    /**
     * 查询商品 SPU 列表。
     */
    @Override
    public List<ProductSpuVo> queryList(ProductSpuBo bo) {
        LambdaQueryWrapper<ProductSpu> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(ProductSpu::getSort);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 根据商品 SPU 编号集合查询商品列表。
     */
    @Override
    public List<ProductSpuVo> queryListByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }
        return baseMapper.selectVoBatchIds(ids);
    }

    /**
     * 移动端根据商品 SPU 编号集合查询商品列表。
     */
    @Override
    public List<AppProductSpuRespVo> queryAppListByIds(Collection<Long> ids) {
        List<ProductSpuVo> spus = queryListByIds(ids);
        if (CollectionUtils.isEmpty(spus)) {
            return Collections.emptyList();
        }
        List<AppProductSpuRespVo> list = new ArrayList<>(spus.size());
        for (ProductSpuVo spu : spus) {
            if (StatusEnum.isEnable(spu.getStatus())) {
                list.add(convertAppProductSpu(spu));
            }
        }
        return list;
    }

    /**
     * 新增或修改商品 SPU。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(ProductSpuBo bo) {
        refreshSkuSummary(bo);
        ProductSpu entity = BeanUtil.toBean(bo, ProductSpu.class);
        boolean isUpdate = entity.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(false);
        }

        boolean flag;
        if (isUpdate) {
            flag = baseMapper.updateById(entity) > 0;
        } else {
            entity.setCreateBy(userId);
            entity.setCreateTime(now);
            flag = baseMapper.insert(entity) > 0;
        }
        if (flag && !CollectionUtils.isEmpty(bo.getSkus())) {
            productSkuService.saveSkuList(entity.getId(), bo.getSkus());
        }
        return flag;
    }

    /**
     * 新增商品 SPU 并返回编号。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSpu(ProductSpuBo bo) {
        refreshSkuSummary(bo);
        ProductSpu entity = BeanUtil.toBean(bo, ProductSpu.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setIsDeleted(false);
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        baseMapper.insert(entity);
        if (!CollectionUtils.isEmpty(bo.getSkus())) {
            productSkuService.saveSkuList(entity.getId(), bo.getSkus());
        }
        return entity.getId();
    }

    /**
     * 校验并批量删除商品 SPU。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            ProductSpu entity = new ProductSpu();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建商品 SPU 查询条件。
     */
    private LambdaQueryWrapper<ProductSpu> buildQueryWrapper(ProductSpuBo bo) {
        LambdaQueryWrapper<ProductSpu> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductSpu::getIsDeleted, false);
        lqw.eq(bo.getId() != null, ProductSpu::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductSpu::getName, bo.getName());
        lqw.eq(bo.getCategoryId() != null, ProductSpu::getCategoryId, bo.getCategoryId());
        lqw.eq(bo.getBrandId() != null, ProductSpu::getBrandId, bo.getBrandId());
        lqw.eq(bo.getStatus() != null, ProductSpu::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 根据 SKU 列表刷新 SPU 冗余价格和库存。
     */
    private void refreshSkuSummary(ProductSpuBo bo) {
        if (CollectionUtils.isEmpty(bo.getSkus())) {
            return;
        }
        List<ProductSkuBo> skus = bo.getSkus();
        bo.setPrice(skus.stream().map(ProductSkuBo::getPrice).filter(Objects::nonNull).min(Comparator.naturalOrder()).orElse(0));
        bo.setMarketPrice(skus.stream().map(ProductSkuBo::getMarketPrice).filter(Objects::nonNull).min(Comparator.naturalOrder()).orElse(0));
        bo.setCostPrice(skus.stream().map(ProductSkuBo::getCostPrice).filter(Objects::nonNull).min(Comparator.naturalOrder()).orElse(0));
        bo.setStock(skus.stream().map(ProductSkuBo::getStock).filter(Objects::nonNull).reduce(0, Integer::sum));
    }

    /**
     * 增加商品浏览量。
     */
    private void increaseBrowseCount(Long id) {
        ProductSpu old = baseMapper.selectById(id);
        if (old == null) {
            return;
        }
        ProductSpu entity = new ProductSpu();
        entity.setId(id);
        entity.setBrowseCount(old.getBrowseCount() == null ? 1 : old.getBrowseCount() + 1);
        baseMapper.updateById(entity);
    }

    /**
     * 转换移动端商品 SPU 列表项。
     */
    private AppProductSpuRespVo convertAppProductSpu(ProductSpuVo spu) {
        AppProductSpuRespVo respVo = new AppProductSpuRespVo();
        respVo.setId(spu.getId());
        respVo.setName(spu.getName());
        respVo.setIntroduction(spu.getIntroduction());
        respVo.setCategoryId(spu.getCategoryId());
        respVo.setPicUrl(spu.getPicUrl());
        respVo.setSliderPicUrls(spu.getSliderPicUrls());
        respVo.setSpecType(spu.getSpecType());
        respVo.setPrice(spu.getPrice());
        respVo.setMarketPrice(spu.getMarketPrice());
        respVo.setStock(spu.getStock());
        respVo.setSalesCount(calculateSalesCount(spu));
        respVo.setDeliveryTypes(spu.getDeliveryTypes());
        return respVo;
    }

    /**
     * 转换移动端商品 SPU 明细。
     */
    private AppProductSpuDetailRespVo convertAppProductSpuDetail(ProductSpuVo spu) {
        AppProductSpuDetailRespVo respVo = new AppProductSpuDetailRespVo();
        respVo.setId(spu.getId());
        respVo.setName(spu.getName());
        respVo.setIntroduction(spu.getIntroduction());
        respVo.setDescription(spu.getDescription());
        respVo.setCategoryId(spu.getCategoryId());
        respVo.setPicUrl(spu.getPicUrl());
        respVo.setSliderPicUrls(spu.getSliderPicUrls());
        respVo.setSpecType(spu.getSpecType());
        respVo.setPrice(spu.getPrice());
        respVo.setMarketPrice(spu.getMarketPrice());
        respVo.setStock(spu.getStock());
        respVo.setSalesCount(calculateSalesCount(spu));
        respVo.setDeliveryTypes(spu.getDeliveryTypes());
        respVo.setSkus(convertAppProductSkuList(spu.getSkus()));
        return respVo;
    }

    /**
     * 转换移动端商品 SKU 列表。
     */
    private List<AppProductSpuDetailRespVo.Sku> convertAppProductSkuList(List<ProductSkuVo> skus) {
        if (CollectionUtils.isEmpty(skus)) {
            return Collections.emptyList();
        }
        List<AppProductSpuDetailRespVo.Sku> list = new ArrayList<>(skus.size());
        for (ProductSkuVo sku : skus) {
            AppProductSpuDetailRespVo.Sku respVo = new AppProductSpuDetailRespVo.Sku();
            respVo.setId(sku.getId());
            respVo.setProperties(convertAppProductProperties(sku.getProperties()));
            respVo.setPrice(sku.getPrice());
            respVo.setMarketPrice(sku.getMarketPrice());
            respVo.setPicUrl(sku.getPicUrl());
            respVo.setStock(sku.getStock());
            respVo.setWeight(sku.getWeight());
            respVo.setVolume(sku.getVolume());
            list.add(respVo);
        }
        return list;
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

    /**
     * 计算移动端展示销量。
     */
    private Integer calculateSalesCount(ProductSpuVo spu) {
        return Objects.requireNonNullElse(spu.getSalesCount(), 0)
                + Objects.requireNonNullElse(spu.getVirtualSalesCount(), 0);
    }

}

package com.fly.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.enums.mall.ProductSpuStatusEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.product.domain.ProductBrand;
import com.fly.mall.api.product.domain.ProductCategory;
import com.fly.mall.api.product.domain.ProductSpu;
import com.fly.mall.api.product.domain.bo.ProductSkuBo;
import com.fly.mall.api.product.domain.bo.ProductSpuBo;
import com.fly.mall.api.product.domain.ProductSku;
import com.fly.mall.api.product.domain.vo.AppProductPropertyValueDetailRespVo;
import com.fly.mall.api.product.domain.vo.AppProductSpuDetailRespVo;
import com.fly.mall.api.product.domain.vo.AppProductSpuRespVo;
import com.fly.mall.api.product.domain.vo.ProductSkuVo;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;
import com.fly.mall.product.mapper.ProductBrandMapper;
import com.fly.mall.product.mapper.ProductCategoryMapper;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 商品 SPU Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class ProductSpuServiceImpl extends BaseServiceImpl<ProductSpuMapper, ProductSpu> implements IProductSpuService {

    private static final int PRODUCT_ALERT_STOCK = 10;
    private static final int PRODUCT_CATEGORY_LEVEL_MIN = 2;

    private final ProductSpuMapper baseMapper;
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductBrandMapper productBrandMapper;
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
        if (spu == null || !ProductSpuStatusEnum.isEnable(spu.getStatus())) {
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
     * 为全部上架商品的销量加一。
     */
    @Override
    public int incrementSalesCountForOnSaleProducts() {
        return baseMapper.incrementSalesCountForOnSaleProducts();
    }

    /**
     * 查询商品 SPU 状态数量。
     */
    @Override
    public Map<Integer, Long> queryStatusCount(ProductSpuBo bo) {
        Map<Integer, Long> counts = new LinkedHashMap<>(5);
        counts.put(ProductSpuBo.FOR_SALE, selectCountByTab(bo, ProductSpuBo.FOR_SALE));
        counts.put(ProductSpuBo.IN_WAREHOUSE, selectCountByTab(bo, ProductSpuBo.IN_WAREHOUSE));
        counts.put(ProductSpuBo.SOLD_OUT, selectCountByTab(bo, ProductSpuBo.SOLD_OUT));
        counts.put(ProductSpuBo.ALERT_STOCK, selectCountByTab(bo, ProductSpuBo.ALERT_STOCK));
        counts.put(ProductSpuBo.RECYCLE_BIN, selectCountByTab(bo, ProductSpuBo.RECYCLE_BIN));
        return counts;
    }

    /**
     * 分页查询商品 SPU。
     */
    @Override
    public PageVo<ProductSpuVo> queryPageList(ProductSpuBo bo, PageBo pageBo) {
        LambdaQueryWrapper<ProductSpu> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(ProductSpu::getSort).orderByDesc(ProductSpu::getCreateTime);
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
            if (ProductSpuStatusEnum.isEnable(spu.getStatus())) {
                list.add(convertAppProductSpu(spu));
            }
        }

        if (!CollectionUtils.isEmpty(spus)) {
            Map<Long, AppProductSpuRespVo> spuMap = CollectionUtils.convertMap(list, AppProductSpuRespVo::getId);
            // 需要按照 ids 顺序返回。例如说：店铺装修选择了 [3, 1, 2] 三个商品，返回结果还是 [3, 1, 2]  这样的顺序
            list = CollectionUtils.convertList(ids, spuMap::get).stream()
                    .filter(Objects::nonNull)
                    .toList();
        }

        return list;
    }

    /**
     * 新增或修改商品 SPU。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(ProductSpuBo bo) {
        ProductSpu oldSpu = validateSpuForSave(bo);

        initSpuFromSkus(bo, bo.getSkus());
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
            entity.setStatus(oldSpu.getStatus());
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
        validateSpuForSave(bo);

        initSpuFromSkus(bo, bo.getSkus());
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
        
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 校验商品 SPU 保存参数。
     */
    private ProductSpu validateSpuForSave(ProductSpuBo bo) {
        ProductSpu oldSpu = null;
        if (bo.getId() != null) {
            oldSpu = baseMapper.selectById(bo.getId());
            if (oldSpu == null || Boolean.TRUE.equals(oldSpu.getIsDeleted())) {
                throw new ServiceException("商品 SPU 不存在");
            }
        }
        validateCategory(bo.getCategoryId());
        validateBrand(bo.getBrandId());
        productSkuService.validateSkuList(bo.getSkus(), bo.getSpecType());
        return oldSpu;
    }

    /**
     * 校验商品分类存在、启用且层级满足商品挂载要求。
     */
    private void validateCategory(Long id) {
        ProductCategory category = productCategoryMapper.selectById(id);
        if (category == null || Boolean.TRUE.equals(category.getIsDeleted())) {
            throw new ServiceException("商品分类不存在");
        }
        if (StatusEnum.isDisable(category.getStatus())) {
            throw new ServiceException("商品分类(" + category.getName() + ")已禁用，无法使用");
        }
        if (getCategoryLevel(id) < PRODUCT_CATEGORY_LEVEL_MIN) {
            throw new ServiceException("商品分类不正确，原因：必须使用第二级的商品分类及以下");
        }
    }

    /**
     * 计算商品分类层级。
     */
    private int getCategoryLevel(Long id) {
        if (Objects.equals(id, ProductCategory.PARENT_ID_ROOT)) {
            return 0;
        }
        int level = 1;
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            ProductCategory category = productCategoryMapper.selectById(id);
            if (category == null || Objects.equals(category.getParentId(), ProductCategory.PARENT_ID_ROOT)) {
                break;
            }
            level++;
            id = category.getParentId();
        }
        return level;
    }

    /**
     * 校验商品品牌存在且启用。
     */
    private void validateBrand(Long id) {
        ProductBrand brand = productBrandMapper.selectById(id);
        if (brand == null || Boolean.TRUE.equals(brand.getIsDeleted())) {
            throw new ServiceException("品牌不存在");
        }
        if (StatusEnum.isDisable(brand.getStatus())) {
            throw new ServiceException("品牌已禁用");
        }
    }

    /**
     * 查询指定分类绑定的商品 SPU 数量。
     */
    @Override
    public Long getSpuCountByCategoryId(Long categoryId) {
        if (categoryId == null) {
            return 0L;
        }
        LambdaQueryWrapper<ProductSpu> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductSpu::getIsDeleted, false);
        lqw.eq(ProductSpu::getCategoryId, categoryId);
        return baseMapper.selectCount(lqw);
    }

    /**
     * 构建商品 SPU 查询条件。
     */
    private LambdaQueryWrapper<ProductSpu> buildQueryWrapper(ProductSpuBo bo) {
        LambdaQueryWrapper<ProductSpu> lqw = buildBaseQueryWrapper(bo, true);
        appendTabQuery(lqw, bo.getTabType());
        return lqw;
    }

    /**
     * 构建商品 SPU 基础查询条件。
     */
    private LambdaQueryWrapper<ProductSpu> buildBaseQueryWrapper(ProductSpuBo bo, boolean includeStatus) {
        LambdaQueryWrapper<ProductSpu> lqw = Wrappers.lambdaQuery();
        lqw.eq(ProductSpu::getIsDeleted, false);
        lqw.eq(bo.getId() != null, ProductSpu::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductSpu::getName, bo.getName());
        lqw.eq(bo.getCategoryId() != null, ProductSpu::getCategoryId, bo.getCategoryId());
        lqw.eq(bo.getBrandId() != null, ProductSpu::getBrandId, bo.getBrandId());
        lqw.eq(includeStatus && bo.getStatus() != null, ProductSpu::getStatus, bo.getStatus());
        appendCreateTimeRange(lqw, bo);
        return lqw;
    }

    /**
     * 追加商品 SPU 创建时间查询范围。
     */
    private void appendCreateTimeRange(LambdaQueryWrapper<ProductSpu> lqw, ProductSpuBo bo) {
        Object beginTime = bo.getParams().get("beginCreateTime");
        Object endTime = bo.getParams().get("endCreateTime");
        if (beginTime instanceof LocalDateTime begin && endTime instanceof LocalDateTime end) {
            lqw.between(ProductSpu::getCreateTime, begin, end);
        } else if (beginTime instanceof LocalDateTime begin) {
            lqw.ge(ProductSpu::getCreateTime, begin);
        } else if (endTime instanceof LocalDateTime end) {
            lqw.le(ProductSpu::getCreateTime, end);
        }
    }

    /**
     * 查询指定商品 SPU Tab 的数量。
     */
    private Long selectCountByTab(ProductSpuBo bo, Integer tabType) {
        LambdaQueryWrapper<ProductSpu> lqw = buildBaseQueryWrapper(bo, false);
        appendTabQuery(lqw, tabType);
        return baseMapper.selectCount(lqw);
    }

    /**
     * 追加管理后台商品 SPU Tab 条件。
     */
    private void appendTabQuery(LambdaQueryWrapper<ProductSpu> lqw, Integer tabType) {
        if (Objects.equals(ProductSpuBo.FOR_SALE, tabType)) {
            lqw.eq(ProductSpu::getStatus, ProductSpuStatusEnum.ENABLE.getStatus());
        }
        if (Objects.equals(ProductSpuBo.IN_WAREHOUSE, tabType)) {
            lqw.eq(ProductSpu::getStatus, ProductSpuStatusEnum.DISABLE.getStatus());
        }
        if (Objects.equals(ProductSpuBo.SOLD_OUT, tabType)) {
            lqw.eq(ProductSpu::getStock, 0);
        }
        if (Objects.equals(ProductSpuBo.ALERT_STOCK, tabType)) {
            lqw.le(ProductSpu::getStock, PRODUCT_ALERT_STOCK)
                    .ne(ProductSpu::getStatus, ProductSpuStatusEnum.RECYCLE.getStatus());
        }
        if (Objects.equals(ProductSpuBo.RECYCLE_BIN, tabType)) {
            lqw.eq(ProductSpu::getStatus, ProductSpuStatusEnum.RECYCLE.getStatus());
        }
    }

    /**
     * 基于 SKU 的信息，初始化 SPU 的信息
     * 主要是计数相关的字段，例如说市场价、最大最小价、库存等等
     *
     * @param spu  商品 SPU
     * @param skus 商品 SKU 数组
     */
    private void initSpuFromSkus(ProductSpuBo spu, List<ProductSkuBo> skus) {
        // sku 单价最低的商品的价格
        spu.setPrice(CollectionUtils.getMinValue(skus, ProductSkuBo::getPrice));
        // sku 单价最低的商品的市场价格
        spu.setMarketPrice(CollectionUtils.getMinValue(skus, ProductSkuBo::getMarketPrice));
        // sku 单价最低的商品的成本价格
        spu.setCostPrice(CollectionUtils.getMinValue(skus, ProductSkuBo::getCostPrice));
        // skus 库存总数
        spu.setStock(CollectionUtils.getSumValue(skus, ProductSkuBo::getStock, Math::addExact));
        // 若是 spu 已有状态则不处理
        if (spu.getStatus() == null) {
            spu.setStatus(ProductSpuStatusEnum.ENABLE.getStatus()); // 默认状态为上架
            spu.setSalesCount(0); // 默认商品销量
            spu.setBrowseCount(0); // 默认商品浏览量
        }
    }

    /**
     * 根据 SKU 列表刷新 SPU 冗余价格和库存。
     */
//    private void refreshSkuSummary(ProductSpuBo bo) {
//        if (CollectionUtils.isEmpty(bo.getSkus())) {
//            return;
//        }
//        List<ProductSkuBo> skus = bo.getSkus();
//        bo.setPrice(skus.stream().map(ProductSkuBo::getPrice).filter(Objects::nonNull).min(Comparator.naturalOrder()).orElse(0));
//        bo.setMarketPrice(skus.stream().map(ProductSkuBo::getMarketPrice).filter(Objects::nonNull).min(Comparator.naturalOrder()).orElse(0));
//        bo.setCostPrice(skus.stream().map(ProductSkuBo::getCostPrice).filter(Objects::nonNull).min(Comparator.naturalOrder()).orElse(0));
//        bo.setStock(skus.stream().map(ProductSkuBo::getStock).filter(Objects::nonNull).reduce(0, Integer::sum));
//    }

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

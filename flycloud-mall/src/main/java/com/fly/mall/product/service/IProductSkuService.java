package com.fly.mall.product.service;

import com.fly.mall.api.product.domain.ProductSku;
import com.fly.mall.api.product.domain.bo.ProductSkuBo;
import com.fly.mall.api.product.domain.vo.ProductSkuVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品 SKU Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IProductSkuService {

    /**
     * 查询商品 SKU 详情。
     */
    ProductSkuVo queryById(Long id);

    /**
     * 根据 SPU 编号查询 SKU 列表。
     */
    List<ProductSkuVo> queryListBySpuId(Long spuId);

    /**
     * 根据 SPU 编号集合查询 SKU 列表。
     */
    List<ProductSkuVo> queryListBySpuIds(Collection<Long> spuIds);

    /**
     * 扣减商品 SKU 库存。
     */
    Boolean reduceStock(Long skuId, Integer count);

    /**
     * 保存指定 SPU 的 SKU 列表。
     */
    Boolean saveSkuList(Long spuId, List<ProductSkuBo> skuList);

    /**
     * 构建 SKU 实体列表。
     */
    List<ProductSku> buildSkuList(Long spuId, List<ProductSkuBo> skuList);

}

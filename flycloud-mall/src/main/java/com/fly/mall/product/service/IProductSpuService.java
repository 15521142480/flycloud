package com.fly.mall.product.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.product.domain.bo.ProductSpuBo;
import com.fly.mall.api.product.domain.vo.AppProductSpuDetailRespVo;
import com.fly.mall.api.product.domain.vo.AppProductSpuRespVo;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品 SPU Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IProductSpuService {

    /**
     * 查询商品 SPU 详情。
     */
    ProductSpuVo queryById(Long id);

    /**
     * 查询商品 SPU 详情，并附带 SKU 列表。
     */
    ProductSpuVo queryDetailById(Long id);

    /**
     * 移动端查询商品 SPU 详情。
     */
    ProductSpuVo queryAppDetailById(Long id);

    /**
     * 移动端查询商品 SPU 明细返回对象。
     */
    AppProductSpuDetailRespVo queryAppDetailRespById(Long id);

    /**
     * 修改商品 SPU 状态。
     */
    Boolean updateStatus(Long id, Integer status);

    /**
     * 查询商品 SPU 状态数量。
     */
    java.util.Map<Integer, Long> queryStatusCount(ProductSpuBo bo);

    /**
     * 分页查询商品 SPU。
     */
    PageVo<ProductSpuVo> queryPageList(ProductSpuBo bo, PageBo pageBo);

    /**
     * 移动端分页查询商品 SPU。
     */
    PageVo<AppProductSpuRespVo> queryAppPageList(ProductSpuBo bo, PageBo pageBo);

    /**
     * 查询商品 SPU 列表。
     */
    List<ProductSpuVo> queryList(ProductSpuBo bo);

    /**
     * 根据商品 SPU 编号集合查询商品列表。
     */
    List<ProductSpuVo> queryListByIds(Collection<Long> ids);

    /**
     * 移动端根据商品 SPU 编号集合查询商品列表。
     */
    List<AppProductSpuRespVo> queryAppListByIds(Collection<Long> ids);

    /**
     * 新增或修改商品 SPU。
     */
    Boolean saveOrUpdate(ProductSpuBo bo);

    /**
     * 新增商品 SPU 并返回编号。
     */
    Long createSpu(ProductSpuBo bo);

    /**
     * 校验并批量删除商品 SPU。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

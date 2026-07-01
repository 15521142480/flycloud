package com.fly.mall.product.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.product.domain.bo.ProductPropertyValueBo;
import com.fly.mall.api.product.domain.vo.ProductPropertyValueVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品属性值 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IProductPropertyValueService {

    /**
     * 查询商品属性值详情。
     */
    ProductPropertyValueVo queryById(Long id);

    /**
     * 分页查询商品属性值。
     */
    PageVo<ProductPropertyValueVo> queryPageList(ProductPropertyValueBo bo, PageBo pageBo);

    /**
     * 查询商品属性值列表。
     */
    List<ProductPropertyValueVo> queryList(ProductPropertyValueBo bo);

    /**
     * 新增或修改商品属性值。
     */
    Boolean saveOrUpdate(ProductPropertyValueBo bo);

    /**
     * 新增商品属性值并返回编号。
     */
    Long createPropertyValue(ProductPropertyValueBo bo);

    /**
     * 校验并批量删除商品属性值。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

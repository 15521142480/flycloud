package com.fly.mall.product.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.product.bo.ProductPropertyBo;
import com.fly.mall.api.domain.product.vo.ProductPropertyVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品属性 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IProductPropertyService {

    /**
     * 查询商品属性详情。
     */
    ProductPropertyVo queryById(Long id);

    /**
     * 分页查询商品属性。
     */
    PageVo<ProductPropertyVo> queryPageList(ProductPropertyBo bo, PageBo pageBo);

    /**
     * 查询商品属性列表。
     */
    List<ProductPropertyVo> queryList(ProductPropertyBo bo);

    /**
     * 新增或修改商品属性。
     */
    Boolean saveOrUpdate(ProductPropertyBo bo);

    /**
     * 校验并批量删除商品属性。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

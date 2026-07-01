package com.fly.mall.product.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.product.domain.bo.ProductCategoryBo;
import com.fly.mall.api.product.domain.vo.ProductCategoryVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品分类 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IProductCategoryService {

    /**
     * 查询商品分类详情。
     */
    ProductCategoryVo queryById(Long id);

    /**
     * 分页查询商品分类。
     */
    PageVo<ProductCategoryVo> queryPageList(ProductCategoryBo bo, PageBo pageBo);

    /**
     * 查询商品分类列表。
     */
    List<ProductCategoryVo> queryList(ProductCategoryBo bo);

    /**
     * 查询启用的商品分类列表。
     */
    List<ProductCategoryVo> queryEnableList(Collection<Long> ids);

    /**
     * 新增或修改商品分类。
     */
    Boolean saveOrUpdate(ProductCategoryBo bo);

    /**
     * 新增商品分类并返回编号。
     */
    Long createCategory(ProductCategoryBo bo);

    /**
     * 校验并批量删除商品分类。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

package com.fly.mall.product.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.product.domain.bo.ProductBrandBo;
import com.fly.mall.api.product.domain.vo.ProductBrandVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品品牌 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IProductBrandService {

    /**
     * 查询商品品牌详情。
     */
    ProductBrandVo queryById(Long id);

    /**
     * 分页查询商品品牌。
     */
    PageVo<ProductBrandVo> queryPageList(ProductBrandBo bo, PageBo pageBo);

    /**
     * 查询商品品牌列表。
     */
    List<ProductBrandVo> queryList(ProductBrandBo bo);

    /**
     * 新增或修改商品品牌。
     */
    Boolean saveOrUpdate(ProductBrandBo bo);

    /**
     * 校验并批量删除商品品牌。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

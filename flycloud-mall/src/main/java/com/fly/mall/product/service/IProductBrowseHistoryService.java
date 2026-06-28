package com.fly.mall.product.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.product.bo.ProductBrowseHistoryBo;
import com.fly.mall.api.domain.product.vo.ProductBrowseHistoryVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品浏览记录 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IProductBrowseHistoryService {

    /**
     * 查询商品浏览记录详情。
     */
    ProductBrowseHistoryVo queryById(Long id);

    /**
     * 分页查询商品浏览记录。
     */
    PageVo<ProductBrowseHistoryVo> queryPageList(ProductBrowseHistoryBo bo, PageBo pageBo);

    /**
     * 分页查询用户商品浏览记录，并补充商品信息。
     */
    PageVo<ProductBrowseHistoryVo> queryUserBrowseHistoryPage(Long userId, ProductBrowseHistoryBo bo, PageBo pageBo);

    /**
     * 查询商品浏览记录列表。
     */
    List<ProductBrowseHistoryVo> queryList(ProductBrowseHistoryBo bo);

    /**
     * 创建商品浏览记录。
     */
    Boolean createBrowseHistory(Long userId, Long spuId);

    /**
     * 隐藏用户商品浏览记录。
     */
    Boolean hideUserBrowseHistory(Long userId, Collection<Long> spuIds);

    /**
     * 新增或修改商品浏览记录。
     */
    Boolean saveOrUpdate(ProductBrowseHistoryBo bo);

    /**
     * 校验并批量删除商品浏览记录。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

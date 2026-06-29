package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.DiscountProductBo;
import com.fly.mall.api.promotion.domain.vo.DiscountProductVo;

import java.util.Collection;
import java.util.List;

/**
 * 限时折扣商品 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IDiscountProductService {

    /**
     * 查询限时折扣商品详情。
     */
    DiscountProductVo queryById(Long id);

    /**
     * 分页查询限时折扣商品。
     */
    PageVo<DiscountProductVo> queryPageList(DiscountProductBo bo, PageBo pageBo);

    /**
     * 查询限时折扣商品列表。
     */
    List<DiscountProductVo> queryList(DiscountProductBo bo);

    /**
     * 新增或修改限时折扣商品。
     */
    Boolean saveOrUpdate(DiscountProductBo bo);

    /**
     * 校验并批量删除限时折扣商品。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

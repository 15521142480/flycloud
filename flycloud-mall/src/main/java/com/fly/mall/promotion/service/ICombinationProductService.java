package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.CombinationProductBo;
import com.fly.mall.api.domain.promotion.vo.CombinationProductVo;

import java.util.Collection;
import java.util.List;

/**
 * 拼团商品 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface ICombinationProductService {

    /**
     * 查询拼团商品详情。
     */
    CombinationProductVo queryById(Long id);

    /**
     * 分页查询拼团商品。
     */
    PageVo<CombinationProductVo> queryPageList(CombinationProductBo bo, PageBo pageBo);

    /**
     * 查询拼团商品列表。
     */
    List<CombinationProductVo> queryList(CombinationProductBo bo);

    /**
     * 新增或修改拼团商品。
     */
    Boolean saveOrUpdate(CombinationProductBo bo);

    /**
     * 校验并批量删除拼团商品。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

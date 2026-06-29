package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.PointProductBo;
import com.fly.mall.api.promotion.domain.vo.PointProductVo;

import java.util.Collection;
import java.util.List;

/**
 * 积分商城商品 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IPointProductService {

    /**
     * 查询积分商城商品详情。
     */
    PointProductVo queryById(Long id);

    /**
     * 分页查询积分商城商品。
     */
    PageVo<PointProductVo> queryPageList(PointProductBo bo, PageBo pageBo);

    /**
     * 查询积分商城商品列表。
     */
    List<PointProductVo> queryList(PointProductBo bo);

    /**
     * 新增或修改积分商城商品。
     */
    Boolean saveOrUpdate(PointProductBo bo);

    /**
     * 校验并批量删除积分商城商品。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

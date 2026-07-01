package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.CouponBo;
import com.fly.mall.api.promotion.domain.vo.CouponVo;

import java.util.Collection;
import java.util.List;

/**
 * 优惠券 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ICouponService {

    /**
     * 查询优惠券详情。
     */
    CouponVo queryById(Long id);

    /**
     * 分页查询优惠券。
     */
    PageVo<CouponVo> queryPageList(CouponBo bo, PageBo pageBo);

    /**
     * 查询优惠券列表。
     */
    List<CouponVo> queryList(CouponBo bo);

    /**
     * 新增或修改优惠券。
     */
    Boolean saveOrUpdate(CouponBo bo);

    /**
     * 校验并批量删除优惠券。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

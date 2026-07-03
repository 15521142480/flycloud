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
     * 领取优惠券。
     */
    Long takeCoupon(Long templateId, Long userId, Integer takeType);

    /**
     * 使用优惠券。
     */
    Boolean useCoupon(Long id, Long userId, Long orderId);

    /**
     * 退回已使用优惠券。
     */
    Boolean returnUsedCoupon(Long id);

    /**
     * 过期未使用优惠券。
     */
    Integer expireCoupon();

    /**
     * 查询未使用优惠券数量。
     */
    Long getUnusedCouponCount(Long userId);

    /**
     * 查询用户领取指定模板的数量。
     */
    Integer getTakeCount(Long templateId, Long userId);

    /**
     * 判断用户是否可继续领取模板优惠券。
     */
    Boolean canTake(Long templateId, Long userId);

    /**
     * 新增或修改优惠券。
     */
    Boolean saveOrUpdate(CouponBo bo);

    /**
     * 校验并批量删除优惠券。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

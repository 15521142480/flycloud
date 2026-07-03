package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.CouponTemplateBo;
import com.fly.mall.api.promotion.domain.vo.CouponTemplateVo;

import java.util.Collection;
import java.util.List;

/**
 * 优惠券模板 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ICouponTemplateService {

    /**
     * 查询优惠券模板详情。
     */
    CouponTemplateVo queryById(Long id);

    /**
     * 分页查询优惠券模板。
     */
    PageVo<CouponTemplateVo> queryPageList(CouponTemplateBo bo, PageBo pageBo);

    /**
     * 查询优惠券模板列表。
     */
    List<CouponTemplateVo> queryList(CouponTemplateBo bo);

    List<CouponTemplateVo> queryList(Collection<Long> ids);

    /**
     * 查询可领取的优惠券模板列表。
     */
    List<CouponTemplateVo> queryCanTakeList(Integer productScope, Long productScopeValue, Integer count);

    /**
     * 判断每人限领数量是否不限制。
     */
    boolean isTakeLimitCountUnlimited(Integer takeLimitCount);

    /**
     * 判断总发放数量是否不限制。
     */
    boolean isTotalCountUnlimited(Integer totalCount);

    /**
     * 增减优惠券模板已领取数量。
     */
    Boolean updateCouponTemplateTakeCount(Long id, int incrCount);

    /**
     * 新增或修改优惠券模板。
     */
    Boolean saveOrUpdate(CouponTemplateBo bo);

    /**
     * 新增优惠券模板并返回编号。
     */
    Long createCouponTemplate(CouponTemplateBo bo);

    /**
     * 修改优惠券模板。
     */
    Boolean updateCouponTemplate(CouponTemplateBo bo);

    /**
     * 修改优惠券模板状态。
     */
    Boolean updateCouponTemplateStatus(Long id, Integer status);

    /**
     * 校验并批量删除优惠券模板。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

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

    /**
     * 新增或修改优惠券模板。
     */
    Boolean saveOrUpdate(CouponTemplateBo bo);

    /**
     * 校验并批量删除优惠券模板。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

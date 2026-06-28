package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.DiscountActivityBo;
import com.fly.mall.api.domain.promotion.vo.DiscountActivityVo;

import java.util.Collection;
import java.util.List;

/**
 * 限时折扣活动 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IDiscountActivityService {

    /**
     * 查询限时折扣活动详情。
     */
    DiscountActivityVo queryById(Long id);

    /**
     * 分页查询限时折扣活动。
     */
    PageVo<DiscountActivityVo> queryPageList(DiscountActivityBo bo, PageBo pageBo);

    /**
     * 查询限时折扣活动列表。
     */
    List<DiscountActivityVo> queryList(DiscountActivityBo bo);

    /**
     * 新增或修改限时折扣活动。
     */
    Boolean saveOrUpdate(DiscountActivityBo bo);

    /**
     * 校验并批量删除限时折扣活动。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.DiscountActivityBo;
import com.fly.mall.api.promotion.domain.vo.DiscountProductVo;
import com.fly.mall.api.promotion.domain.vo.DiscountActivityVo;

import java.util.Collection;
import java.util.List;

/**
 * 限时折扣活动 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
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
     * 创建限时折扣活动。
     */
    Long createDiscountActivity(DiscountActivityBo bo);

    /**
     * 更新限时折扣活动。
     */
    Boolean updateDiscountActivity(DiscountActivityBo bo);

    /**
     * 关闭限时折扣活动。
     */
    Boolean closeDiscountActivity(Long id);

    /**
     * 删除限时折扣活动。
     */
    Boolean deleteDiscountActivity(Long id);

    /**
     * 查询活动下的限时折扣商品。
     */
    List<DiscountProductVo> getDiscountProductsByActivityId(Long activityId);

    /**
     * 查询多个活动下的限时折扣商品。
     */
    List<DiscountProductVo> getDiscountProductsByActivityIds(Collection<Long> activityIds);

    /**
     * 新增或修改限时折扣活动。
     */
    Boolean saveOrUpdate(DiscountActivityBo bo);

    /**
     * 校验并批量删除限时折扣活动。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

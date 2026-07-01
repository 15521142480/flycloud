package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.RewardActivityBo;
import com.fly.mall.api.promotion.domain.vo.RewardActivityVo;

import java.util.Collection;
import java.util.List;

/**
 * 满减送活动 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IRewardActivityService {

    /**
     * 查询满减送活动详情。
     */
    RewardActivityVo queryById(Long id);

    /**
     * 分页查询满减送活动。
     */
    PageVo<RewardActivityVo> queryPageList(RewardActivityBo bo, PageBo pageBo);

    /**
     * 查询满减送活动列表。
     */
    List<RewardActivityVo> queryList(RewardActivityBo bo);

    /**
     * 新增或修改满减送活动。
     */
    Boolean saveOrUpdate(RewardActivityBo bo);

    /**
     * 校验并批量删除满减送活动。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

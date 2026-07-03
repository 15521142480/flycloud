package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.PointActivityBo;
import com.fly.mall.api.promotion.domain.vo.AppPointActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppPointActivityRespVo;
import com.fly.mall.api.promotion.domain.vo.PointActivityVo;

import java.util.Collection;
import java.util.List;

/**
 * 积分商城活动 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IPointActivityService {

    /**
     * 查询积分商城活动详情。
     */
    PointActivityVo queryById(Long id);

    /**
     * 分页查询积分商城活动。
     */
    PageVo<PointActivityVo> queryPageList(PointActivityBo bo, PageBo pageBo);

    /**
     * 查询积分商城活动列表。
     */
    List<PointActivityVo> queryList(PointActivityBo bo);

    /**
     * 移动端分页查询积分商城活动。
     */
    PageVo<AppPointActivityRespVo> queryAppPageList(PageBo pageBo);

    /**
     * 移动端查询积分商城活动详情。
     */
    AppPointActivityDetailRespVo queryAppDetail(Long id);

    /**
     * 移动端根据编号列表查询积分商城活动。
     */
    List<AppPointActivityRespVo> queryAppListByIds(Collection<Long> ids);

    /**
     * 新增或修改积分商城活动。
     */
    Boolean saveOrUpdate(PointActivityBo bo);

    /**
     * 校验并批量删除积分商城活动。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.CombinationActivityBo;
import com.fly.mall.api.promotion.domain.vo.AppCombinationActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppCombinationActivityRespVo;
import com.fly.mall.api.promotion.domain.vo.CombinationActivityVo;

import java.util.Collection;
import java.util.List;

/**
 * 拼团活动 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ICombinationActivityService {

    /**
     * 查询拼团活动详情。
     */
    CombinationActivityVo queryById(Long id);

    /**
     * 分页查询拼团活动。
     */
    PageVo<CombinationActivityVo> queryPageList(CombinationActivityBo bo, PageBo pageBo);

    /**
     * 查询拼团活动列表。
     */
    List<CombinationActivityVo> queryList(CombinationActivityBo bo);

    /**
     * 移动端分页查询拼团活动。
     */
    PageVo<AppCombinationActivityRespVo> queryAppPageList(PageBo pageBo);

    /**
     * 移动端根据编号列表查询拼团活动。
     */
    List<AppCombinationActivityRespVo> queryAppListByIds(Collection<Long> ids);

    /**
     * 移动端查询拼团活动详情。
     */
    AppCombinationActivityDetailRespVo queryAppDetail(Long id);

    /**
     * 新增或修改拼团活动。
     */
    Boolean saveOrUpdate(CombinationActivityBo bo);

    /**
     * 校验并批量删除拼团活动。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.SeckillActivityBo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillActivityDetailRespVo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillActivityNowRespVo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillActivityRespVo;
import com.fly.mall.api.promotion.domain.vo.SeckillActivityVo;

import java.util.Collection;
import java.util.List;

/**
 * 秒杀活动 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ISeckillActivityService {

    /**
     * 查询秒杀活动详情。
     */
    SeckillActivityVo queryById(Long id);

    /**
     * 分页查询秒杀活动。
     */
    PageVo<SeckillActivityVo> queryPageList(SeckillActivityBo bo, PageBo pageBo);

    /**
     * 查询秒杀活动列表。
     */
    List<SeckillActivityVo> queryList(SeckillActivityBo bo);

    /**
     * 查询当前秒杀活动，提供移动端首页使用。
     */
    AppSeckillActivityNowRespVo queryAppNow();

    /**
     * 移动端分页查询秒杀活动。
     */
    PageVo<AppSeckillActivityRespVo> queryAppPageList(Long configId, PageBo pageBo);

    /**
     * 移动端查询秒杀活动详情。
     */
    AppSeckillActivityDetailRespVo queryAppDetail(Long id);

    /**
     * 移动端根据编号列表查询秒杀活动。
     */
    List<AppSeckillActivityRespVo> queryAppListByIds(Collection<Long> ids);

    /**
     * 新增或修改秒杀活动。
     */
    Boolean saveOrUpdate(SeckillActivityBo bo);

    /**
     * 校验并批量删除秒杀活动。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.SeckillActivityBo;
import com.fly.mall.api.domain.promotion.vo.SeckillActivityVo;

import java.util.Collection;
import java.util.List;

/**
 * 秒杀活动 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
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
     * 新增或修改秒杀活动。
     */
    Boolean saveOrUpdate(SeckillActivityBo bo);

    /**
     * 校验并批量删除秒杀活动。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

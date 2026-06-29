package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.BargainActivityBo;
import com.fly.mall.api.promotion.domain.vo.BargainActivityVo;

import java.util.Collection;
import java.util.List;

/**
 * 砍价活动 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IBargainActivityService {

    /**
     * 查询砍价活动详情。
     */
    BargainActivityVo queryById(Long id);

    /**
     * 分页查询砍价活动。
     */
    PageVo<BargainActivityVo> queryPageList(BargainActivityBo bo, PageBo pageBo);

    /**
     * 查询砍价活动列表。
     */
    List<BargainActivityVo> queryList(BargainActivityBo bo);

    /**
     * 新增或修改砍价活动。
     */
    Boolean saveOrUpdate(BargainActivityBo bo);

    /**
     * 校验并批量删除砍价活动。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

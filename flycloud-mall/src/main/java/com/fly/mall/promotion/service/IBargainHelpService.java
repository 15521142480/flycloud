package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.BargainHelpBo;
import com.fly.mall.api.domain.promotion.vo.BargainHelpVo;

import java.util.Collection;
import java.util.List;

/**
 * 砍价助力 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IBargainHelpService {

    /**
     * 查询砍价助力详情。
     */
    BargainHelpVo queryById(Long id);

    /**
     * 分页查询砍价助力。
     */
    PageVo<BargainHelpVo> queryPageList(BargainHelpBo bo, PageBo pageBo);

    /**
     * 查询砍价助力列表。
     */
    List<BargainHelpVo> queryList(BargainHelpBo bo);

    /**
     * 新增或修改砍价助力。
     */
    Boolean saveOrUpdate(BargainHelpBo bo);

    /**
     * 校验并批量删除砍价助力。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

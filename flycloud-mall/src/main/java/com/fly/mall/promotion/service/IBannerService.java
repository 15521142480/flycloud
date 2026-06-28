package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.BannerBo;
import com.fly.mall.api.domain.promotion.vo.BannerVo;

import java.util.Collection;
import java.util.List;

/**
 * 轮播图 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IBannerService {

    /**
     * 查询轮播图详情。
     */
    BannerVo queryById(Long id);

    /**
     * 分页查询轮播图。
     */
    PageVo<BannerVo> queryPageList(BannerBo bo, PageBo pageBo);

    /**
     * 查询轮播图列表。
     */
    List<BannerVo> queryList(BannerBo bo);

    /**
     * 新增或修改轮播图。
     */
    Boolean saveOrUpdate(BannerBo bo);

    /**
     * 校验并批量删除轮播图。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

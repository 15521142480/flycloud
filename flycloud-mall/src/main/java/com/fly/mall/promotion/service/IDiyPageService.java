package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.DiyPageBo;
import com.fly.mall.api.domain.promotion.vo.DiyPageVo;

import java.util.Collection;
import java.util.List;

/**
 * 装修页面 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IDiyPageService {

    /**
     * 查询装修页面详情。
     */
    DiyPageVo queryById(Long id);

    /**
     * 分页查询装修页面。
     */
    PageVo<DiyPageVo> queryPageList(DiyPageBo bo, PageBo pageBo);

    /**
     * 查询装修页面列表。
     */
    List<DiyPageVo> queryList(DiyPageBo bo);

    /**
     * 新增或修改装修页面。
     */
    Boolean saveOrUpdate(DiyPageBo bo);

    /**
     * 校验并批量删除装修页面。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

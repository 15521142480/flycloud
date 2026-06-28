package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.DiyTemplateBo;
import com.fly.mall.api.domain.promotion.vo.DiyTemplateVo;

import java.util.Collection;
import java.util.List;

/**
 * 装修模板 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IDiyTemplateService {

    /**
     * 查询装修模板详情。
     */
    DiyTemplateVo queryById(Long id);

    /**
     * 分页查询装修模板。
     */
    PageVo<DiyTemplateVo> queryPageList(DiyTemplateBo bo, PageBo pageBo);

    /**
     * 查询装修模板列表。
     */
    List<DiyTemplateVo> queryList(DiyTemplateBo bo);

    /**
     * 新增或修改装修模板。
     */
    Boolean saveOrUpdate(DiyTemplateBo bo);

    /**
     * 校验并批量删除装修模板。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

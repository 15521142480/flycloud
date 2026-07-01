package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.DiyTemplateBo;
import com.fly.mall.api.promotion.domain.vo.AppDiyTemplatePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyTemplatePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyTemplateRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyTemplateVo;

import java.util.Collection;
import java.util.List;

/**
 * 装修模板 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IDiyTemplateService {

    /**
     * 查询装修模板详情。
     */
    DiyTemplateVo queryById(Long id);

    /**
     * 查询装修模板响应详情。
     */
    DiyTemplateRespVo queryRespById(Long id);

    /**
     * 分页查询装修模板。
     */
    PageVo<DiyTemplateVo> queryPageList(DiyTemplateBo bo, PageBo pageBo);

    /**
     * 分页查询装修模板响应列表。
     */
    PageVo<DiyTemplateRespVo> queryRespPageList(DiyTemplateBo bo, PageBo pageBo);

    /**
     * 查询装修模板列表。
     */
    List<DiyTemplateVo> queryList(DiyTemplateBo bo);

    /**
     * 查询装修模板响应列表。
     */
    List<DiyTemplateRespVo> queryRespList(DiyTemplateBo bo);

    /**
     * 创建装修模板。
     */
    Long createDiyTemplate(DiyTemplateBo bo);

    /**
     * 修改装修模板。
     */
    Boolean updateDiyTemplate(DiyTemplateBo bo);

    /**
     * 删除装修模板。
     */
    Boolean deleteDiyTemplate(Long id);

    /**
     * 使用装修模板。
     */
    Boolean useDiyTemplate(Long id);

    /**
     * 查询装修模板属性。
     */
    DiyTemplateVo queryPropertyById(Long id);

    /**
     * 查询后台装修模板属性响应。
     */
    DiyTemplatePropertyRespVo queryPropertyRespById(Long id);

    /**
     * 查询移动端装修模板属性响应。
     */
    AppDiyTemplatePropertyRespVo queryAppPropertyRespById(Long id);

    /**
     * 修改装修模板属性。
     */
    Boolean updateDiyTemplateProperty(DiyTemplateBo bo);

    /**
     * 查询正在使用的装修模板。
     */
    DiyTemplateVo queryUsedTemplate();

    /**
     * 查询正在使用的装修模板属性。
     */
    DiyTemplateVo queryUsedTemplateProperty();

    /**
     * 查询移动端正在使用的装修模板属性响应。
     */
    AppDiyTemplatePropertyRespVo queryUsedAppTemplateProperty();

    /**
     * 新增或修改装修模板。
     */
    Boolean saveOrUpdate(DiyTemplateBo bo);

    /**
     * 校验并批量删除装修模板。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.DiyPageBo;
import com.fly.mall.api.promotion.domain.vo.AppDiyPagePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPagePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPageRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPageVo;

import java.util.Collection;
import java.util.List;

/**
 * 装修页面 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IDiyPageService {

    /**
     * 查询装修页面详情。
     */
    DiyPageVo queryById(Long id);

    /**
     * 查询装修页面响应详情。
     */
    DiyPageRespVo queryRespById(Long id);

    /**
     * 分页查询装修页面。
     */
    PageVo<DiyPageVo> queryPageList(DiyPageBo bo, PageBo pageBo);

    /**
     * 分页查询装修页面响应列表。
     */
    PageVo<DiyPageRespVo> queryRespPageList(DiyPageBo bo, PageBo pageBo);

    /**
     * 查询装修页面列表。
     */
    List<DiyPageVo> queryList(DiyPageBo bo);

    /**
     * 查询装修页面响应列表。
     */
    List<DiyPageRespVo> queryRespList(DiyPageBo bo);

    /**
     * 创建装修页面。
     */
    Long createDiyPage(DiyPageBo bo);

    /**
     * 修改装修页面。
     */
    Boolean updateDiyPage(DiyPageBo bo);

    /**
     * 删除装修页面。
     */
    Boolean deleteDiyPage(Long id);

    /**
     * 查询装修页面属性。
     */
    DiyPageVo queryPropertyById(Long id);

    /**
     * 查询后台装修页面属性响应。
     */
    DiyPagePropertyRespVo queryPropertyRespById(Long id);

    /**
     * 查询移动端装修页面属性响应。
     */
    AppDiyPagePropertyRespVo queryAppPropertyRespById(Long id);

    /**
     * 修改装修页面属性。
     */
    Boolean updateDiyPageProperty(DiyPageBo bo);

    /**
     * 根据编号集合查询装修页面。
     */
    List<DiyPageVo> queryListByIds(Collection<Long> ids);

    /**
     * 根据编号集合查询装修页面响应列表。
     */
    List<DiyPageRespVo> queryRespListByIds(Collection<Long> ids);

    /**
     * 根据模板编号查询装修页面。
     */
    List<DiyPageVo> queryListByTemplateId(Long templateId);

    /**
     * 新增或修改装修页面。
     */
    Boolean saveOrUpdate(DiyPageBo bo);

    /**
     * 校验并批量删除装修页面。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

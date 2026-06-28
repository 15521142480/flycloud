package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.ArticleCategoryBo;
import com.fly.mall.api.domain.promotion.vo.ArticleCategoryVo;

import java.util.Collection;
import java.util.List;

/**
 * 文章分类 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IArticleCategoryService {

    /**
     * 查询文章分类详情。
     */
    ArticleCategoryVo queryById(Long id);

    /**
     * 分页查询文章分类。
     */
    PageVo<ArticleCategoryVo> queryPageList(ArticleCategoryBo bo, PageBo pageBo);

    /**
     * 查询文章分类列表。
     */
    List<ArticleCategoryVo> queryList(ArticleCategoryBo bo);

    /**
     * 新增或修改文章分类。
     */
    Boolean saveOrUpdate(ArticleCategoryBo bo);

    /**
     * 校验并批量删除文章分类。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

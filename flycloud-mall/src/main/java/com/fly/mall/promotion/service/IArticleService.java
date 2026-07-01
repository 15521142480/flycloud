package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.ArticleBo;
import com.fly.mall.api.promotion.domain.vo.ArticleVo;

import java.util.Collection;
import java.util.List;

/**
 * 文章 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface IArticleService {

    /**
     * 查询文章详情。
     */
    ArticleVo queryById(Long id);

    /**
     * 分页查询文章。
     */
    PageVo<ArticleVo> queryPageList(ArticleBo bo, PageBo pageBo);

    /**
     * 查询文章列表。
     */
    List<ArticleVo> queryList(ArticleBo bo);

    /**
     * 新增或修改文章。
     */
    Boolean saveOrUpdate(ArticleBo bo);

    /**
     * 校验并批量删除文章。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}

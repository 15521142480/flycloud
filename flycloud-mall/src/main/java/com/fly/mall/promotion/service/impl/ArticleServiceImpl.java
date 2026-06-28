package com.fly.mall.promotion.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.domain.promotion.Article;
import com.fly.mall.api.domain.promotion.bo.ArticleBo;
import com.fly.mall.api.domain.promotion.vo.ArticleVo;
import com.fly.mall.promotion.mapper.ArticleMapper;
import com.fly.mall.promotion.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 文章 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, Article> implements IArticleService {

    private final ArticleMapper baseMapper;

    /**
     * 查询文章详情。
     */
    @Override
    public ArticleVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询文章。
     */
    @Override
    public PageVo<ArticleVo> queryPageList(ArticleBo bo, PageBo pageBo) {
        LambdaQueryWrapper<Article> lqw = buildQueryWrapper(bo);
        Page<ArticleVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询文章列表。
     */
    @Override
    public List<ArticleVo> queryList(ArticleBo bo) {
        LambdaQueryWrapper<Article> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改文章。
     */
    @Override
    public Boolean saveOrUpdate(ArticleBo bo) {
        Article entity = BeanUtil.toBean(bo, Article.class);
        boolean isUpdate = entity.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(false);
        }
        if (isUpdate) {
            return baseMapper.updateById(entity) > 0;
        }
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        return baseMapper.insert(entity) > 0;
    }

    /**
     * 校验并批量删除文章。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            Article entity = new Article();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建文章查询条件。
     */
    private LambdaQueryWrapper<Article> buildQueryWrapper(ArticleBo bo) {
        LambdaQueryWrapper<Article> lqw = Wrappers.lambdaQuery();
        lqw.eq(Article::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, Article::getId, bo.getId());
        lqw.eq(bo.getCategoryId() != null, Article::getCategoryId, bo.getCategoryId());
        lqw.eq(bo.getSpuId() != null, Article::getSpuId, bo.getSpuId());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), Article::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getAuthor()), Article::getAuthor, bo.getAuthor());
        lqw.like(StringUtils.isNotBlank(bo.getPicUrl()), Article::getPicUrl, bo.getPicUrl());
        lqw.like(StringUtils.isNotBlank(bo.getIntroduction()), Article::getIntroduction, bo.getIntroduction());
        lqw.eq(bo.getBrowseCount() != null, Article::getBrowseCount, bo.getBrowseCount());
        lqw.eq(bo.getSort() != null, Article::getSort, bo.getSort());
        lqw.eq(bo.getStatus() != null, Article::getStatus, bo.getStatus());
        lqw.eq(bo.getRecommendHot() != null, Article::getRecommendHot, bo.getRecommendHot());
        lqw.eq(bo.getRecommendBanner() != null, Article::getRecommendBanner, bo.getRecommendBanner());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), Article::getContent, bo.getContent());
        return lqw;
    }

}

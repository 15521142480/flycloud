package com.fly.mall.promotion.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.mall.api.promotion.domain.Article;
import com.fly.mall.api.promotion.domain.vo.ArticleVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

/**
 * 文章 Mapper 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ArticleMapper extends BaseMapperPlus<ArticleMapper, Article, ArticleVo> {

    /**
     * 增加文章浏览次数。
     */
    default void updateBrowseCount(Long id) {
        update(null, new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, id)
                .setSql("browse_count = browse_count + 1"));
    }

}

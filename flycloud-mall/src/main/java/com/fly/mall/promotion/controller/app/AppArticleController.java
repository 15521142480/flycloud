package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.ArticleBo;
import com.fly.mall.api.promotion.domain.vo.ArticleVo;
import com.fly.mall.promotion.service.IArticleService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 文章 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/article")
public class AppArticleController {

    private final IArticleService articleService;

    /**
     * 查询移动端文章分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<ArticleVo>> list(ArticleBo bo, PageBo page) {
        return R.ok(articleService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<ArticleVo>> page(ArticleBo bo, PageBo page) {
        return R.ok(articleService.queryPageList(bo, page));
    }

    /**
     * 获取移动端文章详情。
     */
    @GetMapping("/get/{id}")
    public R<ArticleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(articleService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ArticleVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(articleService.queryById(id));
    }

}

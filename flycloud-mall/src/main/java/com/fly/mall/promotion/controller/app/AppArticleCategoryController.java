package com.fly.mall.promotion.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.ArticleCategoryBo;
import com.fly.mall.api.promotion.domain.vo.ArticleCategoryVo;
import com.fly.mall.promotion.service.IArticleCategoryService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 文章分类 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/promotion/article-category")
public class AppArticleCategoryController {

    private final IArticleCategoryService articleCategoryService;

    /**
     * 查询移动端文章分类分页列表。
     */
    @RequestMapping("/list")
    public R<PageVo<ArticleCategoryVo>> list(ArticleCategoryBo bo, PageBo page) {
        return R.ok(articleCategoryService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<ArticleCategoryVo>> page(ArticleCategoryBo bo, PageBo page) {
        return R.ok(articleCategoryService.queryPageList(bo, page));
    }

    /**
     * 获取移动端文章分类详情。
     */
    @GetMapping("/get/{id}")
    public R<ArticleCategoryVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(articleCategoryService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<ArticleCategoryVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(articleCategoryService.queryById(id));
    }

}

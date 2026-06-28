package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.promotion.bo.ArticleBo;
import com.fly.mall.api.domain.promotion.vo.ArticleVo;
import com.fly.mall.promotion.service.IArticleService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 文章 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/article")
public class ArticleController extends BaseController {

    private final IArticleService articleService;

    /**
     * 查询文章分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:article:list')")
    @GetMapping("/list")
    public R<PageVo<ArticleVo>> list(ArticleBo bo, PageBo page) {
        return R.ok(articleService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:article:list')")
    @GetMapping("/page")
    public R<PageVo<ArticleVo>> page(ArticleBo bo, PageBo page) {
        return R.ok(articleService.queryPageList(bo, page));
    }

    /**
     * 查询所有文章。
     */
    @GetMapping("/getList")
    public R<List<ArticleVo>> allList(ArticleBo bo) {
        return R.ok(articleService.queryList(bo));
    }

    /**
     * 获取文章详情。
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

    /**
     * 新增或修改文章。
     */
    @Log(title = "文章", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:article:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody ArticleBo bo) {
        return R.ok(articleService.saveOrUpdate(bo));
    }

    /**
     * 删除文章。
     */
    @Log(title = "文章", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:article:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(articleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

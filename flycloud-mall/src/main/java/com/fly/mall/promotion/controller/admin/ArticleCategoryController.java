package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.ArticleCategoryBo;
import com.fly.mall.api.promotion.domain.vo.ArticleCategoryVo;
import com.fly.mall.promotion.service.IArticleCategoryService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 文章分类 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/article-category")
public class ArticleCategoryController extends BaseController {

    private final IArticleCategoryService articleCategoryService;

    /**
     * 查询文章分类分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:article-category:list')")
    @GetMapping("/list")
    public R<PageVo<ArticleCategoryVo>> list(ArticleCategoryBo bo, PageBo page) {
        return R.ok(articleCategoryService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:article-category:list')")
    @GetMapping("/page")
    public R<PageVo<ArticleCategoryVo>> page(ArticleCategoryBo bo, PageBo page) {
        return R.ok(articleCategoryService.queryPageList(bo, page));
    }

    /**
     * 查询所有文章分类。
     */
    @GetMapping({"/getList", "/list-all-simple"})
    public R<List<ArticleCategoryVo>> allList(ArticleCategoryBo bo) {
        return R.ok(articleCategoryService.queryList(bo));
    }

    /**
     * 获取文章分类详情。
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

    /**
     * 新增或修改文章分类。
     */
    @Log(title = "文章分类", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:article-category:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody ArticleCategoryBo bo) {
        return R.ok(articleCategoryService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody ArticleCategoryBo bo) {
        return R.ok(articleCategoryService.saveOrUpdate(bo));
    }

    /**
     * 删除文章分类。
     */
    @Log(title = "文章分类", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:article-category:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(articleCategoryService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(articleCategoryService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

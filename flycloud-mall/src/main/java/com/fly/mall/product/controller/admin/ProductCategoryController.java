package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.common.utils.ExcelUtil;
import com.fly.mall.api.product.domain.bo.ProductCategoryBo;
import com.fly.mall.api.product.domain.vo.ProductCategoryVo;
import com.fly.mall.product.service.IProductCategoryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 商品分类控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/product/category")
public class ProductCategoryController extends BaseController {

    private final IProductCategoryService productCategoryService;

    /**
     * 查询商品分类分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:category:list')")
    @GetMapping("/list")
    public R<PageVo<ProductCategoryVo>> list(ProductCategoryBo bo, PageBo page) {
        return R.ok(productCategoryService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:product:category:list')")
    @GetMapping("/page")
    public R<PageVo<ProductCategoryVo>> page(ProductCategoryBo bo, PageBo page) {
        return R.ok(productCategoryService.queryPageList(bo, page));
    }

    /**
     * 查询所有商品分类。
     */
    @GetMapping("/getList")
    public R<List<ProductCategoryVo>> allList(ProductCategoryBo bo) {
        return R.ok(productCategoryService.queryList(bo));
    }

    /**
     * 获取商品分类详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductCategoryVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productCategoryService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductCategoryVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productCategoryService.queryById(id));
    }

    /**
     * 新增或修改商品分类。
     */
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:category:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody ProductCategoryBo bo) {
        return R.ok(productCategoryService.saveOrUpdate(bo));
    }

    /**
     * 删除商品分类。
     */
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:category:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(productCategoryService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 导出商品分类。
     */
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PreAuthorize("@pms.hasPermission('mall:product:category:download')")
    @PostMapping("/export")
    public void export(ProductCategoryBo bo, HttpServletResponse response) {
        List<ProductCategoryVo> list = productCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品分类", ProductCategoryVo.class, response);
    }

}

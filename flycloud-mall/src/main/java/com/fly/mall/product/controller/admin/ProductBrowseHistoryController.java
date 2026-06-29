package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.product.domain.bo.ProductBrowseHistoryBo;
import com.fly.mall.api.product.domain.vo.ProductBrowseHistoryVo;
import com.fly.mall.product.service.IProductBrowseHistoryService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 商品浏览记录 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/product/product-browse-history")
public class ProductBrowseHistoryController extends BaseController {

    private final IProductBrowseHistoryService productBrowseHistoryService;

    /**
     * 查询商品浏览记录分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:product-browse-history:list')")
    @GetMapping("/list")
    public R<PageVo<ProductBrowseHistoryVo>> list(ProductBrowseHistoryBo bo, PageBo page) {
        return R.ok(productBrowseHistoryService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:product:product-browse-history:list')")
    @GetMapping("/page")
    public R<PageVo<ProductBrowseHistoryVo>> page(ProductBrowseHistoryBo bo, PageBo page) {
        return R.ok(productBrowseHistoryService.queryPageList(bo, page));
    }

    /**
     * 查询所有商品浏览记录。
     */
    @GetMapping("/getList")
    public R<List<ProductBrowseHistoryVo>> allList(ProductBrowseHistoryBo bo) {
        return R.ok(productBrowseHistoryService.queryList(bo));
    }

    /**
     * 获取商品浏览记录详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductBrowseHistoryVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productBrowseHistoryService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductBrowseHistoryVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productBrowseHistoryService.queryById(id));
    }

    /**
     * 新增或修改商品浏览记录。
     */
    @Log(title = "商品浏览记录", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:product-browse-history:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody ProductBrowseHistoryBo bo) {
        return R.ok(productBrowseHistoryService.saveOrUpdate(bo));
    }

    /**
     * 删除商品浏览记录。
     */
    @Log(title = "商品浏览记录", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:product-browse-history:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(productBrowseHistoryService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

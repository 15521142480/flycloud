package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.product.domain.bo.ProductPropertyBo;
import com.fly.mall.api.product.domain.vo.ProductPropertyVo;
import com.fly.mall.product.service.IProductPropertyService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 商品属性 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/product/product-property")
public class ProductPropertyController extends BaseController {

    private final IProductPropertyService productPropertyService;

    /**
     * 查询商品属性分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:product-property:list')")
    @GetMapping("/list")
    public R<PageVo<ProductPropertyVo>> list(ProductPropertyBo bo, PageBo page) {
        return R.ok(productPropertyService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:product:product-property:list')")
    @GetMapping("/page")
    public R<PageVo<ProductPropertyVo>> page(ProductPropertyBo bo, PageBo page) {
        return R.ok(productPropertyService.queryPageList(bo, page));
    }

    /**
     * 查询所有商品属性。
     */
    @GetMapping("/getList")
    public R<List<ProductPropertyVo>> allList(ProductPropertyBo bo) {
        return R.ok(productPropertyService.queryList(bo));
    }

    /**
     * 获取商品属性详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductPropertyVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productPropertyService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductPropertyVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productPropertyService.queryById(id));
    }

    /**
     * 新增或修改商品属性。
     */
    @Log(title = "商品属性", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:product-property:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody ProductPropertyBo bo) {
        return R.ok(productPropertyService.saveOrUpdate(bo));
    }

    /**
     * 删除商品属性。
     */
    @Log(title = "商品属性", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:product-property:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(productPropertyService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

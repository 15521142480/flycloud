package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.product.bo.ProductPropertyValueBo;
import com.fly.mall.api.domain.product.vo.ProductPropertyValueVo;
import com.fly.mall.product.service.IProductPropertyValueService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 商品属性值 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/product/product-property-value")
public class ProductPropertyValueController extends BaseController {

    private final IProductPropertyValueService productPropertyValueService;

    /**
     * 查询商品属性值分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:product-property-value:list')")
    @GetMapping("/list")
    public R<PageVo<ProductPropertyValueVo>> list(ProductPropertyValueBo bo, PageBo page) {
        return R.ok(productPropertyValueService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:product:product-property-value:list')")
    @GetMapping("/page")
    public R<PageVo<ProductPropertyValueVo>> page(ProductPropertyValueBo bo, PageBo page) {
        return R.ok(productPropertyValueService.queryPageList(bo, page));
    }

    /**
     * 查询所有商品属性值。
     */
    @GetMapping("/getList")
    public R<List<ProductPropertyValueVo>> allList(ProductPropertyValueBo bo) {
        return R.ok(productPropertyValueService.queryList(bo));
    }

    /**
     * 获取商品属性值详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductPropertyValueVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productPropertyValueService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductPropertyValueVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productPropertyValueService.queryById(id));
    }

    /**
     * 新增或修改商品属性值。
     */
    @Log(title = "商品属性值", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:product-property-value:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody ProductPropertyValueBo bo) {
        return R.ok(productPropertyValueService.saveOrUpdate(bo));
    }

    /**
     * 删除商品属性值。
     */
    @Log(title = "商品属性值", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:product-property-value:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(productPropertyValueService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

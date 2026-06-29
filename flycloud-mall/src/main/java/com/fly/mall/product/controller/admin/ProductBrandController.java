package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.common.utils.ExcelUtil;
import com.fly.mall.api.product.domain.bo.ProductBrandBo;
import com.fly.mall.api.product.domain.vo.ProductBrandVo;
import com.fly.mall.product.service.IProductBrandService;
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
 * 管理后台 - 商品品牌控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/product/brand")
public class ProductBrandController extends BaseController {

    private final IProductBrandService productBrandService;

    /**
     * 查询商品品牌分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:brand:list')")
    @GetMapping("/list")
    public R<PageVo<ProductBrandVo>> list(ProductBrandBo bo, PageBo page) {
        return R.ok(productBrandService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:product:brand:list')")
    @GetMapping("/page")
    public R<PageVo<ProductBrandVo>> page(ProductBrandBo bo, PageBo page) {
        return R.ok(productBrandService.queryPageList(bo, page));
    }

    /**
     * 查询所有商品品牌。
     */
    @GetMapping("/getList")
    public R<List<ProductBrandVo>> allList(ProductBrandBo bo) {
        return R.ok(productBrandService.queryList(bo));
    }

    /**
     * 获取商品品牌详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductBrandVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productBrandService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductBrandVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productBrandService.queryById(id));
    }

    /**
     * 新增或修改商品品牌。
     */
    @Log(title = "商品品牌", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:brand:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody ProductBrandBo bo) {
        return R.ok(productBrandService.saveOrUpdate(bo));
    }

    /**
     * 删除商品品牌。
     */
    @Log(title = "商品品牌", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:brand:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(productBrandService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 导出商品品牌。
     */
    @Log(title = "商品品牌", businessType = BusinessType.EXPORT)
    @PreAuthorize("@pms.hasPermission('mall:product:brand:download')")
    @PostMapping("/export")
    public void export(ProductBrandBo bo, HttpServletResponse response) {
        List<ProductBrandVo> list = productBrandService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品品牌", ProductBrandVo.class, response);
    }

}

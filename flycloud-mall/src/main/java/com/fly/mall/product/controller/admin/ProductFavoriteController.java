package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.product.domain.bo.ProductFavoriteBo;
import com.fly.mall.api.product.domain.vo.ProductFavoriteVo;
import com.fly.mall.product.service.IProductFavoriteService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 商品收藏 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/admin/product/product-favorite", "/admin/product/favorite"})
public class ProductFavoriteController extends BaseController {

    private final IProductFavoriteService productFavoriteService;

    /**
     * 查询商品收藏分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:product-favorite:list')")
    @GetMapping("/list")
    public R<PageVo<ProductFavoriteVo>> list(ProductFavoriteBo bo, PageBo page) {
        return R.ok(productFavoriteService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:product:product-favorite:list')")
    @GetMapping("/page")
    public R<PageVo<ProductFavoriteVo>> page(ProductFavoriteBo bo, PageBo page) {
        return R.ok(productFavoriteService.queryPageList(bo, page));
    }

    /**
     * 查询所有商品收藏。
     */
    @GetMapping("/getList")
    public R<List<ProductFavoriteVo>> allList(ProductFavoriteBo bo) {
        return R.ok(productFavoriteService.queryList(bo));
    }

    /**
     * 获取商品收藏详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductFavoriteVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productFavoriteService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductFavoriteVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productFavoriteService.queryById(id));
    }

    /**
     * 新增或修改商品收藏。
     */
    @Log(title = "商品收藏", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:product-favorite:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody ProductFavoriteBo bo) {
        return R.result(productFavoriteService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody ProductFavoriteBo bo) {
        return R.result(productFavoriteService.saveOrUpdate(bo));
    }

    /**
     * 删除商品收藏。
     */
    @Log(title = "商品收藏", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:product-favorite:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(productFavoriteService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(productFavoriteService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

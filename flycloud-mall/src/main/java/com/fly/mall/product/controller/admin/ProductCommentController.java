package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.product.domain.bo.ProductCommentBo;
import com.fly.mall.api.product.domain.vo.ProductCommentVo;
import com.fly.mall.product.service.IProductCommentService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 商品评价 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/product/comment")
public class ProductCommentController extends BaseController {

    private final IProductCommentService productCommentService;

    /**
     * 查询商品评价分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:comment:list')")
    @GetMapping("/list")
    public R<PageVo<ProductCommentVo>> list(ProductCommentBo bo, PageBo page) {
        return R.ok(productCommentService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:product:comment:list')")
    @GetMapping("/page")
    public R<PageVo<ProductCommentVo>> page(ProductCommentBo bo, PageBo page) {
        return R.ok(productCommentService.queryPageList(bo, page));
    }

    /**
     * 查询所有商品评价。
     */
    @GetMapping("/getList")
    public R<List<ProductCommentVo>> allList(ProductCommentBo bo) {
        return R.ok(productCommentService.queryList(bo));
    }

    /**
     * 获取商品评价详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductCommentVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productCommentService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductCommentVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productCommentService.queryById(id));
    }

    /**
     * 新增或修改商品评价。
     */
    @Log(title = "商品评价", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:comment:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody ProductCommentBo bo) {
        return R.ok(productCommentService.saveOrUpdate(bo));
    }

    /**
     * 删除商品评价。
     */
    @Log(title = "商品评价", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:comment:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(productCommentService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

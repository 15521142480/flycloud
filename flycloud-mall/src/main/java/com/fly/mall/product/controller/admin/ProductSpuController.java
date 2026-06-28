package com.fly.mall.product.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.common.utils.ExcelUtil;
import com.fly.mall.api.domain.product.bo.ProductSpuBo;
import com.fly.mall.api.domain.product.vo.ProductSpuVo;
import com.fly.mall.product.service.IProductSpuService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import java.util.Map;

/**
 * 管理后台 - 商品 SPU 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/product/spu")
public class ProductSpuController extends BaseController {

    private final IProductSpuService productSpuService;

    /**
     * 查询商品 SPU 分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping("/list")
    public R<PageVo<ProductSpuVo>> list(ProductSpuBo bo, PageBo page) {
        return R.ok(productSpuService.queryPageList(bo, page));
    }

    /**
     * 获得商品 SPU 分页。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping("/page")
    public R<PageVo<ProductSpuVo>> page(ProductSpuBo bo, PageBo page) {
        return R.ok(productSpuService.queryPageList(bo, page));
    }

    /**
     * 获得商品 SPU 详情列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping("/detail-list")
    public R<List<ProductSpuVo>> getSpuList(@RequestParam("spuIds") Collection<Long> spuIds) {
        return R.ok(productSpuService.queryListByIds(spuIds));
    }

    /**
     * 获得商品 SPU 精简列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping("/list-all-simple")
    public R<List<ProductSpuVo>> getSpuSimpleList() {
        ProductSpuBo bo = new ProductSpuBo();
        bo.setStatus(com.fly.common.enums.StatusEnum.ENABLE.getStatus());
        return R.ok(productSpuService.queryList(bo));
    }

    /**
     * 获得商品 SPU 状态数量。
     */
    @PreAuthorize("@pms.hasPermission('mall:product:spu:list')")
    @GetMapping("/get-count")
    public R<Map<Integer, Long>> getSpuCount(ProductSpuBo bo) {
        return R.ok(productSpuService.queryStatusCount(bo));
    }

    /**
     * 获取商品 SPU 详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductSpuVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productSpuService.queryDetailById(id));
    }

    /**
     * 获得商品 SPU 明细。
     */
    @GetMapping("/get-detail")
    public R<ProductSpuVo> getSpuDetail(@RequestParam("id") Long id) {
        return R.ok(productSpuService.queryDetailById(id));
    }

    /**
     * 新增或修改商品 SPU。
     */
    @Log(title = "商品SPU", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:product:spu:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody ProductSpuBo bo) {
        return R.ok(productSpuService.saveOrUpdate(bo));
    }

    /**
     * 更新商品 SPU 状态。
     */
    @Log(title = "商品SPU", businessType = BusinessType.UPDATE)
    @PreAuthorize("@pms.hasPermission('mall:product:spu:saveOrUpdate')")
    @PutMapping("/update-status")
    public R<Void> updateStatus(@RequestBody ProductSpuBo bo) {
        return R.ok(productSpuService.updateStatus(bo.getId(), bo.getStatus()));
    }

    /**
     * 删除商品 SPU。
     */
    @Log(title = "商品SPU", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:product:spu:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(productSpuService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 导出商品 SPU。
     */
    @Log(title = "商品SPU", businessType = BusinessType.EXPORT)
    @PreAuthorize("@pms.hasPermission('mall:product:spu:download')")
    @PostMapping("/export")
    public void export(ProductSpuBo bo, HttpServletResponse response) {
        List<ProductSpuVo> list = productSpuService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品SPU", ProductSpuVo.class, response);
    }

}

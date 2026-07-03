package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.PointProductBo;
import com.fly.mall.api.promotion.domain.vo.PointProductVo;
import com.fly.mall.promotion.service.IPointProductService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 积分商城商品 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/point-product")
public class PointProductController extends BaseController {

    private final IPointProductService pointProductService;

    /**
     * 查询积分商城商品分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:point-product:list')")
    @GetMapping("/list")
    public R<PageVo<PointProductVo>> list(PointProductBo bo, PageBo page) {
        return R.ok(pointProductService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:point-product:list')")
    @GetMapping("/page")
    public R<PageVo<PointProductVo>> page(PointProductBo bo, PageBo page) {
        return R.ok(pointProductService.queryPageList(bo, page));
    }

    /**
     * 查询所有积分商城商品。
     */
    @GetMapping("/getList")
    public R<List<PointProductVo>> allList(PointProductBo bo) {
        return R.ok(pointProductService.queryList(bo));
    }

    /**
     * 获取积分商城商品详情。
     */
    @GetMapping("/get/{id}")
    public R<PointProductVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(pointProductService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<PointProductVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(pointProductService.queryById(id));
    }

    /**
     * 新增或修改积分商城商品。
     */
    @Log(title = "积分商城商品", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:point-product:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody PointProductBo bo) {
        return R.result(pointProductService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody PointProductBo bo) {
        return R.result(pointProductService.saveOrUpdate(bo));
    }

    /**
     * 删除积分商城商品。
     */
    @Log(title = "积分商城商品", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:point-product:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(pointProductService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(pointProductService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

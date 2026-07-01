package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.CombinationProductBo;
import com.fly.mall.api.promotion.domain.vo.CombinationProductVo;
import com.fly.mall.promotion.service.ICombinationProductService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 拼团商品 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/combination-product")
public class CombinationProductController extends BaseController {

    private final ICombinationProductService combinationProductService;

    /**
     * 查询拼团商品分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-product:list')")
    @GetMapping("/list")
    public R<PageVo<CombinationProductVo>> list(CombinationProductBo bo, PageBo page) {
        return R.ok(combinationProductService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-product:list')")
    @GetMapping("/page")
    public R<PageVo<CombinationProductVo>> page(CombinationProductBo bo, PageBo page) {
        return R.ok(combinationProductService.queryPageList(bo, page));
    }

    /**
     * 查询所有拼团商品。
     */
    @GetMapping("/getList")
    public R<List<CombinationProductVo>> allList(CombinationProductBo bo) {
        return R.ok(combinationProductService.queryList(bo));
    }

    /**
     * 获取拼团商品详情。
     */
    @GetMapping("/get/{id}")
    public R<CombinationProductVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(combinationProductService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<CombinationProductVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(combinationProductService.queryById(id));
    }

    /**
     * 新增或修改拼团商品。
     */
    @Log(title = "拼团商品", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-product:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody CombinationProductBo bo) {
        return R.ok(combinationProductService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody CombinationProductBo bo) {
        return R.ok(combinationProductService.saveOrUpdate(bo));
    }

    /**
     * 删除拼团商品。
     */
    @Log(title = "拼团商品", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-product:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(combinationProductService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(combinationProductService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

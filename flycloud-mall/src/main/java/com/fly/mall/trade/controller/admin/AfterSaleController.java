package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.AfterSaleBo;
import com.fly.mall.api.trade.domain.vo.AfterSaleVo;
import com.fly.mall.trade.service.IAfterSaleService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 售后单 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/after-sale")
public class AfterSaleController extends BaseController {

    private final IAfterSaleService afterSaleService;

    /**
     * 查询售后单分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:list')")
    @GetMapping("/list")
    public R<PageVo<AfterSaleVo>> list(AfterSaleBo bo, PageBo page) {
        return R.ok(afterSaleService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:list')")
    @GetMapping("/page")
    public R<PageVo<AfterSaleVo>> page(AfterSaleBo bo, PageBo page) {
        return R.ok(afterSaleService.queryPageList(bo, page));
    }

    /**
     * 查询所有售后单。
     */
    @GetMapping("/getList")
    public R<List<AfterSaleVo>> allList(AfterSaleBo bo) {
        return R.ok(afterSaleService.queryList(bo));
    }

    /**
     * 获取售后单详情。
     */
    @GetMapping("/get/{id}")
    public R<AfterSaleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(afterSaleService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<AfterSaleVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(afterSaleService.queryById(id));
    }

    /**
     * 新增或修改售后单。
     */
    @Log(title = "售后单", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody AfterSaleBo bo) {
        return R.ok(afterSaleService.saveOrUpdate(bo));
    }

    /**
     * 删除售后单。
     */
    @Log(title = "售后单", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(afterSaleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

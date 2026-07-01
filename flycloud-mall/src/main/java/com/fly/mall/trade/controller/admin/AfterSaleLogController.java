package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.AfterSaleLogBo;
import com.fly.mall.api.trade.domain.vo.AfterSaleLogVo;
import com.fly.mall.trade.service.IAfterSaleLogService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 售后日志 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/after-sale-log")
public class AfterSaleLogController extends BaseController {

    private final IAfterSaleLogService afterSaleLogService;

    /**
     * 查询售后日志分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:list')")
    @GetMapping("/list")
    public R<PageVo<AfterSaleLogVo>> list(AfterSaleLogBo bo, PageBo page) {
        return R.ok(afterSaleLogService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:list')")
    @GetMapping("/page")
    public R<PageVo<AfterSaleLogVo>> page(AfterSaleLogBo bo, PageBo page) {
        return R.ok(afterSaleLogService.queryPageList(bo, page));
    }

    /**
     * 查询所有售后日志。
     */
    @GetMapping("/getList")
    public R<List<AfterSaleLogVo>> allList(AfterSaleLogBo bo) {
        return R.ok(afterSaleLogService.queryList(bo));
    }

    /**
     * 获取售后日志详情。
     */
    @GetMapping("/get/{id}")
    public R<AfterSaleLogVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(afterSaleLogService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<AfterSaleLogVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(afterSaleLogService.queryById(id));
    }

    /**
     * 新增或修改售后日志。
     */
    @Log(title = "售后日志", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody AfterSaleLogBo bo) {
        return R.ok(afterSaleLogService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody AfterSaleLogBo bo) {
        return R.ok(afterSaleLogService.saveOrUpdate(bo));
    }

    /**
     * 删除售后日志。
     */
    @Log(title = "售后日志", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:after-sale:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(afterSaleLogService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(afterSaleLogService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

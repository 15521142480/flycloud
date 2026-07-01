package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.TradeOrderLogBo;
import com.fly.mall.api.trade.domain.vo.TradeOrderLogVo;
import com.fly.mall.trade.service.ITradeOrderLogService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 交易订单日志 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/trade-order-log")
public class TradeOrderLogController extends BaseController {

    private final ITradeOrderLogService tradeOrderLogService;

    /**
     * 查询交易订单日志分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order-log:list')")
    @GetMapping("/list")
    public R<PageVo<TradeOrderLogVo>> list(TradeOrderLogBo bo, PageBo page) {
        return R.ok(tradeOrderLogService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order-log:list')")
    @GetMapping("/page")
    public R<PageVo<TradeOrderLogVo>> page(TradeOrderLogBo bo, PageBo page) {
        return R.ok(tradeOrderLogService.queryPageList(bo, page));
    }

    /**
     * 查询所有交易订单日志。
     */
    @GetMapping("/getList")
    public R<List<TradeOrderLogVo>> allList(TradeOrderLogBo bo) {
        return R.ok(tradeOrderLogService.queryList(bo));
    }

    /**
     * 获取交易订单日志详情。
     */
    @GetMapping("/get/{id}")
    public R<TradeOrderLogVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tradeOrderLogService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<TradeOrderLogVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeOrderLogService.queryById(id));
    }

    /**
     * 新增或修改交易订单日志。
     */
    @Log(title = "交易订单日志", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order-log:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody TradeOrderLogBo bo) {
        return R.ok(tradeOrderLogService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody TradeOrderLogBo bo) {
        return R.ok(tradeOrderLogService.saveOrUpdate(bo));
    }

    /**
     * 删除交易订单日志。
     */
    @Log(title = "交易订单日志", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order-log:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(tradeOrderLogService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(tradeOrderLogService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

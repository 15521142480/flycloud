package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.TradeOrderBo;
import com.fly.mall.api.trade.domain.vo.TradeOrderVo;
import com.fly.mall.trade.service.ITradeOrderService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 交易订单 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/trade-order")
public class TradeOrderController extends BaseController {

    private final ITradeOrderService tradeOrderService;

    /**
     * 查询交易订单分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order:list')")
    @GetMapping("/list")
    public R<PageVo<TradeOrderVo>> list(TradeOrderBo bo, PageBo page) {
        return R.ok(tradeOrderService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order:list')")
    @GetMapping("/page")
    public R<PageVo<TradeOrderVo>> page(TradeOrderBo bo, PageBo page) {
        return R.ok(tradeOrderService.queryPageList(bo, page));
    }

    /**
     * 查询所有交易订单。
     */
    @GetMapping("/getList")
    public R<List<TradeOrderVo>> allList(TradeOrderBo bo) {
        return R.ok(tradeOrderService.queryList(bo));
    }

    /**
     * 获取交易订单详情。
     */
    @GetMapping("/get/{id}")
    public R<TradeOrderVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tradeOrderService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<TradeOrderVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.queryById(id));
    }

    /**
     * 新增或修改交易订单。
     */
    @Log(title = "交易订单", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody TradeOrderBo bo) {
        return R.ok(tradeOrderService.saveOrUpdate(bo));
    }

    /**
     * 删除交易订单。
     */
    @Log(title = "交易订单", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(tradeOrderService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

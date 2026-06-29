package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.TradeOrderItemBo;
import com.fly.mall.api.trade.domain.vo.TradeOrderItemVo;
import com.fly.mall.trade.service.ITradeOrderItemService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 交易订单项 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/trade-order-item")
public class TradeOrderItemController extends BaseController {

    private final ITradeOrderItemService tradeOrderItemService;

    /**
     * 查询交易订单项分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order-item:list')")
    @GetMapping("/list")
    public R<PageVo<TradeOrderItemVo>> list(TradeOrderItemBo bo, PageBo page) {
        return R.ok(tradeOrderItemService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order-item:list')")
    @GetMapping("/page")
    public R<PageVo<TradeOrderItemVo>> page(TradeOrderItemBo bo, PageBo page) {
        return R.ok(tradeOrderItemService.queryPageList(bo, page));
    }

    /**
     * 查询所有交易订单项。
     */
    @GetMapping("/getList")
    public R<List<TradeOrderItemVo>> allList(TradeOrderItemBo bo) {
        return R.ok(tradeOrderItemService.queryList(bo));
    }

    /**
     * 获取交易订单项详情。
     */
    @GetMapping("/get/{id}")
    public R<TradeOrderItemVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tradeOrderItemService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<TradeOrderItemVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeOrderItemService.queryById(id));
    }

    /**
     * 新增或修改交易订单项。
     */
    @Log(title = "交易订单项", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order-item:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody TradeOrderItemBo bo) {
        return R.ok(tradeOrderItemService.saveOrUpdate(bo));
    }

    /**
     * 删除交易订单项。
     */
    @Log(title = "交易订单项", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order-item:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(tradeOrderItemService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

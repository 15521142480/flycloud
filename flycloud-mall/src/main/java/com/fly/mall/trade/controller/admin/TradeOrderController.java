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
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 管理后台 - 交易订单 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/admin/trade/trade-order", "/admin/trade/order"})
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
    @GetMapping({"/get-detail", "/get"})
    public R<TradeOrderVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.queryById(id));
    }

    /**
     * 新增或修改交易订单。
     */
    @Log(title = "交易订单", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:trade-order:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody TradeOrderBo bo) {
        return R.ok(tradeOrderService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody TradeOrderBo bo) {
        return R.ok(tradeOrderService.saveOrUpdate(bo));
    }

    /**
     * 获得交易订单统计。
     */
    @GetMapping("/summary")
    public R<Map<String, Long>> summary(TradeOrderBo bo) {
        return R.ok(Map.of("count", (long) tradeOrderService.queryList(bo).size()));
    }

    /**
     * 查询交易订单物流轨迹。
     */
    @GetMapping("/get-express-track-list")
    public R<List<Object>> getExpressTrackList(@RequestParam("id") Long id) {
        return R.ok(List.of());
    }

    /**
     * 订单发货。
     */
    @PutMapping("/delivery")
    public R<Void> delivery(@RequestBody TradeOrderBo bo) {
        bo.setStatus(20);
        bo.setDeliveryTime(LocalDateTime.now());
        return R.ok(tradeOrderService.saveOrUpdate(bo));
    }

    /**
     * 更新订单备注。
     */
    @PutMapping("/update-remark")
    public R<Void> updateRemark(@RequestBody TradeOrderBo bo) {
        return R.ok(tradeOrderService.saveOrUpdate(bo));
    }

    /**
     * 更新订单价格。
     */
    @PutMapping("/update-price")
    public R<Void> updatePrice(@RequestBody TradeOrderBo bo) {
        return R.ok(tradeOrderService.saveOrUpdate(bo));
    }

    /**
     * 更新订单收货地址。
     */
    @PutMapping("/update-address")
    public R<Void> updateAddress(@RequestBody TradeOrderBo bo) {
        return R.ok(tradeOrderService.saveOrUpdate(bo));
    }

    /**
     * 按订单编号核销订单。
     */
    @PutMapping("/pick-up-by-id")
    public R<Void> pickUpById(@RequestParam("id") Long id) {
        TradeOrderBo bo = new TradeOrderBo();
        bo.setId(id);
        bo.setStatus(30);
        bo.setReceiveTime(LocalDateTime.now());
        bo.setFinishTime(LocalDateTime.now());
        return R.ok(tradeOrderService.saveOrUpdate(bo));
    }

    /**
     * 按核销码核销订单。
     */
    @PutMapping("/pick-up-by-verify-code")
    public R<Void> pickUpByVerifyCode(@RequestParam("pickUpVerifyCode") String pickUpVerifyCode) {
        TradeOrderBo query = new TradeOrderBo();
        query.setPickUpVerifyCode(pickUpVerifyCode);
        List<TradeOrderVo> orders = tradeOrderService.queryList(query);
        if (orders.isEmpty()) {
            return R.ok();
        }
        return pickUpById(orders.get(0).getId());
    }

    /**
     * 根据核销码查询订单。
     */
    @GetMapping("/get-by-pick-up-verify-code")
    public R<TradeOrderVo> getByPickUpVerifyCode(@RequestParam("pickUpVerifyCode") String pickUpVerifyCode) {
        TradeOrderBo query = new TradeOrderBo();
        query.setPickUpVerifyCode(pickUpVerifyCode);
        return R.ok(tradeOrderService.queryList(query).stream().findFirst().orElse(null));
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

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

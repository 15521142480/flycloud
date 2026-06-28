package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.domain.trade.bo.TradeOrderBo;
import com.fly.mall.api.domain.trade.vo.TradeOrderVo;
import com.fly.mall.trade.service.ITradeOrderService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 移动端 - 交易订单 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/trade-order")
public class AppTradeOrderController {

    private final ITradeOrderService tradeOrderService;

    /**
     * 查询移动端交易订单分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<TradeOrderVo>> list(TradeOrderBo bo, PageBo page) {
        return R.ok(tradeOrderService.queryUserPageList(UserUtils.getCurUserId(), bo, page));
    }

    /**
     * 获得交易订单分页。
     */
    @GetMapping("/page")
    public R<PageVo<TradeOrderVo>> page(TradeOrderBo bo, PageBo page) {
        return R.ok(tradeOrderService.queryUserPageList(UserUtils.getCurUserId(), bo, page));
    }

    /**
     * 创建交易订单。
     */
    @PostMapping("/create")
    public R<TradeOrderVo> createOrder(@RequestBody TradeOrderBo bo) {
        return R.ok(tradeOrderService.createOrder(UserUtils.getCurUserId(), bo));
    }

    /**
     * 获取移动端交易订单详情。
     */
    @GetMapping("/get/{id}")
    public R<TradeOrderVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tradeOrderService.queryByUserAndId(UserUtils.getCurUserId(), id));
    }

    /**
     * 获得交易订单详情。
     */
    @GetMapping("/get-detail")
    public R<TradeOrderVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.queryByUserAndId(UserUtils.getCurUserId(), id));
    }

    /**
     * 获得交易订单数量。
     */
    @GetMapping("/get-count")
    public R<Map<String, Long>> getOrderCount() {
        return R.ok(tradeOrderService.getOrderCount(UserUtils.getCurUserId()));
    }

    /**
     * 确认交易订单收货。
     */
    @PutMapping("/receive")
    public R<Void> receiveOrder(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.receiveOrder(UserUtils.getCurUserId(), id));
    }

    /**
     * 取消交易订单。
     */
    @DeleteMapping("/cancel")
    public R<Void> cancelOrder(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.cancelOrder(UserUtils.getCurUserId(), id));
    }

    /**
     * 删除交易订单。
     */
    @DeleteMapping("/delete")
    public R<Void> deleteOrder(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.deleteOrder(UserUtils.getCurUserId(), id));
    }

}

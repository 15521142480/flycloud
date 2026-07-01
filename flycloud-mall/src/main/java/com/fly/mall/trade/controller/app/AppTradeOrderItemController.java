package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.TradeOrderItemBo;
import com.fly.mall.api.trade.domain.vo.TradeOrderItemVo;
import com.fly.mall.trade.service.ITradeOrderItemService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 交易订单项 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/app/trade/trade-order-item", "/app/trade/order/item"})
public class AppTradeOrderItemController {

    private final ITradeOrderItemService tradeOrderItemService;

    /**
     * 查询移动端交易订单项分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<TradeOrderItemVo>> list(TradeOrderItemBo bo, PageBo page) {
        return R.ok(tradeOrderItemService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<TradeOrderItemVo>> page(TradeOrderItemBo bo, PageBo page) {
        return R.ok(tradeOrderItemService.queryPageList(bo, page));
    }

    /**
     * 获取移动端交易订单项详情。
     */
    @GetMapping("/get/{id}")
    public R<TradeOrderItemVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tradeOrderItemService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<TradeOrderItemVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeOrderItemService.queryById(id));
    }

    /**
     * 创建订单项评价。
     */
    @PostMapping("/create-comment")
    public R<Long> createComment(@RequestBody com.fly.mall.api.trade.domain.bo.TradeOrderItemBo bo) {
        tradeOrderItemService.saveOrUpdate(bo);
        return R.ok(bo.getId());
    }

}

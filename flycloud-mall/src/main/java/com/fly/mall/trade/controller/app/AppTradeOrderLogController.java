package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.TradeOrderLogBo;
import com.fly.mall.api.trade.domain.vo.TradeOrderLogVo;
import com.fly.mall.trade.service.ITradeOrderLogService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 交易订单日志 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/trade-order-log")
public class AppTradeOrderLogController {

    private final ITradeOrderLogService tradeOrderLogService;

    /**
     * 查询移动端交易订单日志分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<TradeOrderLogVo>> list(TradeOrderLogBo bo, PageBo page) {
        return R.ok(tradeOrderLogService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<TradeOrderLogVo>> page(TradeOrderLogBo bo, PageBo page) {
        return R.ok(tradeOrderLogService.queryPageList(bo, page));
    }

    /**
     * 获取移动端交易订单日志详情。
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

}

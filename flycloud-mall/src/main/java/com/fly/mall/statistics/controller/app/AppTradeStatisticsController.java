package com.fly.mall.statistics.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.statistics.domain.bo.TradeStatisticsBo;
import com.fly.mall.api.statistics.domain.vo.TradeStatisticsVo;
import com.fly.mall.statistics.service.ITradeStatisticsService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 交易统计 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/statistics/trade-statistics")
public class AppTradeStatisticsController {

    private final ITradeStatisticsService tradeStatisticsService;

    /**
     * 查询移动端交易统计分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<TradeStatisticsVo>> list(TradeStatisticsBo bo, PageBo page) {
        return R.ok(tradeStatisticsService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<TradeStatisticsVo>> page(TradeStatisticsBo bo, PageBo page) {
        return R.ok(tradeStatisticsService.queryPageList(bo, page));
    }

    /**
     * 获取移动端交易统计详情。
     */
    @GetMapping("/get/{id}")
    public R<TradeStatisticsVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tradeStatisticsService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<TradeStatisticsVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeStatisticsService.queryById(id));
    }

}

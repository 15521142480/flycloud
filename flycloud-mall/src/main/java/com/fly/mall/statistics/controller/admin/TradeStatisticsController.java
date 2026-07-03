package com.fly.mall.statistics.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.common.utils.ExcelUtil;
import com.fly.mall.api.statistics.domain.bo.StatisticsTimeRangeBo;
import com.fly.mall.api.statistics.domain.bo.TradeOrderTrendBo;
import com.fly.mall.api.statistics.domain.bo.TradeStatisticsBo;
import com.fly.mall.api.statistics.domain.vo.DataComparisonRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeOrderCountRespVo;
import com.fly.mall.api.statistics.domain.vo.StatisticsTradeOrderSummaryRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeOrderTrendRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeSummaryRespVo;
import com.fly.mall.api.statistics.domain.vo.TradeStatisticsVo;
import com.fly.mall.api.statistics.domain.vo.TradeTrendSummaryRespVo;
import com.fly.mall.statistics.service.ITradeStatisticsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 交易统计 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/statistics/trade")
public class TradeStatisticsController extends BaseController {

    private final ITradeStatisticsService tradeStatisticsService;

    /**
     * 查询交易状况明细。
     */
    @PreAuthorize("@pms.hasPermission('mall:statistics:trade:list')")
    @GetMapping("/list")
    public R<List<TradeTrendSummaryRespVo>> list(StatisticsTimeRangeBo bo) {
        return R.ok(tradeStatisticsService.getTradeStatisticsList(bo));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:statistics:trade:list')")
    @GetMapping("/page")
    public R<PageVo<TradeStatisticsVo>> page(TradeStatisticsBo bo, PageBo page) {
        return R.ok(tradeStatisticsService.queryPageList(bo, page));
    }

    /**
     * 查询交易统计汇总。
     */
    @GetMapping("/summary")
    public R<DataComparisonRespVo<TradeSummaryRespVo>> summary() {
        return R.ok(tradeStatisticsService.getTradeSummaryComparison());
    }

    /**
     * 查询交易状况分析。
     */
    @GetMapping("/analyse")
    public R<DataComparisonRespVo<TradeTrendSummaryRespVo>> analyse(StatisticsTimeRangeBo bo) {
        return R.ok(tradeStatisticsService.getTradeStatisticsAnalyse(bo));
    }

    /**
     * 导出交易状况明细。
     */
    @GetMapping("/export-excel")
    public void exportExcel(StatisticsTimeRangeBo bo, HttpServletResponse response) {
        ExcelUtil.exportExcel(tradeStatisticsService.getTradeStatisticsList(bo), "交易统计", TradeTrendSummaryRespVo.class, response);
    }

    /**
     * 查询交易订单待处理数量。
     */
    @GetMapping("/order-count")
    public R<TradeOrderCountRespVo> orderCount() {
        return R.ok(tradeStatisticsService.getOrderCount());
    }

    /**
     * 查询交易订单对照数据。
     */
    @GetMapping("/order-comparison")
    public R<DataComparisonRespVo<StatisticsTradeOrderSummaryRespVo>> orderComparison() {
        return R.ok(tradeStatisticsService.getOrderComparison());
    }

    /**
     * 查询订单量趋势。
     */
    @GetMapping("/order-count-trend")
    public R<List<DataComparisonRespVo<TradeOrderTrendRespVo>>> orderCountTrend(TradeOrderTrendBo bo) {
        return R.ok(tradeStatisticsService.getOrderCountTrendComparison(bo));
    }

    /**
     * 查询所有交易统计。
     */
    @GetMapping("/getList")
    public R<List<TradeStatisticsVo>> allList(TradeStatisticsBo bo) {
        return R.ok(tradeStatisticsService.queryList(bo));
    }

    /**
     * 获取交易统计详情。
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

    /**
     * 新增或修改交易统计。
     */
    @Log(title = "交易统计", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:statistics:trade:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody TradeStatisticsBo bo) {
        return R.result(tradeStatisticsService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody TradeStatisticsBo bo) {
        return R.result(tradeStatisticsService.saveOrUpdate(bo));
    }

    /**
     * 删除交易统计。
     */
    @Log(title = "交易统计", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:statistics:trade:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(tradeStatisticsService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(tradeStatisticsService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

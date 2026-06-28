package com.fly.mall.statistics.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.statistics.bo.TradeStatisticsBo;
import com.fly.mall.api.domain.statistics.vo.TradeStatisticsVo;
import com.fly.mall.statistics.service.ITradeStatisticsService;
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
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/statistics/trade-statistics")
public class TradeStatisticsController extends BaseController {

    private final ITradeStatisticsService tradeStatisticsService;

    /**
     * 查询交易统计分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:statistics:trade-statistics:list')")
    @GetMapping("/list")
    public R<PageVo<TradeStatisticsVo>> list(TradeStatisticsBo bo, PageBo page) {
        return R.ok(tradeStatisticsService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:statistics:trade-statistics:list')")
    @GetMapping("/page")
    public R<PageVo<TradeStatisticsVo>> page(TradeStatisticsBo bo, PageBo page) {
        return R.ok(tradeStatisticsService.queryPageList(bo, page));
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
    @PreAuthorize("@pms.hasPermission('mall:statistics:trade-statistics:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody TradeStatisticsBo bo) {
        return R.ok(tradeStatisticsService.saveOrUpdate(bo));
    }

    /**
     * 删除交易统计。
     */
    @Log(title = "交易统计", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:statistics:trade-statistics:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(tradeStatisticsService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

package com.fly.mall.statistics.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.common.utils.ExcelUtil;
import com.fly.mall.api.statistics.domain.bo.ProductStatisticsBo;
import com.fly.mall.api.statistics.domain.bo.StatisticsTimeRangeBo;
import com.fly.mall.api.statistics.domain.vo.DataComparisonRespVo;
import com.fly.mall.api.statistics.domain.vo.ProductStatisticsRespVo;
import com.fly.mall.api.statistics.domain.vo.ProductStatisticsVo;
import com.fly.mall.statistics.service.IProductStatisticsService;
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
 * 管理后台 - 商品统计 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/admin/statistics/product-statistics", "/admin/statistics/product"})
public class ProductStatisticsController extends BaseController {

    private final IProductStatisticsService productStatisticsService;

    /**
     * 查询商品统计明细。
     */
    @PreAuthorize("@pms.hasPermission('mall:statistics:product-statistics:list')")
    @GetMapping("/list")
    public R<List<ProductStatisticsRespVo>> list(StatisticsTimeRangeBo bo) {
        return R.ok(productStatisticsService.getProductStatisticsList(bo));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:statistics:product-statistics:list')")
    @GetMapping("/page")
    public R<PageVo<ProductStatisticsVo>> page(ProductStatisticsBo bo, PageBo page) {
        return R.ok(productStatisticsService.queryPageList(bo, page));
    }

    /**
     * 查询商品统计分析。
     */
    @GetMapping("/analyse")
    public R<DataComparisonRespVo<ProductStatisticsRespVo>> analyse(StatisticsTimeRangeBo bo) {
        return R.ok(productStatisticsService.getProductStatisticsAnalyse(bo));
    }

    /**
     * 查询商品统计排行榜分页。
     */
    @GetMapping("/rank-page")
    public R<PageVo<ProductStatisticsRespVo>> rankPage(StatisticsTimeRangeBo bo, PageBo page) {
        return R.ok(productStatisticsService.getProductStatisticsRankPage(bo, page));
    }

    /**
     * 导出商品统计明细。
     */
    @GetMapping("/export-excel")
    public void exportExcel(StatisticsTimeRangeBo bo, HttpServletResponse response) {
        ExcelUtil.exportExcel(productStatisticsService.getProductStatisticsList(bo), "商品统计", ProductStatisticsRespVo.class, response);
    }

    /**
     * 查询所有商品统计。
     */
    @GetMapping("/getList")
    public R<List<ProductStatisticsVo>> allList(ProductStatisticsBo bo) {
        return R.ok(productStatisticsService.queryList(bo));
    }

    /**
     * 获取商品统计详情。
     */
    @GetMapping("/get/{id}")
    public R<ProductStatisticsVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productStatisticsService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<ProductStatisticsVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(productStatisticsService.queryById(id));
    }

    /**
     * 新增或修改商品统计。
     */
    @Log(title = "商品统计", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:statistics:product-statistics:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody ProductStatisticsBo bo) {
        return R.ok(productStatisticsService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody ProductStatisticsBo bo) {
        return R.ok(productStatisticsService.saveOrUpdate(bo));
    }

    /**
     * 删除商品统计。
     */
    @Log(title = "商品统计", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:statistics:product-statistics:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(productStatisticsService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(productStatisticsService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

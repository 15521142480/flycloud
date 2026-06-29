package com.fly.mall.statistics.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.statistics.domain.bo.ProductStatisticsBo;
import com.fly.mall.api.statistics.domain.vo.ProductStatisticsVo;
import com.fly.mall.statistics.service.IProductStatisticsService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 商品统计 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/statistics/product-statistics")
public class AppProductStatisticsController {

    private final IProductStatisticsService productStatisticsService;

    /**
     * 查询移动端商品统计分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<ProductStatisticsVo>> list(ProductStatisticsBo bo, PageBo page) {
        return R.ok(productStatisticsService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<ProductStatisticsVo>> page(ProductStatisticsBo bo, PageBo page) {
        return R.ok(productStatisticsService.queryPageList(bo, page));
    }

    /**
     * 获取移动端商品统计详情。
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

}

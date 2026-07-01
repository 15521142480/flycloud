package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.AfterSaleLogBo;
import com.fly.mall.api.trade.domain.vo.AfterSaleLogVo;
import com.fly.mall.trade.service.IAfterSaleLogService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 售后日志 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/after-sale-log")
public class AppAfterSaleLogController {

    private final IAfterSaleLogService afterSaleLogService;

    /**
     * 查询移动端售后日志分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<AfterSaleLogVo>> list(AfterSaleLogBo bo, PageBo page) {
        return R.ok(afterSaleLogService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<AfterSaleLogVo>> page(AfterSaleLogBo bo, PageBo page) {
        return R.ok(afterSaleLogService.queryPageList(bo, page));
    }

    /**
     * 获取移动端售后日志详情。
     */
    @GetMapping("/get/{id}")
    public R<AfterSaleLogVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(afterSaleLogService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<AfterSaleLogVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(afterSaleLogService.queryById(id));
    }

}

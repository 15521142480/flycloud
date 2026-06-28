package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.trade.bo.TradeConfigBo;
import com.fly.mall.api.domain.trade.vo.TradeConfigVo;
import com.fly.mall.trade.service.ITradeConfigService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 交易配置 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/trade-config")
public class AppTradeConfigController {

    private final ITradeConfigService tradeConfigService;

    /**
     * 查询移动端交易配置分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<TradeConfigVo>> list(TradeConfigBo bo, PageBo page) {
        return R.ok(tradeConfigService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<TradeConfigVo>> page(TradeConfigBo bo, PageBo page) {
        return R.ok(tradeConfigService.queryPageList(bo, page));
    }

    /**
     * 获取移动端交易配置详情。
     */
    @GetMapping("/get/{id}")
    public R<TradeConfigVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tradeConfigService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<TradeConfigVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeConfigService.queryById(id));
    }

}

package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.BrokerageRecordBo;
import com.fly.mall.api.trade.domain.vo.BrokerageRecordVo;
import com.fly.mall.trade.service.IBrokerageRecordService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 移动端 - 佣金记录 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/brokerage-record")
public class AppBrokerageRecordController {

    private final IBrokerageRecordService brokerageRecordService;

    /**
     * 查询移动端佣金记录分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<BrokerageRecordVo>> list(BrokerageRecordBo bo, PageBo page) {
        return R.ok(brokerageRecordService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<BrokerageRecordVo>> page(BrokerageRecordBo bo, PageBo page) {
        return R.ok(brokerageRecordService.queryPageList(bo, page));
    }

    /**
     * 获取移动端佣金记录详情。
     */
    @GetMapping("/get/{id}")
    public R<BrokerageRecordVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(brokerageRecordService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<BrokerageRecordVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(brokerageRecordService.queryById(id));
    }

    /**
     * 查询商品佣金价格。
     */
    @GetMapping("/get-product-brokerage-price")
    public R<Map<String, Integer>> getProductBrokeragePrice() {
        return R.ok(Map.of("firstBrokeragePrice", 0, "secondBrokeragePrice", 0));
    }

}

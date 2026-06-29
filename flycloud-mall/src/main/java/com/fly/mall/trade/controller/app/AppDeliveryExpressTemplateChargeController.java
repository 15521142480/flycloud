package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressTemplateChargeBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressTemplateChargeVo;
import com.fly.mall.trade.service.IDeliveryExpressTemplateChargeService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 运费模板计费规则 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/delivery-express-template-charge")
public class AppDeliveryExpressTemplateChargeController {

    private final IDeliveryExpressTemplateChargeService deliveryExpressTemplateChargeService;

    /**
     * 查询移动端运费模板计费规则分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<DeliveryExpressTemplateChargeVo>> list(DeliveryExpressTemplateChargeBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateChargeService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<DeliveryExpressTemplateChargeVo>> page(DeliveryExpressTemplateChargeBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateChargeService.queryPageList(bo, page));
    }

    /**
     * 获取移动端运费模板计费规则详情。
     */
    @GetMapping("/get/{id}")
    public R<DeliveryExpressTemplateChargeVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deliveryExpressTemplateChargeService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<DeliveryExpressTemplateChargeVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(deliveryExpressTemplateChargeService.queryById(id));
    }

}

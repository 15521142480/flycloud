package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressTemplateBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressTemplateVo;
import com.fly.mall.trade.service.IDeliveryExpressTemplateService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 运费模板 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/delivery-express-template")
public class AppDeliveryExpressTemplateController {

    private final IDeliveryExpressTemplateService deliveryExpressTemplateService;

    /**
     * 查询移动端运费模板分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<DeliveryExpressTemplateVo>> list(DeliveryExpressTemplateBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<DeliveryExpressTemplateVo>> page(DeliveryExpressTemplateBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateService.queryPageList(bo, page));
    }

    /**
     * 获取移动端运费模板详情。
     */
    @GetMapping("/get/{id}")
    public R<DeliveryExpressTemplateVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deliveryExpressTemplateService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<DeliveryExpressTemplateVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(deliveryExpressTemplateService.queryById(id));
    }

}

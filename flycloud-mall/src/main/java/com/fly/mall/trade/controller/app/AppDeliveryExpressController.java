package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressVo;
import com.fly.mall.trade.service.IDeliveryExpressService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 快递公司 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/delivery-express")
public class AppDeliveryExpressController {

    private final IDeliveryExpressService deliveryExpressService;

    /**
     * 查询移动端快递公司分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<DeliveryExpressVo>> list(DeliveryExpressBo bo, PageBo page) {
        return R.ok(deliveryExpressService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<DeliveryExpressVo>> page(DeliveryExpressBo bo, PageBo page) {
        return R.ok(deliveryExpressService.queryPageList(bo, page));
    }

    /**
     * 获取移动端快递公司详情。
     */
    @GetMapping("/get/{id}")
    public R<DeliveryExpressVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deliveryExpressService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<DeliveryExpressVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(deliveryExpressService.queryById(id));
    }

}

package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.trade.domain.bo.DeliveryPickUpStoreBo;
import com.fly.mall.api.trade.domain.vo.DeliveryPickUpStoreVo;
import com.fly.mall.trade.service.IDeliveryPickUpStoreService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 自提门店 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/delivery-pick-up-store")
public class AppDeliveryPickUpStoreController {

    private final IDeliveryPickUpStoreService deliveryPickUpStoreService;

    /**
     * 查询移动端自提门店分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<DeliveryPickUpStoreVo>> list(DeliveryPickUpStoreBo bo, PageBo page) {
        return R.ok(deliveryPickUpStoreService.queryPageList(bo, page));
    }

    /**
     * 获得分页列表。
     */
    @GetMapping("/page")
    public R<PageVo<DeliveryPickUpStoreVo>> page(DeliveryPickUpStoreBo bo, PageBo page) {
        return R.ok(deliveryPickUpStoreService.queryPageList(bo, page));
    }

    /**
     * 获取移动端自提门店详情。
     */
    @GetMapping("/get/{id}")
    public R<DeliveryPickUpStoreVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deliveryPickUpStoreService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<DeliveryPickUpStoreVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(deliveryPickUpStoreService.queryById(id));
    }

}

package com.fly.mall.trade.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.enums.StatusEnum;
import com.fly.mall.api.trade.domain.bo.DeliveryPickUpStoreBo;
import com.fly.mall.api.trade.domain.vo.DeliveryPickUpStoreVo;
import com.fly.mall.trade.service.IDeliveryPickUpStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 自提门店兼容控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping({"/trade/delivery/pick-up-store", "/app/trade/delivery/pick-up-store"})
public class AppDeliverPickUpStoreController {

    private final IDeliveryPickUpStoreService deliveryPickUpStoreService;

    /**
     * 获得启用的自提门店列表。
     */
    @GetMapping("/list")
    public R<List<DeliveryPickUpStoreVo>> getDeliveryPickUpStoreList(
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude) {
        DeliveryPickUpStoreBo bo = new DeliveryPickUpStoreBo();
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        return R.ok(deliveryPickUpStoreService.queryList(bo));
    }

    /**
     * 获得自提门店详情。
     */
    @GetMapping("/get")
    public R<DeliveryPickUpStoreVo> getOrder(@RequestParam("id") Long id) {
        return R.ok(deliveryPickUpStoreService.queryById(id));
    }

}

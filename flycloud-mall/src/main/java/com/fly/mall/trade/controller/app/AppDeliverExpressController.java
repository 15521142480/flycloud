package com.fly.mall.trade.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.enums.StatusEnum;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressVo;
import com.fly.mall.trade.service.IDeliveryExpressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

/**
 * 移动端 - 快递公司兼容控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping({"/trade/delivery/express", "/app/trade/delivery/express"})
public class AppDeliverExpressController {

    private final IDeliveryExpressService deliveryExpressService;

    /**
     * 获得启用的快递公司列表。
     */
    @GetMapping("/list")
    public R<List<DeliveryExpressVo>> getDeliveryExpressList() {
        DeliveryExpressBo bo = new DeliveryExpressBo();
        bo.setStatus(StatusEnum.ENABLE.getStatus());
        List<DeliveryExpressVo> list = deliveryExpressService.queryList(bo);
        list.sort(Comparator.comparing(DeliveryExpressVo::getSort, Comparator.nullsLast(Integer::compareTo)));
        return R.ok(list);
    }

}

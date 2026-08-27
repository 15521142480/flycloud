package com.fly.mall.trade.feign;

import com.fly.common.domain.model.R;
import com.fly.mall.api.trade.domain.vo.TradeOrderVo;
import com.fly.mall.api.trade.feign.ITradeOrderApi;
import com.fly.mall.trade.service.ITradeOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易订单 Feign 内部接口控制器。
 *
 * @author lxs
 * @date 2026-08-27
 */
@RestController
@RequiredArgsConstructor
public class TradeOrderApiController implements ITradeOrderApi {

    private final ITradeOrderService tradeOrderService;

    /**
     * 根据订单数据库主键或订单流水号查询订单详情。
     *
     * @param idOrNo 订单数据库主键或订单流水号
     * @return 订单详情
     */
    @Override
    public R<TradeOrderVo> getOrderByIdOrNo(String idOrNo) {
        return R.ok(tradeOrderService.queryByIdOrNo(idOrNo));
    }
}

package com.fly.mall.api.trade.feign;

import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import com.fly.mall.api.path.MallApiPaths;
import com.fly.mall.api.trade.domain.vo.TradeOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 交易订单内部远程调用接口。
 * <p>
 * 仅供已签名的 {@code /feign/**} 服务间调用使用，不能作为前端开放接口。
 *
 * @author lxs
 * @date 2026-08-27
 */
@FeignClient(value = ServerNames.MALL_SERVER_NAME, contextId = "TradeOrderApi")
public interface ITradeOrderApi {

    /**
     * 根据订单数据库主键或订单流水号查询订单详情。
     *
     * @param idOrNo 订单数据库主键或订单流水号
     * @return 订单详情
     */
    @GetMapping(MallApiPaths.PROVIDER_TRADE_ORDER_ID_OR_NO)
    R<TradeOrderVo> getOrderByIdOrNo(@RequestParam("idOrNo") String idOrNo);
}

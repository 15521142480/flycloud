package com.fly.mall.trade.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.trade.domain.bo.TradeOrderBo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateRespVo;
import com.fly.mall.api.trade.domain.vo.AppOrderExpressTrackRespDto;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderDetailRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderItemCommentCreateReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderItemRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderPageItemRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeProductSettlementRespVo;
import com.fly.mall.trade.service.ITradeOrderService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 移动端 - 交易订单 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/trade/order")
public class AppTradeOrderController {

    private final ITradeOrderService tradeOrderService;

    /**
     * 获得订单结算信息。
     */
    @GetMapping("/settlement")
    public R<AppTradeOrderSettlementRespVo> settlementOrder(AppTradeOrderSettlementReqVo settlementReqVo) {
        return R.ok(tradeOrderService.settlementOrder(UserUtils.getCurUserId(), settlementReqVo));
    }

    /**
     * 获得商品结算信息。
     */
    @GetMapping("/settlement-product")
    public R<List<AppTradeProductSettlementRespVo>> settlementProduct(@RequestParam("spuIds") List<Long> spuIds) {
        return R.ok(tradeOrderService.settlementProduct(spuIds));
    }

    /**
     * 查询移动端交易订单分页列表。
     */
    @GetMapping("/list")
    public R<PageVo<AppTradeOrderPageItemRespVo>> list(TradeOrderBo bo, PageBo page) {
        return R.ok(tradeOrderService.queryUserAppPageList(UserUtils.getCurUserId(), bo, page));
    }

    /**
     * 获得交易订单分页。
     */
    @GetMapping("/page")
    public R<PageVo<AppTradeOrderPageItemRespVo>> page(TradeOrderBo bo, PageBo page) {
        return R.ok(tradeOrderService.queryUserAppPageList(UserUtils.getCurUserId(), bo, page));
    }

    /**
     * 创建交易订单。
     */
    @PostMapping("/create")
    public R<AppTradeOrderCreateRespVo> createOrder(@RequestBody AppTradeOrderCreateReqVo createReqVo) {
        return R.ok(tradeOrderService.createAppOrder(UserUtils.getCurUserId(), createReqVo));
    }

    /**
     * 获取移动端交易订单详情。
     */
    @GetMapping("/get/{id}")
    public R<AppTradeOrderDetailRespVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(tradeOrderService.queryUserAppDetail(UserUtils.getCurUserId(), id));
    }

    /**
     * 获得交易订单详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<AppTradeOrderDetailRespVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.queryUserAppDetail(UserUtils.getCurUserId(), id));
    }

    /**
     * 支付回调后更新订单为已支付。
     */
    @PostMapping("/update-paid")
    public R<Boolean> updatePaid(@RequestBody PayOrderNotifyReq req) {
        tradeOrderService.updateOrderPaid(Long.valueOf(req.getMerchantOrderId()), req.getPayOrderId());
        return R.result(Boolean.TRUE);
    }

    /**
     * 查询订单物流轨迹。
     */
    @GetMapping("/get-express-track-list")
    public R<List<AppOrderExpressTrackRespDto>> getExpressTrackList(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.getAppExpressTrackList(UserUtils.getCurUserId(), id));
    }

    /**
     * 获得交易订单数量。
     */
    @GetMapping("/get-count")
    public R<Map<String, Long>> getOrderCount() {
        return R.ok(tradeOrderService.getOrderCount(UserUtils.getCurUserId()));
    }

    /**
     * 确认交易订单收货。
     */
    @PutMapping("/receive")
    public R<Boolean> receiveOrder(@RequestParam("id") Long id) {
        return R.result(tradeOrderService.receiveOrder(UserUtils.getCurUserId(), id));
    }

    /**
     * 取消交易订单。
     */
    @DeleteMapping("/cancel")
    public R<Boolean> cancelOrder(@RequestParam("id") Long id) {
        return R.result(tradeOrderService.cancelOrder(UserUtils.getCurUserId(), id));
    }

    /**
     * 删除交易订单。
     */
    @DeleteMapping("/delete")
    public R<Boolean> deleteOrder(@RequestParam("id") Long id) {
        return R.result(tradeOrderService.deleteOrder(UserUtils.getCurUserId(), id));
    }

    /**
     * 获得交易订单项。
     */
    @GetMapping("/item/get")
    public R<AppTradeOrderItemRespVo> getOrderItem(@RequestParam("id") Long id) {
        return R.ok(tradeOrderService.getAppOrderItem(UserUtils.getCurUserId(), id));
    }

    /**
     * 创建交易订单项评价。
     */
    @PostMapping("/item/create-comment")
    public R<Long> createOrderItemComment(@RequestBody AppTradeOrderItemCommentCreateReqVo createReqVo) {
        return R.ok(tradeOrderService.createOrderItemComment(UserUtils.getCurUserId(), createReqVo));
    }

    /**
     * 支付订单通知请求。
     */
    @Data
    public static class PayOrderNotifyReq {
        private String merchantOrderId;
        private Long payOrderId;
    }

}

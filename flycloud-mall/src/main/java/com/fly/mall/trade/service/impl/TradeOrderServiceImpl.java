package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.enums.mall.ProductSpuStatusEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.product.domain.ProductSku;
import com.fly.mall.api.product.domain.vo.AppProductPropertyValueDetailRespVo;
import com.fly.mall.api.product.domain.vo.ProductSkuVo;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;
import com.fly.mall.api.promotion.domain.RewardActivity;
import com.fly.mall.api.promotion.domain.bo.CouponBo;
import com.fly.mall.api.promotion.domain.vo.CouponVo;
import com.fly.mall.api.promotion.domain.vo.DiscountProductVo;
import com.fly.mall.api.promotion.domain.vo.RewardActivityVo;
import com.fly.mall.api.trade.domain.TradeOrder;
import com.fly.mall.api.trade.domain.TradeOrderItem;
import com.fly.mall.api.trade.domain.bo.TradeOrderBo;
import com.fly.mall.api.trade.domain.vo.AppOrderExpressTrackRespDto;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderDetailRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderItemCommentCreateReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderItemRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderPageItemRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeProductSettlementRespVo;
import com.fly.mall.api.trade.domain.vo.CartVo;
import com.fly.mall.api.trade.domain.vo.TradeOrderSummaryRespVo;
import com.fly.mall.api.trade.domain.vo.TradeOrderItemVo;
import com.fly.mall.api.trade.domain.vo.TradeOrderVo;
import com.fly.mall.product.service.IProductSkuService;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.promotion.service.ICouponService;
import com.fly.mall.promotion.service.IDiscountProductService;
import com.fly.mall.promotion.service.IRewardActivityService;
import com.fly.mall.trade.config.TradeOrderProperties;
import com.fly.mall.trade.mapper.TradeOrderItemMapper;
import com.fly.mall.trade.mapper.TradeOrderMapper;
import com.fly.mall.trade.service.ICartService;
import com.fly.mall.trade.service.ITradeOrderItemService;
import com.fly.mall.trade.service.ITradeOrderService;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;
import com.fly.system.api.pay.feign.IPayOrderApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 交易订单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class TradeOrderServiceImpl extends BaseServiceImpl<TradeOrderMapper, TradeOrder> implements ITradeOrderService {

    /**
     * 普通订单。
     */
    private static final int ORDER_TYPE_NORMAL = 0;

    /**
     * 待支付。
     */
    private static final int ORDER_STATUS_UNPAID = 0;

    /**
     * 待发货。
     */
    private static final int ORDER_STATUS_UNDELIVERED = 10;

    /**
     * 已发货。
     */
    private static final int ORDER_STATUS_DELIVERED = 20;

    /**
     * 已完成。
     */
    private static final int ORDER_STATUS_COMPLETED = 30;

    /**
     * 已取消。
     */
    private static final int ORDER_STATUS_CANCELED = 40;

    /**
     * 未退款。
     */
    private static final int REFUND_STATUS_NONE = 0;

    /**
     * 快递配送。
     */
    private static final int DELIVERY_TYPE_EXPRESS = 1;

    /**
     * 到店自提。
     */
    private static final int DELIVERY_TYPE_PICK_UP = 2;

    /**
     * 限时折扣。
     */
    private static final int PROMOTION_TYPE_DISCOUNT_ACTIVITY = 4;

    /**
     * 满减送。
     */
    private static final int PROMOTION_TYPE_REWARD_ACTIVITY = 5;

    /**
     * 优惠类型：减价。
     */
    private static final int DISCOUNT_TYPE_PRICE = 1;

    /**
     * 优惠类型：折扣。
     */
    private static final int DISCOUNT_TYPE_PERCENT = 2;

    /**
     * 优惠券状态：未使用。
     */
    private static final int COUPON_STATUS_UNUSED = 1;

    /**
     * 商品范围：全部商品。
     */
    private static final int PRODUCT_SCOPE_ALL = 1;

    /**
     * 商品范围：指定商品。
     */
    private static final int PRODUCT_SCOPE_SPU = 2;

    /**
     * 商品范围：指定分类。
     */
    private static final int PRODUCT_SCOPE_CATEGORY = 3;

    private final TradeOrderMapper baseMapper;
    private final TradeOrderItemMapper tradeOrderItemMapper;
    private final ITradeOrderItemService tradeOrderItemService;
    private final ICartService cartService;
    private final IProductSkuService productSkuService;
    private final IProductSpuService productSpuService;
    private final ICouponService couponService;
    private final IDiscountProductService discountProductService;
    private final IRewardActivityService rewardActivityService;
    private final IPayOrderApi payOrderApi;
    private final TradeOrderProperties tradeOrderProperties;

    /**
     * 查询交易订单详情。
     */
    @Override
    public TradeOrderVo queryById(Long id) {
        TradeOrderVo order = baseMapper.selectVoById(id);
        fillOrderItems(order);
        return order;
    }

    /**
     * 查询当前用户交易订单详情。
     */
    @Override
    public TradeOrderVo queryByUserAndId(Long userId, Long id) {
        TradeOrderVo order = queryById(id);
        if (order == null || !Objects.equals(order.getUserId(), userId)) {
            return null;
        }
        return order;
    }

    /**
     * 分页查询交易订单。
     */
    @Override
    public PageVo<TradeOrderVo> queryPageList(TradeOrderBo bo, PageBo pageBo) {
        LambdaQueryWrapper<TradeOrder> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(TradeOrder::getCreateTime);
        Page<TradeOrderVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<TradeOrderVo> page = this.build(result);
        fillOrderItems(page.getList());
        return page;
    }

    /**
     * 分页查询当前用户交易订单。
     */
    @Override
    public PageVo<TradeOrderVo> queryUserPageList(Long userId, TradeOrderBo bo, PageBo pageBo) {
        if (bo == null) {
            bo = new TradeOrderBo();
        }
        bo.setUserId(userId);
        return queryPageList(bo, pageBo);
    }

    /**
     * 分页查询当前用户移动端交易订单。
     */
    @Override
    public PageVo<AppTradeOrderPageItemRespVo> queryUserAppPageList(Long userId, TradeOrderBo bo, PageBo pageBo) {
        PageVo<TradeOrderVo> page = queryUserPageList(userId, bo, pageBo);
        PageVo<AppTradeOrderPageItemRespVo> respPage = new PageVo<>();
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        respPage.setList(page.getList().stream().map(this::buildAppPageItemRespVo).toList());
        return respPage;
    }

    /**
     * 查询当前用户移动端交易订单详情。
     */
    @Override
    public AppTradeOrderDetailRespVo queryUserAppDetail(Long userId, Long id) {
        TradeOrderVo order = queryByUserAndId(userId, id);
        return buildAppDetailRespVo(order);
    }

    /**
     * 查询当前用户移动端订单物流轨迹。
     */
    @Override
    public List<AppOrderExpressTrackRespDto> getAppExpressTrackList(Long userId, Long id) {
        TradeOrder order = validateUserOrder(userId, id);
        return buildOrderExpressTrackList(order);
    }

    /**
     * 查询后台订单物流轨迹。
     */
    @Override
    public List<AppOrderExpressTrackRespDto> getExpressTrackList(Long id) {
        return buildOrderExpressTrackList(validateOrderExists(id));
    }

    /**
     * 查询后台订单统计。
     */
    @Override
    public TradeOrderSummaryRespVo getOrderSummary(TradeOrderBo bo) {
        List<TradeOrderVo> orders = queryList(bo);
        TradeOrderSummaryRespVo respVo = new TradeOrderSummaryRespVo();
        respVo.setOrderCount(0L);
        respVo.setOrderPayPrice(0L);
        respVo.setAfterSaleCount(0L);
        respVo.setAfterSalePrice(0L);
        for (TradeOrderVo order : orders) {
            Integer refundStatus = order.getRefundStatus();
            if (refundStatus == null || refundStatus == REFUND_STATUS_NONE) {
                respVo.setOrderCount(respVo.getOrderCount() + 1);
                respVo.setOrderPayPrice(respVo.getOrderPayPrice() + defaultZero(order.getPayPrice()));
            } else {
                respVo.setAfterSaleCount(respVo.getAfterSaleCount() + 1);
                respVo.setAfterSalePrice(respVo.getAfterSalePrice() + defaultZero(order.getRefundPrice()));
            }
        }
        return respVo;
    }

    /**
     * 查询当前用户移动端交易订单项。
     */
    @Override
    public AppTradeOrderItemRespVo getAppOrderItem(Long userId, Long id) {
        TradeOrderItemVo item = tradeOrderItemService.queryById(id);
        if (item == null) {
            return null;
        }
        validateUserOrder(userId, item.getOrderId());
        return buildAppOrderItemRespVo(item);
    }

    /**
     * 当前用户创建交易订单项评价。
     */
    @Override
    public Long createOrderItemComment(Long userId, AppTradeOrderItemCommentCreateReqVo createReqVo) {
        if (createReqVo == null || createReqVo.getOrderItemId() == null) {
            throw new ServiceException("订单项不能为空");
        }
        TradeOrderItemVo item = tradeOrderItemService.queryById(createReqVo.getOrderItemId());
        if (item == null) {
            throw new ServiceException("订单项不存在");
        }
        TradeOrder order = validateUserOrder(userId, item.getOrderId());
        if (!Objects.equals(order.getStatus(), ORDER_STATUS_COMPLETED)) {
            throw new ServiceException("订单完成后才可以评价");
        }
        if (Boolean.TRUE.equals(item.getCommentStatus())) {
            throw new ServiceException("订单项已评价");
        }

        TradeOrderItem updateItem = new TradeOrderItem();
        updateItem.setId(item.getId());
        updateItem.setCommentStatus(true);
        updateItem.setUpdateBy(String.valueOf(userId));
        updateItem.setUpdateTime(LocalDateTime.now());
        tradeOrderItemMapper.updateById(updateItem);

        List<TradeOrderItemVo> items = tradeOrderItemService.queryListByOrderId(order.getId());
        boolean allCommented = items.stream()
                .filter(orderItem -> !Objects.equals(orderItem.getId(), item.getId()))
                .allMatch(orderItem -> Boolean.TRUE.equals(orderItem.getCommentStatus()));
        if (allCommented) {
            TradeOrder updateOrder = new TradeOrder();
            updateOrder.setId(order.getId());
            updateOrder.setCommentStatus(true);
            updateOrder.setUpdateBy(String.valueOf(userId));
            updateOrder.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(updateOrder);
        }
        return item.getId();
    }

    /**
     * 查询交易订单列表。
     */
    @Override
    public List<TradeOrderVo> queryList(TradeOrderBo bo) {
        LambdaQueryWrapper<TradeOrder> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(TradeOrder::getCreateTime);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 创建购物车订单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TradeOrderVo createOrder(Long userId, TradeOrderBo bo) {
        List<CartVo> carts = getOrderCarts(userId, bo);
        if (CollectionUtils.isEmpty(carts)) {
            throw new ServiceException("请选择要下单的商品");
        }

        LocalDateTime now = LocalDateTime.now();
        String operator = String.valueOf(userId);
        List<TradeOrderItem> items = buildOrderItems(userId, carts, operator, now);
        int totalPrice = items.stream().map(TradeOrderItem::getPayPrice).filter(Objects::nonNull).reduce(0, Integer::sum);
        int productCount = items.stream().map(TradeOrderItem::getCount).filter(Objects::nonNull).reduce(0, Integer::sum);

        TradeOrder order = BeanUtil.toBean(bo, TradeOrder.class);
        order.setId(null);
        order.setNo(generateOrderNo());
        order.setType(ORDER_TYPE_NORMAL);
        order.setTerminal(order.getTerminal() == null ? 20 : order.getTerminal());
        order.setUserId(userId);
        order.setStatus(ORDER_STATUS_UNPAID);
        order.setProductCount(productCount);
        order.setCommentStatus(false);
        order.setPayStatus(false);
        order.setTotalPrice(totalPrice);
        order.setDiscountPrice(defaultZero(order.getDiscountPrice()));
        order.setDeliveryPrice(defaultZero(order.getDeliveryPrice()));
        order.setAdjustPrice(defaultZero(order.getAdjustPrice()));
        order.setCouponPrice(defaultZero(order.getCouponPrice()));
        order.setPointPrice(defaultZero(order.getPointPrice()));
        order.setVipPrice(defaultZero(order.getVipPrice()));
        order.setPayPrice(Math.max(0, totalPrice + order.getDeliveryPrice() + order.getAdjustPrice()
                - order.getDiscountPrice() - order.getCouponPrice() - order.getPointPrice() - order.getVipPrice()));
        order.setDeliveryType(order.getDeliveryType() == null ? DELIVERY_TYPE_EXPRESS : order.getDeliveryType());
        order.setRefundStatus(REFUND_STATUS_NONE);
        order.setRefundPrice(0);
        order.setUsePoint(defaultZero(order.getUsePoint()));
        order.setGivePoint(defaultZero(order.getGivePoint()));
        order.setRefundPoint(defaultZero(order.getRefundPoint()));
        order.setIsDeleted(false);
        order.setCreateBy(operator);
        order.setCreateTime(now);
        order.setUpdateBy(operator);
        order.setUpdateTime(now);
        baseMapper.insert(order);

        for (TradeOrderItem item : items) {
            item.setOrderId(order.getId());
        }
        tradeOrderItemMapper.insertBatch(items);
        for (TradeOrderItem item : items) {
            productSkuService.reduceStock(item.getSkuId(), item.getCount());
        }
        cartService.deleteCart(userId, carts.stream().map(CartVo::getId).collect(Collectors.toList()));
        return queryById(order.getId());
    }

    /**
     * 移动端结算订单。
     */
    @Override
    public AppTradeOrderSettlementRespVo settlementOrder(Long userId, AppTradeOrderSettlementReqVo settlementReqVo) {
        List<OrderItemSource> sources = resolveOrderItemSources(userId, settlementReqVo == null ? null : settlementReqVo.getItems());
        Map<Long, DiscountProductVo> discountProductMap = getMatchDiscountProductMap(
                CollectionUtils.convertSet(sources, source -> source.sku().getId()));
        List<AppTradeOrderSettlementRespVo.Item> items = new ArrayList<>(sources.size());
        int totalPrice = 0;
        int discountPrice = 0;
        List<Long> spuIds = new ArrayList<>(sources.size());
        List<Long> categoryIds = new ArrayList<>(sources.size());
        for (OrderItemSource source : sources) {
            AppTradeOrderSettlementRespVo.Item item = new AppTradeOrderSettlementRespVo.Item();
            item.setCategoryId(source.spu().getCategoryId());
            item.setSpuId(source.spu().getId());
            item.setSpuName(source.spu().getName());
            item.setSkuId(source.sku().getId());
            item.setPrice(defaultZero(source.sku().getPrice()));
            item.setPicUrl(source.sku().getPicUrl() != null ? source.sku().getPicUrl() : source.spu().getPicUrl());
            item.setProperties(convertAppProductProperties(source.sku().getProperties()));
            item.setCartId(source.cartId());
            item.setCount(source.count());
            items.add(item);
            totalPrice += item.getPrice() * item.getCount();
            discountPrice += calculateDiscountPrice(item.getPrice(), discountProductMap.get(item.getSkuId())) * item.getCount();
            spuIds.add(item.getSpuId());
            categoryIds.add(item.getCategoryId());
        }
        int couponBasePrice = Math.max(0, totalPrice - discountPrice);
        List<AppTradeOrderSettlementRespVo.Coupon> coupons = buildSettlementCoupons(userId, couponBasePrice, spuIds, categoryIds);
        int couponPrice = calculateSelectedCouponPrice(settlementReqVo == null ? null : settlementReqVo.getCouponId(),
                coupons, couponBasePrice, true);

        AppTradeOrderSettlementRespVo respVo = new AppTradeOrderSettlementRespVo();
        respVo.setType(ORDER_TYPE_NORMAL);
        respVo.setItems(items);
        respVo.setCoupons(coupons);
        respVo.setPrice(new AppTradeOrderSettlementRespVo.Price(totalPrice, discountPrice, 0, couponPrice, 0, 0,
                Math.max(0, couponBasePrice - couponPrice)));
        respVo.setUsePoint(0);
        respVo.setTotalPoint(0);
        respVo.setPromotions(Collections.emptyList());
        return respVo;
    }

    /**
     * 移动端查询商品结算信息。
     */
    @Override
    public List<AppTradeProductSettlementRespVo> settlementProduct(List<Long> spuIds) {
        if (CollectionUtils.isEmpty(spuIds)) {
            return Collections.emptyList();
        }
        List<ProductSkuVo> allSkus = productSkuService.queryListBySpuIds(spuIds);
        Map<Long, List<ProductSkuVo>> skuMap = allSkus.stream().collect(Collectors.groupingBy(ProductSkuVo::getSpuId));
        Map<Long, DiscountProductVo> discountProductMap = getMatchDiscountProductMap(
                CollectionUtils.convertSet(allSkus, ProductSkuVo::getId));
        List<RewardActivityVo> rewardActivities = rewardActivityService.getMatchRewardActivityListBySpuIds(spuIds);
        List<AppTradeProductSettlementRespVo> list = new ArrayList<>(spuIds.size());
        for (Long spuId : spuIds) {
            AppTradeProductSettlementRespVo respVo = new AppTradeProductSettlementRespVo();
            respVo.setSpuId(spuId);
            List<AppTradeProductSettlementRespVo.Sku> skus = skuMap.getOrDefault(spuId, Collections.emptyList())
                    .stream().map(sku -> {
                        DiscountProductVo discountProduct = discountProductMap.get(sku.getId());
                        AppTradeProductSettlementRespVo.Sku skuRespVo = new AppTradeProductSettlementRespVo.Sku();
                        skuRespVo.setId(sku.getId());
                        Integer discountPrice = calculateDiscountPrice(defaultZero(sku.getPrice()), discountProduct);
                        if (discountPrice > 0) {
                            skuRespVo.setPromotionPrice(Math.max(0, defaultZero(sku.getPrice()) - discountPrice));
                            skuRespVo.setPromotionType(PROMOTION_TYPE_DISCOUNT_ACTIVITY);
                            skuRespVo.setPromotionId(discountProduct.getId());
                            skuRespVo.setPromotionEndTime(discountProduct.getActivityEndTime());
                        } else {
                            skuRespVo.setPromotionPrice(defaultZero(sku.getPrice()));
                        }
                        return skuRespVo;
                    }).collect(Collectors.toList());
            respVo.setSkus(skus);
            respVo.setRewardActivity(buildRewardActivityRespVo(spuId, rewardActivities));
            list.add(respVo);
        }
        return list;
    }

    /**
     * 移动端创建交易订单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppTradeOrderCreateRespVo createAppOrder(Long userId, AppTradeOrderCreateReqVo createReqVo) {
        List<OrderItemSource> sources = resolveOrderItemSources(userId, createReqVo == null ? null : createReqVo.getItems());
        Map<Long, DiscountProductVo> discountProductMap = getMatchDiscountProductMap(
                CollectionUtils.convertSet(sources, source -> source.sku().getId()));
        LocalDateTime now = LocalDateTime.now();
        String operator = String.valueOf(userId);
        List<TradeOrderItem> items = buildAppOrderItems(userId, sources, operator, now, discountProductMap);
        int totalPrice = items.stream()
                .map(item -> defaultZero(item.getPrice()) * defaultZero(item.getCount()))
                .reduce(0, Integer::sum);
        int discountPrice = items.stream().map(TradeOrderItem::getDiscountPrice).filter(Objects::nonNull).reduce(0, Integer::sum);
        int couponPrice = calculateAppOrderCouponPrice(userId, createReqVo == null ? null : createReqVo.getCouponId(),
                Math.max(0, totalPrice - discountPrice), sources);
        applyCouponPriceToItems(items, couponPrice);
        int productCount = items.stream().map(TradeOrderItem::getCount).filter(Objects::nonNull).reduce(0, Integer::sum);

        TradeOrder order = new TradeOrder();
        order.setNo(generateOrderNo());
        order.setType(ORDER_TYPE_NORMAL);
        order.setTerminal(20);
        order.setUserId(userId);
        order.setUserRemark(createReqVo == null ? null : createReqVo.getRemark());
        order.setStatus(ORDER_STATUS_UNPAID);
        order.setProductCount(productCount);
        order.setCommentStatus(false);
        order.setPayStatus(false);
        order.setTotalPrice(totalPrice);
        order.setDiscountPrice(discountPrice);
        order.setDeliveryPrice(0);
        order.setAdjustPrice(0);
        order.setCouponId(createReqVo == null ? null : createReqVo.getCouponId());
        order.setCouponPrice(couponPrice);
        order.setPointPrice(0);
        order.setVipPrice(0);
        order.setPayPrice(Math.max(0, totalPrice - discountPrice - couponPrice));
        order.setDeliveryType(createReqVo == null || createReqVo.getDeliveryType() == null
                ? DELIVERY_TYPE_EXPRESS : createReqVo.getDeliveryType());
        order.setReceiverName(createReqVo == null ? null : createReqVo.getReceiverName());
        order.setReceiverMobile(createReqVo == null ? null : createReqVo.getReceiverMobile());
        order.setPickUpStoreId(createReqVo == null ? null : createReqVo.getPickUpStoreId());
        order.setRefundStatus(REFUND_STATUS_NONE);
        order.setRefundPrice(0);
        order.setUsePoint(0);
        order.setGivePoint(0);
        order.setRefundPoint(0);
        order.setIsDeleted(false);
        order.setCreateBy(operator);
        order.setCreateTime(now);
        order.setUpdateBy(operator);
        order.setUpdateTime(now);
        baseMapper.insert(order);

        for (TradeOrderItem item : items) {
            item.setOrderId(order.getId());
        }
        tradeOrderItemMapper.insertBatch(items);
        for (TradeOrderItem item : items) {
            productSkuService.reduceStock(item.getSkuId(), item.getCount());
        }
        List<Long> cartIds = sources.stream().map(source -> source.cartId()).filter(Objects::nonNull).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(cartIds)) {
            cartService.deleteCart(userId, cartIds);
        }
        if (order.getCouponId() != null) {
            couponService.useCoupon(order.getCouponId(), userId, order.getId());
        }

        Long payOrderId = createPayOrder(userId, order);
        TradeOrder updateOrder = new TradeOrder();
        updateOrder.setId(order.getId());
        updateOrder.setPayOrderId(payOrderId);
        updateOrder.setUpdateBy(operator);
        updateOrder.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(updateOrder);

        AppTradeOrderCreateRespVo respVo = new AppTradeOrderCreateRespVo();
        respVo.setId(order.getId());
        respVo.setPayOrderId(payOrderId);
        return respVo;
    }

    /**
     * 查询当前用户订单数量。
     */
    @Override
    public Map<String, Long> getOrderCount(Long userId) {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("allCount", countByUserAndStatus(userId, null, null));
        result.put("unpaidCount", countByUserAndStatus(userId, ORDER_STATUS_UNPAID, null));
        result.put("undeliveredCount", countByUserAndStatus(userId, ORDER_STATUS_UNDELIVERED, null));
        result.put("deliveredCount", countByUserAndStatus(userId, ORDER_STATUS_DELIVERED, null));
        result.put("uncommentedCount", countByUserAndStatus(userId, ORDER_STATUS_COMPLETED, false));
        result.put("afterSaleCount", 0L);
        return result;
    }

    /**
     * 支付回调后更新订单为已支付。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderPaid(Long id, Long payOrderId) {
        TradeOrder order = validateOrderExists(id);
        if (!Objects.equals(order.getStatus(), ORDER_STATUS_UNPAID) || Boolean.TRUE.equals(order.getPayStatus())) {
            if (Objects.equals(order.getPayOrderId(), payOrderId)) {
                return;
            }
            throw new ServiceException("订单支付单不匹配");
        }

        PayOrderRespVo payOrder = validatePayOrderPaid(order, payOrderId);
        TradeOrder updateOrder = new TradeOrder();
        updateOrder.setStatus(ORDER_STATUS_UNDELIVERED);
        updateOrder.setPayStatus(true);
        updateOrder.setPayTime(LocalDateTime.now());
        updateOrder.setPayChannelCode(payOrder.getChannelCode());
        updateOrder.setUpdateBy(String.valueOf(order.getUserId()));
        updateOrder.setUpdateTime(LocalDateTime.now());

        LambdaUpdateWrapper<TradeOrder> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(TradeOrder::getId, order.getId());
        updateWrapper.eq(TradeOrder::getStatus, ORDER_STATUS_UNPAID);
        updateWrapper.eq(TradeOrder::getPayStatus, false);
        int updateCount = baseMapper.update(updateOrder, updateWrapper);
        if (updateCount == 0) {
            throw new ServiceException("订单状态不是待支付，更新支付状态失败");
        }
    }

    /**
     * 当前用户取消订单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(Long userId, Long id) {
        TradeOrder order = validateUserOrder(userId, id);
        if (!Objects.equals(order.getStatus(), ORDER_STATUS_UNPAID)) {
            throw new ServiceException("只有待支付订单可以取消");
        }
        TradeOrder entity = new TradeOrder();
        entity.setId(id);
        entity.setStatus(ORDER_STATUS_CANCELED);
        entity.setCancelTime(LocalDateTime.now());
        entity.setCancelType(10);
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(LocalDateTime.now());
        boolean success = baseMapper.updateById(entity) > 0;
        if (success) {
            returnOrderStock(order.getId());
            returnOrderCoupon(order);
        }
        return success;
    }

    /**
     * 当前用户删除订单。
     */
    @Override
    public Boolean deleteOrder(Long userId, Long id) {
        TradeOrder order = validateUserOrder(userId, id);
        if (!Objects.equals(order.getStatus(), ORDER_STATUS_CANCELED)
                && !Objects.equals(order.getStatus(), ORDER_STATUS_COMPLETED)) {
            throw new ServiceException("只有已取消或已完成订单可以删除");
        }
        TradeOrder entity = new TradeOrder();
        entity.setId(id);
        entity.setIsDeleted(true);
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 当前用户确认收货。
     */
    @Override
    public Boolean receiveOrder(Long userId, Long id) {
        TradeOrder order = validateUserOrder(userId, id);
        if (!Objects.equals(order.getStatus(), ORDER_STATUS_DELIVERED)) {
            throw new ServiceException("只有已发货订单可以确认收货");
        }
        TradeOrder entity = new TradeOrder();
        entity.setId(id);
        entity.setStatus(ORDER_STATUS_COMPLETED);
        entity.setReceiveTime(LocalDateTime.now());
        entity.setFinishTime(LocalDateTime.now());
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 系统自动确认收货。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer receiveOrderBySystem() {
        LocalDateTime expireTime = LocalDateTime.now().minus(tradeOrderProperties.getReceiveExpireTime());
        List<TradeOrder> orders = baseMapper.selectList(new LambdaQueryWrapper<TradeOrder>()
                .eq(TradeOrder::getIsDeleted, false)
                .eq(TradeOrder::getStatus, ORDER_STATUS_DELIVERED)
                .lt(TradeOrder::getDeliveryTime, expireTime));
        int count = 0;
        for (TradeOrder order : orders) {
            TradeOrder entity = new TradeOrder();
            entity.setId(order.getId());
            entity.setStatus(ORDER_STATUS_COMPLETED);
            entity.setReceiveTime(LocalDateTime.now());
            entity.setFinishTime(LocalDateTime.now());
            entity.setUpdateBy("system");
            entity.setUpdateTime(LocalDateTime.now());

            LambdaUpdateWrapper<TradeOrder> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(TradeOrder::getId, order.getId());
            updateWrapper.eq(TradeOrder::getStatus, ORDER_STATUS_DELIVERED);
            count += baseMapper.update(entity, updateWrapper);
        }
        return count;
    }

    /**
     * 系统自动取消超时未支付订单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer cancelOrderBySystem() {
        LocalDateTime expireTime = LocalDateTime.now().minus(tradeOrderProperties.getPayExpireTime());
        List<TradeOrder> orders = baseMapper.selectList(new LambdaQueryWrapper<TradeOrder>()
                .eq(TradeOrder::getIsDeleted, false)
                .eq(TradeOrder::getStatus, ORDER_STATUS_UNPAID)
                .lt(TradeOrder::getCreateTime, expireTime));
        int count = 0;
        for (TradeOrder order : orders) {
            if (isPayOrderSuccess(order)) {
                continue;
            }
            TradeOrder entity = new TradeOrder();
            entity.setId(order.getId());
            entity.setStatus(ORDER_STATUS_CANCELED);
            entity.setCancelTime(LocalDateTime.now());
            entity.setCancelType(20);
            entity.setUpdateBy("system");
            entity.setUpdateTime(LocalDateTime.now());

            LambdaUpdateWrapper<TradeOrder> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(TradeOrder::getId, order.getId());
            updateWrapper.eq(TradeOrder::getStatus, ORDER_STATUS_UNPAID);
            int updateCount = baseMapper.update(entity, updateWrapper);
            if (updateCount > 0) {
                returnOrderStock(order.getId());
                returnOrderCoupon(order);
            }
            count += updateCount;
        }
        return count;
    }

    /**
     * 后台订单发货。
     */
    @Override
    public Boolean deliveryOrder(TradeOrderBo bo) {
        if (bo == null || bo.getId() == null) {
            throw new ServiceException("订单编号不能为空");
        }
        TradeOrder order = validateOrderExists(bo.getId());
        if (!Objects.equals(order.getStatus(), ORDER_STATUS_UNDELIVERED)) {
            throw new ServiceException("只有待发货订单可以发货");
        }
        if (!Objects.equals(order.getDeliveryType(), DELIVERY_TYPE_EXPRESS)) {
            throw new ServiceException("只有快递配送订单可以发货");
        }
        if (bo.getLogisticsId() != null && bo.getLogisticsId() > 0 && StringUtils.isBlank(bo.getLogisticsNo())) {
            throw new ServiceException("物流单号不能为空");
        }
        TradeOrder entity = new TradeOrder();
        entity.setId(order.getId());
        entity.setStatus(ORDER_STATUS_DELIVERED);
        entity.setLogisticsId(bo.getLogisticsId() == null ? 0L : bo.getLogisticsId());
        entity.setLogisticsNo(bo.getLogisticsId() == null || bo.getLogisticsId() == 0 ? "" : bo.getLogisticsNo());
        entity.setDeliveryTime(LocalDateTime.now());
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 后台更新订单备注。
     */
    @Override
    public Boolean updateOrderRemark(TradeOrderBo bo) {
        if (bo == null || bo.getId() == null) {
            throw new ServiceException("订单编号不能为空");
        }
        validateOrderExists(bo.getId());
        TradeOrder entity = new TradeOrder();
        entity.setId(bo.getId());
        entity.setRemark(bo.getRemark());
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 后台更新订单价格。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateOrderPrice(TradeOrderBo bo) {
        if (bo == null || bo.getId() == null || bo.getAdjustPrice() == null) {
            throw new ServiceException("订单编号和调价金额不能为空");
        }
        TradeOrder order = validateOrderExists(bo.getId());
        if (Boolean.TRUE.equals(order.getPayStatus())) {
            throw new ServiceException("已支付订单不能调价");
        }
        if (defaultZero(order.getAdjustPrice()) > 0) {
            throw new ServiceException("订单已调价，不能重复调价");
        }
        int newPayPrice = defaultZero(order.getPayPrice()) + bo.getAdjustPrice();
        if (newPayPrice <= 0) {
            throw new ServiceException("调价后支付金额必须大于 0");
        }

        LocalDateTime now = LocalDateTime.now();
        String operator = String.valueOf(UserUtils.getCurUserId());
        TradeOrder entity = new TradeOrder();
        entity.setId(order.getId());
        entity.setAdjustPrice(defaultZero(order.getAdjustPrice()) + bo.getAdjustPrice());
        entity.setPayPrice(newPayPrice);
        entity.setUpdateBy(operator);
        entity.setUpdateTime(now);
        baseMapper.updateById(entity);

        List<TradeOrderItemVo> items = tradeOrderItemService.queryListByOrderId(order.getId());
        List<Integer> adjustPrices = dividePrice(items, bo.getAdjustPrice());
        for (int i = 0; i < items.size(); i++) {
            TradeOrderItemVo item = items.get(i);
            Integer adjustPrice = adjustPrices.get(i);
            TradeOrderItem updateItem = new TradeOrderItem();
            updateItem.setId(item.getId());
            updateItem.setAdjustPrice(defaultZero(item.getAdjustPrice()) + adjustPrice);
            updateItem.setPayPrice(defaultZero(item.getPayPrice()) + adjustPrice);
            updateItem.setUpdateBy(operator);
            updateItem.setUpdateTime(now);
            tradeOrderItemMapper.updateById(updateItem);
        }
        return true;
    }

    /**
     * 后台更新订单收货地址。
     */
    @Override
    public Boolean updateOrderAddress(TradeOrderBo bo) {
        if (bo == null || bo.getId() == null) {
            throw new ServiceException("订单编号不能为空");
        }
        TradeOrder order = validateOrderExists(bo.getId());
        if (!Objects.equals(order.getStatus(), ORDER_STATUS_UNDELIVERED)) {
            throw new ServiceException("只有待发货订单可以修改收货地址");
        }
        TradeOrder entity = new TradeOrder();
        entity.setId(order.getId());
        entity.setReceiverName(bo.getReceiverName());
        entity.setReceiverMobile(bo.getReceiverMobile());
        entity.setReceiverAreaId(bo.getReceiverAreaId());
        entity.setReceiverDetailAddress(bo.getReceiverDetailAddress());
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 后台按订单编号核销订单。
     */
    @Override
    public Boolean pickUpOrderByAdmin(Long userId, Long id) {
        return pickUpOrder(userId, validateOrderExists(id));
    }

    /**
     * 后台按核销码核销订单。
     */
    @Override
    public Boolean pickUpOrderByAdmin(Long userId, String pickUpVerifyCode) {
        TradeOrderVo order = getByPickUpVerifyCode(pickUpVerifyCode);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        return pickUpOrder(userId, validateOrderExists(order.getId()));
    }

    /**
     * 根据自提核销码查询订单。
     */
    @Override
    public TradeOrderVo getByPickUpVerifyCode(String pickUpVerifyCode) {
        if (StringUtils.isBlank(pickUpVerifyCode)) {
            throw new ServiceException("核销码不能为空");
        }
        TradeOrderBo query = new TradeOrderBo();
        query.setPickUpVerifyCode(pickUpVerifyCode);
        return queryList(query).stream().findFirst().orElse(null);
    }

    /**
     * 新增或修改交易订单。
     */
    @Override
    public Boolean saveOrUpdate(TradeOrderBo bo) {
        TradeOrder entity = BeanUtil.toBean(bo, TradeOrder.class);
        boolean isUpdate = entity.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(false);
        }
        if (isUpdate) {
            return baseMapper.updateById(entity) > 0;
        }
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        return baseMapper.insert(entity) > 0;
    }

    /**
     * 校验并批量删除交易订单。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            TradeOrder entity = new TradeOrder();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建交易订单查询条件。
     */
    private LambdaQueryWrapper<TradeOrder> buildQueryWrapper(TradeOrderBo bo) {
        LambdaQueryWrapper<TradeOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(TradeOrder::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, TradeOrder::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getNo()), TradeOrder::getNo, bo.getNo());
        lqw.eq(bo.getType() != null, TradeOrder::getType, bo.getType());
        lqw.eq(bo.getTerminal() != null, TradeOrder::getTerminal, bo.getTerminal());
        lqw.eq(bo.getUserId() != null, TradeOrder::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserIp()), TradeOrder::getUserIp, bo.getUserIp());
        lqw.like(StringUtils.isNotBlank(bo.getUserRemark()), TradeOrder::getUserRemark, bo.getUserRemark());
        lqw.eq(bo.getStatus() != null, TradeOrder::getStatus, bo.getStatus());
        lqw.eq(bo.getProductCount() != null, TradeOrder::getProductCount, bo.getProductCount());
        lqw.eq(bo.getFinishTime() != null, TradeOrder::getFinishTime, bo.getFinishTime());
        lqw.eq(bo.getCancelTime() != null, TradeOrder::getCancelTime, bo.getCancelTime());
        lqw.eq(bo.getCancelType() != null, TradeOrder::getCancelType, bo.getCancelType());
        lqw.like(StringUtils.isNotBlank(bo.getRemark()), TradeOrder::getRemark, bo.getRemark());
        lqw.eq(bo.getCommentStatus() != null, TradeOrder::getCommentStatus, bo.getCommentStatus());
        lqw.eq(bo.getBrokerageUserId() != null, TradeOrder::getBrokerageUserId, bo.getBrokerageUserId());
        lqw.eq(bo.getPayOrderId() != null, TradeOrder::getPayOrderId, bo.getPayOrderId());
        lqw.eq(bo.getPayStatus() != null, TradeOrder::getPayStatus, bo.getPayStatus());
        lqw.eq(bo.getPayTime() != null, TradeOrder::getPayTime, bo.getPayTime());
        lqw.like(StringUtils.isNotBlank(bo.getPayChannelCode()), TradeOrder::getPayChannelCode, bo.getPayChannelCode());
        lqw.eq(bo.getTotalPrice() != null, TradeOrder::getTotalPrice, bo.getTotalPrice());
        lqw.eq(bo.getDiscountPrice() != null, TradeOrder::getDiscountPrice, bo.getDiscountPrice());
        lqw.eq(bo.getDeliveryPrice() != null, TradeOrder::getDeliveryPrice, bo.getDeliveryPrice());
        lqw.eq(bo.getAdjustPrice() != null, TradeOrder::getAdjustPrice, bo.getAdjustPrice());
        lqw.eq(bo.getPayPrice() != null, TradeOrder::getPayPrice, bo.getPayPrice());
        lqw.eq(bo.getDeliveryType() != null, TradeOrder::getDeliveryType, bo.getDeliveryType());
        lqw.eq(bo.getLogisticsId() != null, TradeOrder::getLogisticsId, bo.getLogisticsId());
        lqw.like(StringUtils.isNotBlank(bo.getLogisticsNo()), TradeOrder::getLogisticsNo, bo.getLogisticsNo());
        lqw.eq(bo.getDeliveryTime() != null, TradeOrder::getDeliveryTime, bo.getDeliveryTime());
        lqw.eq(bo.getReceiveTime() != null, TradeOrder::getReceiveTime, bo.getReceiveTime());
        lqw.like(StringUtils.isNotBlank(bo.getReceiverName()), TradeOrder::getReceiverName, bo.getReceiverName());
        lqw.like(StringUtils.isNotBlank(bo.getReceiverMobile()), TradeOrder::getReceiverMobile, bo.getReceiverMobile());
        lqw.eq(bo.getReceiverAreaId() != null, TradeOrder::getReceiverAreaId, bo.getReceiverAreaId());
        lqw.like(StringUtils.isNotBlank(bo.getReceiverDetailAddress()), TradeOrder::getReceiverDetailAddress, bo.getReceiverDetailAddress());
        lqw.eq(bo.getPickUpStoreId() != null, TradeOrder::getPickUpStoreId, bo.getPickUpStoreId());
        lqw.like(StringUtils.isNotBlank(bo.getPickUpVerifyCode()), TradeOrder::getPickUpVerifyCode, bo.getPickUpVerifyCode());
        lqw.eq(bo.getRefundStatus() != null, TradeOrder::getRefundStatus, bo.getRefundStatus());
        lqw.eq(bo.getRefundPrice() != null, TradeOrder::getRefundPrice, bo.getRefundPrice());
        lqw.eq(bo.getCouponId() != null, TradeOrder::getCouponId, bo.getCouponId());
        lqw.eq(bo.getCouponPrice() != null, TradeOrder::getCouponPrice, bo.getCouponPrice());
        lqw.eq(bo.getUsePoint() != null, TradeOrder::getUsePoint, bo.getUsePoint());
        lqw.eq(bo.getPointPrice() != null, TradeOrder::getPointPrice, bo.getPointPrice());
        lqw.eq(bo.getGivePoint() != null, TradeOrder::getGivePoint, bo.getGivePoint());
        lqw.eq(bo.getRefundPoint() != null, TradeOrder::getRefundPoint, bo.getRefundPoint());
        lqw.eq(bo.getVipPrice() != null, TradeOrder::getVipPrice, bo.getVipPrice());
        lqw.eq(bo.getSeckillActivityId() != null, TradeOrder::getSeckillActivityId, bo.getSeckillActivityId());
        lqw.eq(bo.getBargainActivityId() != null, TradeOrder::getBargainActivityId, bo.getBargainActivityId());
        lqw.eq(bo.getBargainRecordId() != null, TradeOrder::getBargainRecordId, bo.getBargainRecordId());
        lqw.eq(bo.getCombinationActivityId() != null, TradeOrder::getCombinationActivityId, bo.getCombinationActivityId());
        lqw.eq(bo.getCombinationHeadId() != null, TradeOrder::getCombinationHeadId, bo.getCombinationHeadId());
        lqw.eq(bo.getCombinationRecordId() != null, TradeOrder::getCombinationRecordId, bo.getCombinationRecordId());
        lqw.eq(bo.getPointActivityId() != null, TradeOrder::getPointActivityId, bo.getPointActivityId());
        return lqw;
    }

    /**
     * 获取本次下单的购物车商品。
     */
    private List<CartVo> getOrderCarts(Long userId, TradeOrderBo bo) {
        List<CartVo> userCarts = cartService.queryUserCartList(userId);
        if (CollectionUtils.isEmpty(userCarts)) {
            return List.of();
        }
        if (!CollectionUtils.isEmpty(bo.getCartIds())) {
            return userCarts.stream()
                    .filter(cart -> bo.getCartIds().contains(cart.getId()))
                    .collect(Collectors.toList());
        }
        return userCarts.stream().filter(cart -> Boolean.TRUE.equals(cart.getSelected())).collect(Collectors.toList());
    }

    /**
     * 解析移动端提交的下单商品。
     */
    private List<OrderItemSource> resolveOrderItemSources(Long userId, List<AppTradeOrderSettlementReqVo.Item> reqItems) {
        if (CollectionUtils.isEmpty(reqItems)) {
            throw new ServiceException("请选择要下单的商品");
        }
        Map<Long, CartVo> cartMap = Collections.emptyMap();
        if (reqItems.stream().anyMatch(item -> item != null && item.getCartId() != null)) {
            cartMap = cartService.queryUserCartList(userId).stream().collect(Collectors.toMap(CartVo::getId, item -> item));
        }

        List<OrderItemSource> sources = new ArrayList<>(reqItems.size());
        for (AppTradeOrderSettlementReqVo.Item reqItem : reqItems) {
            if (reqItem == null) {
                throw new ServiceException("商品不能为空");
            }
            Long cartId = reqItem.getCartId();
            Long skuId = reqItem.getSkuId();
            Integer count = reqItem.getCount();
            if (cartId != null) {
                CartVo cart = cartMap.get(cartId);
                if (cart == null) {
                    throw new ServiceException("购物车商品不存在");
                }
                skuId = cart.getSkuId();
                count = cart.getCount();
            }
            if (skuId == null || count == null) {
                throw new ServiceException("商品 SKU 和数量不能为空");
            }

            ProductSkuVo sku = productSkuService.queryById(skuId);
            if (sku == null) {
                throw new ServiceException("商品 SKU 不存在");
            }
            ProductSpuVo spu = productSpuService.queryById(sku.getSpuId());
            if (spu == null || !ProductSpuStatusEnum.isEnable(spu.getStatus())) {
                throw new ServiceException("商品不存在或已下架");
            }
            if (count <= 0) {
                throw new ServiceException("商品数量必须大于 0");
            }
            if (sku.getStock() == null || sku.getStock() < count) {
                throw new ServiceException("商品库存不足：" + spu.getName());
            }
            sources.add(new OrderItemSource(cartId, spu, sku, count));
        }
        return sources;
    }

    /**
     * 构建订单项。
     */
    private List<TradeOrderItem> buildOrderItems(Long userId, List<CartVo> carts, String operator, LocalDateTime now) {
        List<TradeOrderItem> items = new ArrayList<>();
        for (CartVo cart : carts) {
            ProductSkuVo sku = productSkuService.queryById(cart.getSkuId());
            ProductSpuVo spu = productSpuService.queryById(cart.getSpuId());
            if (sku == null || spu == null) {
                throw new ServiceException("购物车商品不存在");
            }
            if (!ProductSpuStatusEnum.isEnable(spu.getStatus())) {
                throw new ServiceException("商品不存在或已下架");
            }
            Integer count = cart.getCount() == null ? 0 : cart.getCount();
            if (count <= 0) {
                throw new ServiceException("购物车商品数量必须大于 0");
            }
            if (sku.getStock() == null || sku.getStock() < count) {
                throw new ServiceException("商品库存不足：" + spu.getName());
            }
            int price = sku.getPrice() == null ? 0 : sku.getPrice();
            TradeOrderItem item = new TradeOrderItem();
            item.setUserId(userId);
            item.setCartId(cart.getId());
            item.setSpuId(cart.getSpuId());
            item.setSpuName(spu.getName());
            item.setSkuId(cart.getSkuId());
            item.setProperties(convertSkuProperties(sku.getProperties()));
            item.setPicUrl(sku.getPicUrl() != null ? sku.getPicUrl() : spu.getPicUrl());
            item.setCount(count);
            item.setCommentStatus(false);
            item.setPrice(price);
            item.setDiscountPrice(0);
            item.setDeliveryPrice(0);
            item.setAdjustPrice(0);
            item.setCouponPrice(0);
            item.setPointPrice(0);
            item.setUsePoint(0);
            item.setGivePoint(0);
            item.setVipPrice(0);
            item.setPayPrice(price * count);
            item.setAfterSaleStatus(0);
            item.setIsDeleted(false);
            item.setCreateBy(operator);
            item.setCreateTime(now);
            item.setUpdateBy(operator);
            item.setUpdateTime(now);
            items.add(item);
        }
        return items;
    }

    /**
     * 构建移动端订单项。
     */
    private List<TradeOrderItem> buildAppOrderItems(Long userId, List<OrderItemSource> sources, String operator,
                                                    LocalDateTime now, Map<Long, DiscountProductVo> discountProductMap) {
        List<TradeOrderItem> items = new ArrayList<>(sources.size());
        for (OrderItemSource source : sources) {
            int price = defaultZero(source.sku().getPrice());
            int discountPrice = calculateDiscountPrice(price, discountProductMap.get(source.sku().getId())) * source.count();
            TradeOrderItem item = new TradeOrderItem();
            item.setUserId(userId);
            item.setCartId(source.cartId());
            item.setSpuId(source.spu().getId());
            item.setSpuName(source.spu().getName());
            item.setSkuId(source.sku().getId());
            item.setProperties(convertSkuProperties(source.sku().getProperties()));
            item.setPicUrl(source.sku().getPicUrl() != null ? source.sku().getPicUrl() : source.spu().getPicUrl());
            item.setCount(source.count());
            item.setCommentStatus(false);
            item.setPrice(price);
            item.setDiscountPrice(discountPrice);
            item.setDeliveryPrice(0);
            item.setAdjustPrice(0);
            item.setCouponPrice(0);
            item.setPointPrice(0);
            item.setUsePoint(0);
            item.setGivePoint(0);
            item.setVipPrice(0);
            item.setPayPrice(Math.max(0, price * source.count() - discountPrice));
            item.setAfterSaleStatus(0);
            item.setIsDeleted(false);
            item.setCreateBy(operator);
            item.setCreateTime(now);
            item.setUpdateBy(operator);
            item.setUpdateTime(now);
            items.add(item);
        }
        return items;
    }

    /**
     * 转换 SKU 规格属性到订单项快照。
     */
    private List<TradeOrderItem.Property> convertSkuProperties(List<ProductSku.Property> properties) {
        if (properties == null) {
            return List.of();
        }
        return properties.stream().map(property -> {
            TradeOrderItem.Property itemProperty = new TradeOrderItem.Property();
            itemProperty.setPropertyId(property.getPropertyId());
            itemProperty.setPropertyName(property.getPropertyName());
            itemProperty.setValueId(property.getValueId());
            itemProperty.setValueName(property.getValueName());
            return itemProperty;
        }).collect(Collectors.toList());
    }

    /**
     * 转换移动端商品规格属性信息。
     */
    private List<AppProductPropertyValueDetailRespVo> convertAppProductProperties(List<ProductSku.Property> properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return Collections.emptyList();
        }
        List<AppProductPropertyValueDetailRespVo> respList = new ArrayList<>(properties.size());
        for (ProductSku.Property property : properties) {
            AppProductPropertyValueDetailRespVo respVo = new AppProductPropertyValueDetailRespVo();
            respVo.setPropertyId(property.getPropertyId());
            respVo.setPropertyName(property.getPropertyName());
            respVo.setValueId(property.getValueId());
            respVo.setValueName(property.getValueName());
            respList.add(respVo);
        }
        return respList;
    }

    /**
     * 创建支付订单。
     */
    private Long createPayOrder(Long userId, TradeOrder order) {
        PayOrderCreateReqDto createReqDto = new PayOrderCreateReqDto();
        createReqDto.setAppId(1L);
        createReqDto.setUserIp("0.0.0.0");
        createReqDto.setUserId(userId);
        createReqDto.setUserType(2);
        createReqDto.setMerchantOrderId(String.valueOf(order.getId()));
        createReqDto.setSubject("商城订单 " + order.getNo());
        createReqDto.setBody("商城订单 " + order.getNo());
        createReqDto.setPrice(order.getPayPrice());
        createReqDto.setExpireTime(LocalDateTime.now().plus(tradeOrderProperties.getPayExpireTime()));
        return payOrderApi.createPayOrder(createReqDto).getCheckedData();
    }

    /**
     * 校验支付订单已经支付且和商城订单匹配。
     */
    private PayOrderRespVo validatePayOrderPaid(TradeOrder order, Long payOrderId) {
        if (payOrderId == null) {
            throw new ServiceException("支付订单编号不能为空");
        }
        PayOrderRespVo payOrder = payOrderApi.getOrder(payOrderId).getCheckedData();
        if (payOrder == null) {
            throw new ServiceException("支付订单不存在");
        }
        if (!Objects.equals(payOrder.getStatus(), 10)) {
            throw new ServiceException("支付订单未支付成功");
        }
        if (!Objects.equals(payOrder.getPrice(), order.getPayPrice())) {
            throw new ServiceException("支付金额与商城订单金额不一致");
        }
        if (!Objects.equals(payOrder.getMerchantOrderId(), String.valueOf(order.getId()))) {
            throw new ServiceException("支付订单与商城订单不匹配");
        }
        if (order.getPayOrderId() != null && !Objects.equals(order.getPayOrderId(), payOrderId)) {
            throw new ServiceException("商城订单绑定的支付单不匹配");
        }
        return payOrder;
    }

    /**
     * 判断支付单是否已经支付成功。
     */
    private boolean isPayOrderSuccess(TradeOrder order) {
        if (order.getPayOrderId() == null) {
            return false;
        }
        PayOrderRespVo payOrder = payOrderApi.getOrder(order.getPayOrderId()).getCheckedData();
        return payOrder != null && Objects.equals(payOrder.getStatus(), 10);
    }

    /**
     * 订单取消后归还订单项库存。
     */
    private void returnOrderStock(Long orderId) {
        List<TradeOrderItemVo> items = tradeOrderItemService.queryListByOrderId(orderId);
        for (TradeOrderItemVo item : items) {
            productSkuService.increaseStock(item.getSkuId(), item.getCount());
        }
    }

    /**
     * 订单取消后退回已使用优惠券。
     */
    private void returnOrderCoupon(TradeOrder order) {
        if (order.getCouponId() != null && defaultZero(order.getCouponPrice()) > 0) {
            couponService.returnUsedCoupon(order.getCouponId());
        }
    }

    /**
     * 生成交易订单号。
     */
    private String generateOrderNo() {
        return "M" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    /**
     * 查询用户指定状态订单数量。
     */
    private Long countByUserAndStatus(Long userId, Integer status, Boolean commentStatus) {
        LambdaQueryWrapper<TradeOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(TradeOrder::getIsDeleted, false);
        lqw.eq(TradeOrder::getUserId, userId);
        lqw.eq(status != null, TradeOrder::getStatus, status);
        lqw.eq(commentStatus != null, TradeOrder::getCommentStatus, commentStatus);
        return baseMapper.selectCount(lqw);
    }

    /**
     * 校验当前用户订单。
     */
    private TradeOrder validateUserOrder(Long userId, Long id) {
        TradeOrder order = baseMapper.selectById(id);
        if (order == null || Boolean.TRUE.equals(order.getIsDeleted()) || !Objects.equals(order.getUserId(), userId)) {
            throw new ServiceException("订单不存在");
        }
        return order;
    }

    /**
     * 校验订单存在。
     */
    private TradeOrder validateOrderExists(Long id) {
        if (id == null) {
            throw new ServiceException("订单编号不能为空");
        }
        TradeOrder order = baseMapper.selectById(id);
        if (order == null || Boolean.TRUE.equals(order.getIsDeleted())) {
            throw new ServiceException("订单不存在");
        }
        return order;
    }

    /**
     * 构建订单物流轨迹。
     */
    private List<AppOrderExpressTrackRespDto> buildOrderExpressTrackList(TradeOrder order) {
        List<AppOrderExpressTrackRespDto> tracks = new ArrayList<>();
        if (order.getCreateTime() != null) {
            tracks.add(buildExpressTrack(order.getCreateTime(), "订单已创建"));
        }
        if (order.getPayTime() != null) {
            tracks.add(buildExpressTrack(order.getPayTime(), "订单已支付"));
        }
        if (order.getDeliveryTime() != null) {
            tracks.add(buildExpressTrack(order.getDeliveryTime(),
                    "商家已发货" + (StringUtils.isNotBlank(order.getLogisticsNo()) ? "，物流单号：" + order.getLogisticsNo() : "")));
        }
        if (order.getReceiveTime() != null) {
            tracks.add(buildExpressTrack(order.getReceiveTime(), "订单已确认收货"));
        }
        if (order.getFinishTime() != null && order.getReceiveTime() == null) {
            tracks.add(buildExpressTrack(order.getFinishTime(), "订单已完成"));
        }
        if (order.getCancelTime() != null) {
            tracks.add(buildExpressTrack(order.getCancelTime(), "订单已取消"));
        }
        tracks.sort(Comparator.comparing(AppOrderExpressTrackRespDto::getTime));
        return tracks;
    }

    /**
     * 构建单条订单轨迹。
     */
    private AppOrderExpressTrackRespDto buildExpressTrack(LocalDateTime time, String content) {
        AppOrderExpressTrackRespDto track = new AppOrderExpressTrackRespDto();
        track.setTime(time);
        track.setContent(content);
        return track;
    }

    /**
     * 后台核销自提订单。
     */
    private Boolean pickUpOrder(Long userId, TradeOrder order) {
        if (!Objects.equals(order.getDeliveryType(), DELIVERY_TYPE_PICK_UP)) {
            throw new ServiceException("只有到店自提订单可以核销");
        }
        if (!Objects.equals(order.getStatus(), ORDER_STATUS_UNDELIVERED)) {
            throw new ServiceException("只有待核销订单可以核销");
        }
        TradeOrder entity = new TradeOrder();
        entity.setId(order.getId());
        entity.setStatus(ORDER_STATUS_COMPLETED);
        entity.setReceiveTime(LocalDateTime.now());
        entity.setFinishTime(LocalDateTime.now());
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(LocalDateTime.now());
        return baseMapper.updateById(entity) > 0;
    }

    /**
     * 按订单项支付金额分摊调价金额。
     */
    private List<Integer> dividePrice(List<TradeOrderItemVo> items, Integer adjustPrice) {
        if (CollectionUtils.isEmpty(items)) {
            return List.of();
        }
        int totalPayPrice = items.stream().map(TradeOrderItemVo::getPayPrice)
                .filter(Objects::nonNull).reduce(0, Integer::sum);
        List<Integer> result = new ArrayList<>(items.size());
        int assigned = 0;
        for (int i = 0; i < items.size(); i++) {
            int itemAdjustPrice;
            if (i == items.size() - 1 || totalPayPrice == 0) {
                itemAdjustPrice = adjustPrice - assigned;
            } else {
                itemAdjustPrice = adjustPrice * defaultZero(items.get(i).getPayPrice()) / totalPayPrice;
                assigned += itemAdjustPrice;
            }
            result.add(itemAdjustPrice);
        }
        return result;
    }

    /**
     * 补充订单项。
     */
    private void fillOrderItems(TradeOrderVo order) {
        if (order == null) {
            return;
        }
        order.setItems(tradeOrderItemService.queryListByOrderId(order.getId()));
    }

    /**
     * 批量补充订单项。
     */
    private void fillOrderItems(List<TradeOrderVo> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        List<Long> orderIds = CollectionUtils.convertList(orders, TradeOrderVo::getId);
        Map<Long, List<TradeOrderItemVo>> itemMap = tradeOrderItemService.queryListByOrderIds(orderIds)
                .stream().collect(Collectors.groupingBy(TradeOrderItemVo::getOrderId));
        for (TradeOrderVo order : orders) {
            order.setItems(itemMap.getOrDefault(order.getId(), List.of()));
        }
    }

    /**
     * 构建移动端订单分页项响应对象。
     */
    private AppTradeOrderPageItemRespVo buildAppPageItemRespVo(TradeOrderVo order) {
        AppTradeOrderPageItemRespVo respVo = new AppTradeOrderPageItemRespVo();
        respVo.setId(order.getId());
        respVo.setNo(order.getNo());
        respVo.setType(order.getType());
        respVo.setStatus(order.getStatus());
        respVo.setProductCount(order.getProductCount());
        respVo.setCommentStatus(order.getCommentStatus());
        respVo.setCreateTime(order.getCreateTime());
        respVo.setPayOrderId(order.getPayOrderId());
        respVo.setPayPrice(order.getPayPrice());
        respVo.setDeliveryType(order.getDeliveryType());
        respVo.setCombinationRecordId(order.getCombinationRecordId());
        respVo.setItems(buildAppOrderItemRespVoList(order.getItems()));
        return respVo;
    }

    /**
     * 构建移动端订单详情响应对象。
     */
    private AppTradeOrderDetailRespVo buildAppDetailRespVo(TradeOrderVo order) {
        if (order == null) {
            return null;
        }
        AppTradeOrderDetailRespVo respVo = new AppTradeOrderDetailRespVo();
        respVo.setId(order.getId());
        respVo.setNo(order.getNo());
        respVo.setType(order.getType());
        respVo.setCreateTime(order.getCreateTime());
        respVo.setUserRemark(order.getUserRemark());
        respVo.setStatus(order.getStatus());
        respVo.setProductCount(order.getProductCount());
        respVo.setFinishTime(order.getFinishTime());
        respVo.setCancelTime(order.getCancelTime());
        respVo.setCommentStatus(order.getCommentStatus());
        respVo.setPayStatus(order.getPayStatus());
        respVo.setPayOrderId(order.getPayOrderId());
        respVo.setPayTime(order.getPayTime());
        respVo.setPayExpireTime(order.getCreateTime() == null ? null
                : order.getCreateTime().plus(tradeOrderProperties.getPayExpireTime()));
        respVo.setPayChannelCode(order.getPayChannelCode());
        respVo.setPayChannelName(order.getPayChannelCode());
        respVo.setTotalPrice(order.getTotalPrice());
        respVo.setDiscountPrice(order.getDiscountPrice());
        respVo.setDeliveryPrice(order.getDeliveryPrice());
        respVo.setAdjustPrice(order.getAdjustPrice());
        respVo.setPayPrice(order.getPayPrice());
        respVo.setDeliveryType(order.getDeliveryType());
        respVo.setLogisticsId(order.getLogisticsId());
        respVo.setLogisticsNo(order.getLogisticsNo());
        respVo.setDeliveryTime(order.getDeliveryTime());
        respVo.setReceiveTime(order.getReceiveTime());
        respVo.setReceiverName(order.getReceiverName());
        respVo.setReceiverMobile(order.getReceiverMobile());
        respVo.setReceiverAreaId(order.getReceiverAreaId());
        respVo.setReceiverDetailAddress(order.getReceiverDetailAddress());
        respVo.setPickUpStoreId(order.getPickUpStoreId());
        respVo.setPickUpVerifyCode(order.getPickUpVerifyCode());
        respVo.setRefundStatus(order.getRefundStatus());
        respVo.setRefundPrice(order.getRefundPrice());
        respVo.setCouponId(order.getCouponId());
        respVo.setCouponPrice(order.getCouponPrice());
        respVo.setPointPrice(order.getPointPrice());
        respVo.setVipPrice(order.getVipPrice());
        respVo.setCombinationRecordId(order.getCombinationRecordId());
        respVo.setItems(buildAppOrderItemRespVoList(order.getItems()));
        return respVo;
    }

    /**
     * 构建移动端订单项响应对象列表。
     */
    private List<AppTradeOrderItemRespVo> buildAppOrderItemRespVoList(List<TradeOrderItemVo> items) {
        if (CollectionUtils.isEmpty(items)) {
            return List.of();
        }
        return items.stream().map(this::buildAppOrderItemRespVo).toList();
    }

    /**
     * 构建移动端订单项响应对象。
     */
    private AppTradeOrderItemRespVo buildAppOrderItemRespVo(TradeOrderItemVo item) {
        AppTradeOrderItemRespVo respVo = new AppTradeOrderItemRespVo();
        respVo.setId(item.getId());
        respVo.setOrderId(item.getOrderId());
        respVo.setSpuId(item.getSpuId());
        respVo.setSpuName(item.getSpuName());
        respVo.setSkuId(item.getSkuId());
        respVo.setProperties(convertOrderItemProperties(item.getProperties()));
        respVo.setPicUrl(item.getPicUrl());
        respVo.setCount(item.getCount());
        respVo.setCommentStatus(item.getCommentStatus());
        respVo.setPrice(item.getPrice());
        respVo.setPayPrice(item.getPayPrice());
        respVo.setAfterSaleId(item.getAfterSaleId());
        respVo.setAfterSaleStatus(item.getAfterSaleStatus());
        return respVo;
    }

    /**
     * 转换订单项规格属性信息。
     */
    private List<AppProductPropertyValueDetailRespVo> convertOrderItemProperties(List<TradeOrderItem.Property> properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return List.of();
        }
        List<AppProductPropertyValueDetailRespVo> respList = new ArrayList<>(properties.size());
        for (TradeOrderItem.Property property : properties) {
            AppProductPropertyValueDetailRespVo respVo = new AppProductPropertyValueDetailRespVo();
            respVo.setPropertyId(property.getPropertyId());
            respVo.setPropertyName(property.getPropertyName());
            respVo.setValueId(property.getValueId());
            respVo.setValueName(property.getValueName());
            respList.add(respVo);
        }
        return respList;
    }

    /**
     * 空金额按 0 处理。
     */
    private Integer defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    /**
     * 查询当前有效限时折扣，并按 SKU 编号聚合。
     */
    private Map<Long, DiscountProductVo> getMatchDiscountProductMap(Collection<Long> skuIds) {
        if (CollectionUtils.isEmpty(skuIds)) {
            return Collections.emptyMap();
        }
        return discountProductService.getMatchDiscountProductListBySkuIds(skuIds)
                .stream().collect(Collectors.toMap(DiscountProductVo::getSkuId, item -> item, (first, second) -> first));
    }

    /**
     * 计算限时折扣减免金额，保持和 yudao 的折扣语义一致。
     */
    private Integer calculateDiscountPrice(Integer price, DiscountProductVo discountProduct) {
        if (discountProduct == null || price == null || price <= 0) {
            return 0;
        }
        Integer newPrice;
        if (Objects.equals(discountProduct.getDiscountType(), DISCOUNT_TYPE_PRICE)) {
            newPrice = price - defaultZero(discountProduct.getDiscountPrice());
        } else if (Objects.equals(discountProduct.getDiscountType(), DISCOUNT_TYPE_PERCENT)) {
            newPrice = BigDecimal.valueOf(price)
                    .multiply(BigDecimal.valueOf(defaultZero(discountProduct.getDiscountPercent())))
                    .divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP)
                    .intValue();
        } else {
            return 0;
        }
        return Math.max(0, price - Math.max(0, newPrice));
    }

    /**
     * 构建订单结算可用优惠券列表。
     */
    private List<AppTradeOrderSettlementRespVo.Coupon> buildSettlementCoupons(Long userId, int couponBasePrice,
                                                                               List<Long> spuIds, List<Long> categoryIds) {
        CouponBo couponBo = new CouponBo();
        couponBo.setUserId(userId);
        couponBo.setStatus(COUPON_STATUS_UNUSED);
        return couponService.queryList(couponBo).stream()
                .map(coupon -> buildSettlementCoupon(coupon, couponBasePrice, spuIds, categoryIds))
                .toList();
    }

    /**
     * 构建单张结算优惠券。
     */
    private AppTradeOrderSettlementRespVo.Coupon buildSettlementCoupon(CouponVo coupon, int couponBasePrice,
                                                                       List<Long> spuIds, List<Long> categoryIds) {
        AppTradeOrderSettlementRespVo.Coupon respVo = new AppTradeOrderSettlementRespVo.Coupon();
        respVo.setId(coupon.getId());
        respVo.setName(coupon.getName());
        respVo.setUsePrice(coupon.getUsePrice());
        respVo.setValidStartTime(coupon.getValidStartTime());
        respVo.setValidEndTime(coupon.getValidEndTime());
        respVo.setDiscountType(coupon.getDiscountType());
        respVo.setDiscountPercent(coupon.getDiscountPercent());
        respVo.setDiscountPrice(coupon.getDiscountPrice());
        respVo.setDiscountLimitPrice(coupon.getDiscountLimitPrice());
        String mismatchReason = getCouponMismatchReason(coupon, couponBasePrice, spuIds, categoryIds);
        respVo.setMatch(mismatchReason == null);
        respVo.setMismatchReason(mismatchReason);
        return respVo;
    }

    /**
     * 获取优惠券不可用原因。
     */
    private String getCouponMismatchReason(CouponVo coupon, int couponBasePrice, List<Long> spuIds, List<Long> categoryIds) {
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getValidStartTime() != null && now.isBefore(coupon.getValidStartTime())) {
            return "优惠券未到可用时间";
        }
        if (coupon.getValidEndTime() != null && now.isAfter(coupon.getValidEndTime())) {
            return "优惠券已过期";
        }
        if (defaultZero(coupon.getUsePrice()) > couponBasePrice) {
            return "订单金额未满足优惠券使用门槛";
        }
        if (Objects.equals(coupon.getProductScope(), PRODUCT_SCOPE_SPU)
                && !hasIntersection(coupon.getProductScopeValues(), spuIds)) {
            return "当前商品不在优惠券适用范围";
        }
        if (Objects.equals(coupon.getProductScope(), PRODUCT_SCOPE_CATEGORY)
                && !hasIntersection(coupon.getProductScopeValues(), categoryIds)) {
            return "当前分类不在优惠券适用范围";
        }
        return null;
    }

    /**
     * 计算创建订单时选中优惠券优惠金额。
     */
    private int calculateAppOrderCouponPrice(Long userId, Long couponId, int couponBasePrice, List<OrderItemSource> sources) {
        if (couponId == null) {
            return 0;
        }
        List<Long> spuIds = sources.stream().map(source -> source.spu().getId()).toList();
        List<Long> categoryIds = sources.stream().map(source -> source.spu().getCategoryId()).toList();
        List<AppTradeOrderSettlementRespVo.Coupon> coupons = buildSettlementCoupons(userId, couponBasePrice, spuIds, categoryIds);
        return calculateSelectedCouponPrice(couponId, coupons, couponBasePrice, false);
    }

    /**
     * 计算选中优惠券金额。
     */
    private int calculateSelectedCouponPrice(Long couponId, List<AppTradeOrderSettlementRespVo.Coupon> coupons,
                                             int couponBasePrice, boolean ignoreMismatch) {
        if (couponId == null) {
            return 0;
        }
        AppTradeOrderSettlementRespVo.Coupon coupon = coupons.stream()
                .filter(item -> Objects.equals(item.getId(), couponId))
                .findFirst()
                .orElseThrow(() -> new ServiceException("优惠券不存在或不属于当前用户"));
        if (!Boolean.TRUE.equals(coupon.getMatch())) {
            if (ignoreMismatch) {
                return 0;
            }
            throw new ServiceException(coupon.getMismatchReason());
        }
        return calculateCouponDiscountPrice(coupon, couponBasePrice);
    }

    /**
     * 按优惠券规则计算优惠金额。
     */
    private int calculateCouponDiscountPrice(AppTradeOrderSettlementRespVo.Coupon coupon, int couponBasePrice) {
        if (Objects.equals(coupon.getDiscountType(), DISCOUNT_TYPE_PRICE)) {
            return Math.min(couponBasePrice, defaultZero(coupon.getDiscountPrice()));
        }
        if (Objects.equals(coupon.getDiscountType(), DISCOUNT_TYPE_PERCENT)) {
            int newPrice = BigDecimal.valueOf(couponBasePrice)
                    .multiply(BigDecimal.valueOf(defaultZero(coupon.getDiscountPercent())))
                    .divide(BigDecimal.valueOf(10000), 0, RoundingMode.HALF_UP)
                    .intValue();
            int couponPrice = Math.max(0, couponBasePrice - newPrice);
            return defaultZero(coupon.getDiscountLimitPrice()) > 0
                    ? Math.min(couponPrice, coupon.getDiscountLimitPrice()) : couponPrice;
        }
        return 0;
    }

    /**
     * 将订单优惠券金额分摊到订单项。
     */
    private void applyCouponPriceToItems(List<TradeOrderItem> items, int couponPrice) {
        if (couponPrice <= 0 || CollectionUtils.isEmpty(items)) {
            return;
        }
        List<TradeOrderItemVo> itemVos = items.stream().map(item -> {
            TradeOrderItemVo itemVo = new TradeOrderItemVo();
            itemVo.setPayPrice(item.getPayPrice());
            return itemVo;
        }).toList();
        List<Integer> couponPrices = dividePrice(itemVos, -couponPrice);
        for (int i = 0; i < items.size(); i++) {
            int itemCouponPrice = Math.abs(couponPrices.get(i));
            TradeOrderItem item = items.get(i);
            item.setCouponPrice(itemCouponPrice);
            item.setPayPrice(Math.max(0, defaultZero(item.getPayPrice()) - itemCouponPrice));
        }
    }

    /**
     * 判断两个编号列表是否存在交集。
     */
    private boolean hasIntersection(Collection<Long> first, Collection<Long> second) {
        if (CollectionUtils.isEmpty(first) || CollectionUtils.isEmpty(second)) {
            return false;
        }
        for (Long value : first) {
            if (second.contains(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 构建商品命中的满减送活动信息。
     */
    private AppTradeProductSettlementRespVo.RewardActivity buildRewardActivityRespVo(
            Long spuId, List<RewardActivityVo> rewardActivities) {
        if (CollectionUtils.isEmpty(rewardActivities)) {
            return null;
        }
        for (RewardActivityVo rewardActivity : rewardActivities) {
            if (rewardActivity.getSpuIds() != null && rewardActivity.getSpuIds().contains(spuId)) {
                AppTradeProductSettlementRespVo.RewardActivity respVo =
                        new AppTradeProductSettlementRespVo.RewardActivity();
                respVo.setId(rewardActivity.getId());
                respVo.setConditionType(rewardActivity.getConditionType());
                respVo.setRules(convertRewardActivityRules(rewardActivity.getRules()));
                return respVo;
            }
        }
        return null;
    }

    /**
     * 转换满减送活动规则。
     */
    private List<AppTradeProductSettlementRespVo.RewardActivityRule> convertRewardActivityRules(
            List<RewardActivity.Rule> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return List.of();
        }
        return rules.stream().map(rule -> {
            AppTradeProductSettlementRespVo.RewardActivityRule respVo =
                    new AppTradeProductSettlementRespVo.RewardActivityRule();
            respVo.setLimit(rule.getLimit());
            respVo.setDiscountPrice(rule.getDiscountPrice());
            respVo.setFreeDelivery(rule.getFreeDelivery());
            respVo.setPoint(rule.getPoint());
            respVo.setGiveCouponTemplateCounts(rule.getGiveCouponTemplateCounts());
            return respVo;
        }).toList();
    }

    /**
     * 移动端下单商品来源。
     */
    private record OrderItemSource(Long cartId, ProductSpuVo spu, ProductSkuVo sku, Integer count) {
    }

}

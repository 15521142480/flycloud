package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.StatusEnum;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.mall.api.product.domain.ProductSku;
import com.fly.mall.api.product.domain.vo.AppProductPropertyValueDetailRespVo;
import com.fly.mall.api.product.domain.vo.ProductSkuVo;
import com.fly.mall.api.product.domain.vo.ProductSpuVo;
import com.fly.mall.api.trade.domain.TradeOrder;
import com.fly.mall.api.trade.domain.TradeOrderItem;
import com.fly.mall.api.trade.domain.bo.TradeOrderBo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderCreateRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementReqVo;
import com.fly.mall.api.trade.domain.vo.AppTradeOrderSettlementRespVo;
import com.fly.mall.api.trade.domain.vo.AppTradeProductSettlementRespVo;
import com.fly.mall.api.trade.domain.vo.CartVo;
import com.fly.mall.api.trade.domain.vo.TradeOrderItemVo;
import com.fly.mall.api.trade.domain.vo.TradeOrderVo;
import com.fly.mall.product.service.IProductSkuService;
import com.fly.mall.product.service.IProductSpuService;
import com.fly.mall.trade.mapper.TradeOrderItemMapper;
import com.fly.mall.trade.mapper.TradeOrderMapper;
import com.fly.mall.trade.service.ICartService;
import com.fly.mall.trade.service.ITradeOrderItemService;
import com.fly.mall.trade.service.ITradeOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
 * @date 2026-06-29
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

    private final TradeOrderMapper baseMapper;
    private final TradeOrderItemMapper tradeOrderItemMapper;
    private final ITradeOrderItemService tradeOrderItemService;
    private final ICartService cartService;
    private final IProductSkuService productSkuService;
    private final IProductSpuService productSpuService;

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
        List<AppTradeOrderSettlementRespVo.Item> items = new ArrayList<>(sources.size());
        int totalPrice = 0;
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
        }

        AppTradeOrderSettlementRespVo respVo = new AppTradeOrderSettlementRespVo();
        respVo.setType(ORDER_TYPE_NORMAL);
        respVo.setItems(items);
        respVo.setCoupons(Collections.emptyList());
        respVo.setPrice(new AppTradeOrderSettlementRespVo.Price(totalPrice, 0, 0, 0, 0, 0, totalPrice));
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
        Map<Long, List<ProductSkuVo>> skuMap = productSkuService.queryListBySpuIds(spuIds)
                .stream().collect(Collectors.groupingBy(ProductSkuVo::getSpuId));
        List<AppTradeProductSettlementRespVo> list = new ArrayList<>(spuIds.size());
        for (Long spuId : spuIds) {
            AppTradeProductSettlementRespVo respVo = new AppTradeProductSettlementRespVo();
            respVo.setSpuId(spuId);
            List<AppTradeProductSettlementRespVo.Sku> skus = skuMap.getOrDefault(spuId, Collections.emptyList())
                    .stream().map(sku -> {
                        AppTradeProductSettlementRespVo.Sku skuRespVo = new AppTradeProductSettlementRespVo.Sku();
                        skuRespVo.setId(sku.getId());
                        skuRespVo.setPromotionPrice(defaultZero(sku.getPrice()));
                        return skuRespVo;
                    }).collect(Collectors.toList());
            respVo.setSkus(skus);
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
        LocalDateTime now = LocalDateTime.now();
        String operator = String.valueOf(userId);
        List<TradeOrderItem> items = buildAppOrderItems(userId, sources, operator, now);
        int totalPrice = items.stream().map(TradeOrderItem::getPayPrice).filter(Objects::nonNull).reduce(0, Integer::sum);
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
        order.setDiscountPrice(0);
        order.setDeliveryPrice(0);
        order.setAdjustPrice(0);
        order.setCouponPrice(0);
        order.setPointPrice(0);
        order.setVipPrice(0);
        order.setPayPrice(totalPrice);
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

        AppTradeOrderCreateRespVo respVo = new AppTradeOrderCreateRespVo();
        respVo.setId(order.getId());
        respVo.setPayOrderId(order.getPayOrderId());
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
     * 当前用户取消订单。
     */
    @Override
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
        return baseMapper.updateById(entity) > 0;
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
            if (spu == null || !StatusEnum.isEnable(spu.getStatus())) {
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
    private List<TradeOrderItem> buildAppOrderItems(Long userId, List<OrderItemSource> sources, String operator, LocalDateTime now) {
        List<TradeOrderItem> items = new ArrayList<>(sources.size());
        for (OrderItemSource source : sources) {
            int price = defaultZero(source.sku().getPrice());
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
            item.setDiscountPrice(0);
            item.setDeliveryPrice(0);
            item.setAdjustPrice(0);
            item.setCouponPrice(0);
            item.setPointPrice(0);
            item.setUsePoint(0);
            item.setGivePoint(0);
            item.setVipPrice(0);
            item.setPayPrice(price * source.count());
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
     * 空金额按 0 处理。
     */
    private Integer defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    /**
     * 移动端下单商品来源。
     */
    private record OrderItemSource(Long cartId, ProductSpuVo spu, ProductSkuVo sku, Integer count) {
    }

}

package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.file.FileUrlFieldConverter;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.trade.domain.AfterSale;
import com.fly.mall.api.trade.domain.TradeOrderItem;
import com.fly.mall.api.trade.domain.bo.AfterSaleBo;
import com.fly.mall.api.trade.domain.bo.TradeOrderItemBo;
import com.fly.mall.api.trade.domain.vo.AfterSaleVo;
import com.fly.mall.api.trade.domain.vo.TradeOrderItemVo;
import com.fly.mall.api.trade.domain.vo.TradeOrderVo;
import com.fly.mall.trade.mapper.AfterSaleMapper;
import com.fly.mall.trade.service.IAfterSaleService;
import com.fly.mall.trade.service.ITradeOrderItemService;
import com.fly.mall.trade.service.ITradeOrderService;
import com.fly.mall.trade.mapper.TradeOrderItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 售后单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class AfterSaleServiceImpl extends BaseServiceImpl<AfterSaleMapper, AfterSale> implements IAfterSaleService {

    private static final int STATUS_APPLY = 10;
    private static final int STATUS_SELLER_AGREE = 20;
    private static final int STATUS_BUYER_DELIVERY = 30;
    private static final int STATUS_WAIT_REFUND = 40;
    private static final int STATUS_COMPLETE = 50;
    private static final int STATUS_SELLER_DISAGREE = 61;
    private static final int STATUS_BUYER_CANCEL = 62;
    private static final int STATUS_SELLER_REFUSE = 63;

    private static final int WAY_REFUND = 10;
    private static final int WAY_RETURN_AND_REFUND = 20;
    private static final int ORDER_STATUS_UNPAID = 0;
    private static final int ORDER_STATUS_DELIVERED = 20;
    private static final int ORDER_STATUS_COMPLETED = 30;
    private static final int ORDER_STATUS_CANCELED = 40;
    private static final int AFTER_SALE_TYPE_IN_SALE = 10;
    private static final int AFTER_SALE_TYPE_AFTER_SALE = 20;
    private static final int ORDER_ITEM_AFTER_SALE_NONE = 0;
    private static final int ORDER_ITEM_AFTER_SALE_APPLYING = 10;
    private static final int ORDER_ITEM_AFTER_SALE_SUCCESS = 20;

    private final AfterSaleMapper baseMapper;
    private final ITradeOrderService tradeOrderService;
    private final ITradeOrderItemService tradeOrderItemService;
    private final TradeOrderItemMapper tradeOrderItemMapper;
    private final FileUrlFieldConverter fileUrlFieldConverter;

    /**
     * 查询售后单详情。
     */
    @Override
    public AfterSaleVo queryById(Long id) {
        return fileUrlFieldConverter.buildUrl(baseMapper.selectVoById(id), "applyPicUrls", "picUrl");
    }

    /**
     * 查询当前用户售后单详情。
     */
    @Override
    public AfterSaleVo queryByUserAndId(Long userId, Long id) {
        AfterSaleVo afterSale = queryById(id);
        if (afterSale == null || !Objects.equals(afterSale.getUserId(), userId)) {
            return null;
        }
        return afterSale;
    }

    /**
     * 分页查询售后单。
     */
    @Override
    public PageVo<AfterSaleVo> queryPageList(AfterSaleBo bo, PageBo pageBo) {
        LambdaQueryWrapper<AfterSale> lqw = buildQueryWrapper(bo);
        Page<AfterSaleVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return fileUrlFieldConverter.buildUrlPage(this.build(result), "applyPicUrls", "picUrl");
    }

    /**
     * 查询售后单列表。
     */
    @Override
    public List<AfterSaleVo> queryList(AfterSaleBo bo) {
        LambdaQueryWrapper<AfterSale> lqw = buildQueryWrapper(bo);
        return fileUrlFieldConverter.buildUrlList(baseMapper.selectVoList(lqw), "applyPicUrls", "picUrl");
    }

    /**
     * 用户创建售后申请。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAfterSale(Long userId, AfterSaleBo bo) {
        TradeOrderItemVo orderItem = validateOrderItemApplicable(userId, bo);
        TradeOrderVo order = tradeOrderService.queryByUserAndId(userId, orderItem.getOrderId());
        LocalDateTime now = LocalDateTime.now();

        fileUrlFieldConverter.toPath(bo, "applyPicUrls", "picUrl");
        AfterSale entity = BeanUtil.toBean(bo, AfterSale.class);
        entity.setId(null);
        entity.setNo(generateAfterSaleNo());
        entity.setStatus(STATUS_APPLY);
        entity.setUserId(userId);
        entity.setType(Objects.equals(order.getStatus(), ORDER_STATUS_COMPLETED) ? AFTER_SALE_TYPE_AFTER_SALE : AFTER_SALE_TYPE_IN_SALE);
        entity.setOrderId(order.getId());
        entity.setOrderNo(order.getNo());
        entity.setOrderItemId(orderItem.getId());
        entity.setSpuId(orderItem.getSpuId());
        entity.setSpuName(orderItem.getSpuName());
        entity.setSkuId(orderItem.getSkuId());
        entity.setProperties(bo.getProperties() == null ? java.util.List.copyOf(orderItem.getProperties()) : bo.getProperties());
        entity.setPicUrl(orderItem.getPicUrl());
        fileUrlFieldConverter.toPath(entity, "applyPicUrls", "picUrl");
        entity.setCount(bo.getCount() == null ? orderItem.getCount() : bo.getCount());
        entity.setRefundPrice(bo.getRefundPrice() == null ? orderItem.getPayPrice() : bo.getRefundPrice());
        entity.setIsDeleted(false);
        entity.setCreateBy(String.valueOf(userId));
        entity.setCreateTime(now);
        entity.setUpdateBy(String.valueOf(userId));
        entity.setUpdateTime(now);
        baseMapper.insert(entity);
        updateOrderItemAfterSale(orderItem.getId(), entity.getId(), ORDER_ITEM_AFTER_SALE_APPLYING);
        return entity.getId();
    }

    /**
     * 用户取消售后申请。
     */
    @Override
    public Boolean cancelAfterSale(Long userId, Long id) {
        AfterSale afterSale = validateUserAfterSale(userId, id);
        if (!List.of(STATUS_APPLY, STATUS_SELLER_AGREE, STATUS_BUYER_DELIVERY).contains(afterSale.getStatus())) {
            throw new ServiceException("当前售后状态不允许取消");
        }
        updateAfterSaleStatus(afterSale, STATUS_BUYER_CANCEL, null);
        updateOrderItemAfterSale(afterSale.getOrderItemId(), null, ORDER_ITEM_AFTER_SALE_NONE);
        return true;
    }

    /**
     * 用户填写退货物流。
     */
    @Override
    public Boolean deliveryAfterSale(Long userId, AfterSaleBo bo) {
        AfterSale afterSale = validateUserAfterSale(userId, bo.getId());
        if (!Objects.equals(afterSale.getStatus(), STATUS_SELLER_AGREE)) {
            throw new ServiceException("只有商家同意后才可以填写退货物流");
        }
        if (bo.getLogisticsId() == null || StringUtils.isBlank(bo.getLogisticsNo())) {
            throw new ServiceException("退货物流公司和物流单号不能为空");
        }
        AfterSale update = new AfterSale();
        update.setStatus(STATUS_BUYER_DELIVERY);
        update.setLogisticsId(bo.getLogisticsId());
        update.setLogisticsNo(bo.getLogisticsNo());
        update.setDeliveryTime(LocalDateTime.now());
        updateAfterSaleStatus(afterSale, STATUS_BUYER_DELIVERY, update);
        return true;
    }

    /**
     * 后台同意售后申请。
     */
    @Override
    public Boolean agreeAfterSale(Long userId, Long id) {
        AfterSale afterSale = validateAfterSaleAuditable(id);
        int nextStatus = Objects.equals(afterSale.getWay(), WAY_REFUND) ? STATUS_WAIT_REFUND : STATUS_SELLER_AGREE;
        AfterSale update = new AfterSale();
        update.setStatus(nextStatus);
        update.setAuditUserId(userId);
        update.setAuditTime(LocalDateTime.now());
        updateAfterSaleStatus(afterSale, nextStatus, update);
        return true;
    }

    /**
     * 后台拒绝售后申请。
     */
    @Override
    public Boolean disagreeAfterSale(Long userId, AfterSaleBo bo) {
        AfterSale afterSale = validateAfterSaleAuditable(bo.getId());
        AfterSale update = new AfterSale();
        update.setStatus(STATUS_SELLER_DISAGREE);
        update.setAuditUserId(userId);
        update.setAuditTime(LocalDateTime.now());
        update.setAuditReason(bo.getAuditReason());
        updateAfterSaleStatus(afterSale, STATUS_SELLER_DISAGREE, update);
        updateOrderItemAfterSale(afterSale.getOrderItemId(), null, ORDER_ITEM_AFTER_SALE_NONE);
        return true;
    }

    /**
     * 后台确认收到退货。
     */
    @Override
    public Boolean receiveAfterSale(Long userId, Long id) {
        AfterSale afterSale = validateAfterSaleReceivable(id);
        AfterSale update = new AfterSale();
        update.setStatus(STATUS_WAIT_REFUND);
        update.setReceiveTime(LocalDateTime.now());
        updateAfterSaleStatus(afterSale, STATUS_WAIT_REFUND, update);
        return true;
    }

    /**
     * 后台确认退款。
     */
    @Override
    public Boolean refundAfterSale(Long userId, Long id) {
        AfterSale afterSale = validateAfterSaleRefundable(id);
        AfterSale update = new AfterSale();
        update.setStatus(STATUS_COMPLETE);
        update.setRefundTime(LocalDateTime.now());
        updateAfterSaleStatus(afterSale, STATUS_COMPLETE, update);
        updateOrderItemAfterSale(afterSale.getOrderItemId(), afterSale.getId(), ORDER_ITEM_AFTER_SALE_SUCCESS);
        return true;
    }

    /**
     * 后台拒绝收货。
     */
    @Override
    public Boolean refuseAfterSale(Long userId, AfterSaleBo bo) {
        AfterSale afterSale = validateAfterSaleReceivable(bo.getId());
        AfterSale update = new AfterSale();
        update.setStatus(STATUS_SELLER_REFUSE);
        update.setReceiveTime(LocalDateTime.now());
        update.setReceiveReason(bo.getReceiveReason());
        updateAfterSaleStatus(afterSale, STATUS_SELLER_REFUSE, update);
        updateOrderItemAfterSale(afterSale.getOrderItemId(), null, ORDER_ITEM_AFTER_SALE_NONE);
        return true;
    }

    /**
     * 支付退款回调后更新售后单为已退款。
     */
    @Override
    public Boolean updateAfterSaleRefunded(Long id, Long orderId, Long payRefundId) {
        AfterSale afterSale = validateAfterSaleRefundable(id);
        if (orderId != null && !Objects.equals(afterSale.getOrderId(), orderId)) {
            throw new ServiceException("售后单与退款订单不匹配");
        }
        AfterSale update = new AfterSale();
        update.setStatus(STATUS_COMPLETE);
        update.setPayRefundId(payRefundId);
        update.setRefundTime(LocalDateTime.now());
        updateAfterSaleStatus(afterSale, STATUS_COMPLETE, update);
        updateOrderItemAfterSale(afterSale.getOrderItemId(), afterSale.getId(), ORDER_ITEM_AFTER_SALE_SUCCESS);
        return true;
    }

    /**
     * 新增或修改售后单。
     */
    @Override
    public Boolean saveOrUpdate(AfterSaleBo bo) {
        fileUrlFieldConverter.toPath(bo, "applyPicUrls", "picUrl");
        AfterSale entity = BeanUtil.toBean(bo, AfterSale.class);
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
     * 校验并批量删除售后单。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            AfterSale entity = new AfterSale();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    private TradeOrderItemVo validateOrderItemApplicable(Long userId, AfterSaleBo bo) {
        if (bo == null || bo.getOrderItemId() == null) {
            throw new ServiceException("订单项不能为空");
        }
        TradeOrderItemVo orderItem = tradeOrderItemService.queryById(bo.getOrderItemId());
        if (orderItem == null || !Objects.equals(orderItem.getUserId(), userId)) {
            throw new ServiceException("订单项不存在");
        }
        if (orderItem.getAfterSaleStatus() != null && !Objects.equals(orderItem.getAfterSaleStatus(), ORDER_ITEM_AFTER_SALE_NONE)) {
            throw new ServiceException("订单项已申请售后");
        }
        TradeOrderVo order = tradeOrderService.queryByUserAndId(userId, orderItem.getOrderId());
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (Objects.equals(order.getStatus(), ORDER_STATUS_CANCELED) || Objects.equals(order.getStatus(), ORDER_STATUS_UNPAID)) {
            throw new ServiceException("未支付或已取消订单不能申请售后");
        }
        if (Objects.equals(bo.getWay(), WAY_RETURN_AND_REFUND)
                && !Objects.equals(order.getStatus(), ORDER_STATUS_DELIVERED)
                && !Objects.equals(order.getStatus(), ORDER_STATUS_COMPLETED)) {
            throw new ServiceException("订单发货后才可以申请退货退款");
        }
        int refundPrice = bo.getRefundPrice() == null ? 0 : bo.getRefundPrice();
        if (refundPrice < 0 || refundPrice > (orderItem.getPayPrice() == null ? 0 : orderItem.getPayPrice())) {
            throw new ServiceException("退款金额不能超过订单项实付金额");
        }
        return orderItem;
    }

    private AfterSale validateUserAfterSale(Long userId, Long id) {
        AfterSale afterSale = baseMapper.selectById(id);
        if (afterSale == null || Boolean.TRUE.equals(afterSale.getIsDeleted()) || !Objects.equals(afterSale.getUserId(), userId)) {
            throw new ServiceException("售后单不存在");
        }
        return afterSale;
    }

    private AfterSale validateAfterSaleAuditable(Long id) {
        AfterSale afterSale = validateAfterSaleExists(id);
        if (!Objects.equals(afterSale.getStatus(), STATUS_APPLY)) {
            throw new ServiceException("只有待审核售后单可以审核");
        }
        return afterSale;
    }

    private AfterSale validateAfterSaleReceivable(Long id) {
        AfterSale afterSale = validateAfterSaleExists(id);
        if (!Objects.equals(afterSale.getStatus(), STATUS_BUYER_DELIVERY)) {
            throw new ServiceException("只有买家已退货售后单可以确认收货");
        }
        return afterSale;
    }

    private AfterSale validateAfterSaleRefundable(Long id) {
        AfterSale afterSale = validateAfterSaleExists(id);
        if (!Objects.equals(afterSale.getStatus(), STATUS_WAIT_REFUND)) {
            throw new ServiceException("只有待退款售后单可以退款");
        }
        return afterSale;
    }

    private AfterSale validateAfterSaleExists(Long id) {
        AfterSale afterSale = baseMapper.selectById(id);
        if (afterSale == null || Boolean.TRUE.equals(afterSale.getIsDeleted())) {
            throw new ServiceException("售后单不存在");
        }
        return afterSale;
    }

    private void updateAfterSaleStatus(AfterSale oldAfterSale, Integer newStatus, AfterSale update) {
        AfterSale entity = update == null ? new AfterSale() : update;
        entity.setId(oldAfterSale.getId());
        entity.setStatus(newStatus);
        entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        entity.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(entity);
    }

    private void updateOrderItemAfterSale(Long orderItemId, Long afterSaleId, Integer afterSaleStatus) {
        TradeOrderItem item = new TradeOrderItem();
        item.setId(orderItemId);
        item.setAfterSaleId(afterSaleId);
        item.setAfterSaleStatus(afterSaleStatus);
        item.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        item.setUpdateTime(LocalDateTime.now());
        tradeOrderItemMapper.updateById(item);
    }

    private String generateAfterSaleNo() {
        return "S" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    /**
     * 构建售后单查询条件。
     */
    private LambdaQueryWrapper<AfterSale> buildQueryWrapper(AfterSaleBo bo) {
        LambdaQueryWrapper<AfterSale> lqw = Wrappers.lambdaQuery();
        lqw.eq(AfterSale::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, AfterSale::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getNo()), AfterSale::getNo, bo.getNo());
        lqw.eq(bo.getStatus() != null, AfterSale::getStatus, bo.getStatus());
        lqw.eq(bo.getWay() != null, AfterSale::getWay, bo.getWay());
        lqw.eq(bo.getType() != null, AfterSale::getType, bo.getType());
        lqw.eq(bo.getUserId() != null, AfterSale::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getApplyReason()), AfterSale::getApplyReason, bo.getApplyReason());
        lqw.like(StringUtils.isNotBlank(bo.getApplyDescription()), AfterSale::getApplyDescription, bo.getApplyDescription());
        lqw.eq(bo.getOrderId() != null, AfterSale::getOrderId, bo.getOrderId());
        lqw.like(StringUtils.isNotBlank(bo.getOrderNo()), AfterSale::getOrderNo, bo.getOrderNo());
        lqw.eq(bo.getOrderItemId() != null, AfterSale::getOrderItemId, bo.getOrderItemId());
        lqw.eq(bo.getSpuId() != null, AfterSale::getSpuId, bo.getSpuId());
        lqw.like(StringUtils.isNotBlank(bo.getSpuName()), AfterSale::getSpuName, bo.getSpuName());
        lqw.eq(bo.getSkuId() != null, AfterSale::getSkuId, bo.getSkuId());
        lqw.like(StringUtils.isNotBlank(bo.getPicUrl()), AfterSale::getPicUrl, bo.getPicUrl());
        lqw.eq(bo.getCount() != null, AfterSale::getCount, bo.getCount());
        lqw.eq(bo.getAuditTime() != null, AfterSale::getAuditTime, bo.getAuditTime());
        lqw.eq(bo.getAuditUserId() != null, AfterSale::getAuditUserId, bo.getAuditUserId());
        lqw.like(StringUtils.isNotBlank(bo.getAuditReason()), AfterSale::getAuditReason, bo.getAuditReason());
        lqw.eq(bo.getRefundPrice() != null, AfterSale::getRefundPrice, bo.getRefundPrice());
        lqw.eq(bo.getPayRefundId() != null, AfterSale::getPayRefundId, bo.getPayRefundId());
        lqw.eq(bo.getRefundTime() != null, AfterSale::getRefundTime, bo.getRefundTime());
        lqw.eq(bo.getLogisticsId() != null, AfterSale::getLogisticsId, bo.getLogisticsId());
        lqw.like(StringUtils.isNotBlank(bo.getLogisticsNo()), AfterSale::getLogisticsNo, bo.getLogisticsNo());
        lqw.eq(bo.getDeliveryTime() != null, AfterSale::getDeliveryTime, bo.getDeliveryTime());
        lqw.eq(bo.getReceiveTime() != null, AfterSale::getReceiveTime, bo.getReceiveTime());
        lqw.like(StringUtils.isNotBlank(bo.getReceiveReason()), AfterSale::getReceiveReason, bo.getReceiveReason());
        return lqw;
    }

}

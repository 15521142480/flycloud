package com.fly.pay.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.pay.enums.PayNotifyTypeEnum;
import com.fly.pay.mapper.PayDemoOrderMapper;
import com.fly.pay.mapper.PayOrderMapper;
import com.fly.pay.mapper.PayRefundMapper;
import com.fly.pay.service.IPayDemoOrderService;
import com.fly.pay.service.IPayNotifyService;
import com.fly.pay.service.IPayOrderService;
import com.fly.system.api.pay.domain.PayDemoOrder;
import com.fly.system.api.pay.domain.PayOrder;
import com.fly.system.api.pay.domain.PayRefund;
import com.fly.system.api.pay.domain.bo.PayDemoOrderCreateReqBo;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import com.fly.system.api.pay.domain.vo.PayDemoOrderVo;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 支付示例订单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Slf4j
@Service
public class PayDemoOrderServiceImpl implements IPayDemoOrderService {

    private static final String PAY_APP_KEY = "demo";

    private static final int USER_TYPE_ADMIN = 1;

    private static final int ORDER_STATUS_SUCCESS = 10;

    private static final int REFUND_STATUS_SUCCESS = 10;

    private static final long DEFAULT_APP_ID = 1L;

    private final Map<Long, Object[]> spuMap = new HashMap<>();

    private final PayDemoOrderMapper payDemoOrderMapper;
    private final PayOrderMapper payOrderMapper;
    private final PayRefundMapper payRefundMapper;
    private final IPayOrderService payOrderService;
    private final ObjectProvider<IPayNotifyService> payNotifyServiceProvider;

    public PayDemoOrderServiceImpl(PayDemoOrderMapper payDemoOrderMapper,
                                   PayOrderMapper payOrderMapper,
                                   PayRefundMapper payRefundMapper,
                                   IPayOrderService payOrderService,
                                   ObjectProvider<IPayNotifyService> payNotifyServiceProvider) {
        this.payDemoOrderMapper = payDemoOrderMapper;
        this.payOrderMapper = payOrderMapper;
        this.payRefundMapper = payRefundMapper;
        this.payOrderService = payOrderService;
        this.payNotifyServiceProvider = payNotifyServiceProvider;
        spuMap.put(1L, new Object[]{"华为手机", 1});
        spuMap.put(2L, new Object[]{"小米电视", 10});
        spuMap.put(3L, new Object[]{"苹果手表", 100});
        spuMap.put(4L, new Object[]{"华硕笔记本", 1000});
        spuMap.put(5L, new Object[]{"蔚来汽车", 200000});
    }

    /**
     * 创建支付示例订单并同步创建支付订单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDemoOrder(Long userId, PayDemoOrderCreateReqBo createReqBo, String userIp) {
        Object[] spu = spuMap.get(createReqBo.getSpuId());
        if (spu == null) {
            throw new ServiceException("示例商品不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        PayDemoOrder demoOrder = new PayDemoOrder();
        demoOrder.setUserId(userId);
        demoOrder.setSpuId(createReqBo.getSpuId());
        demoOrder.setSpuName((String) spu[0]);
        demoOrder.setPrice((Integer) spu[1]);
        demoOrder.setPayStatus(false);
        demoOrder.setRefundPrice(0);
        demoOrder.setIsDeleted(false);
        demoOrder.setCreateBy(String.valueOf(userId));
        demoOrder.setCreateTime(now);
        demoOrder.setUpdateBy(String.valueOf(userId));
        demoOrder.setUpdateTime(now);
        payDemoOrderMapper.insert(demoOrder);

        PayOrderCreateReqDto orderCreateReq = new PayOrderCreateReqDto();
        orderCreateReq.setAppKey(PAY_APP_KEY);
        orderCreateReq.setUserIp(userIp);
        orderCreateReq.setUserId(userId);
        orderCreateReq.setUserType(USER_TYPE_ADMIN);
        orderCreateReq.setMerchantOrderId(String.valueOf(demoOrder.getId()));
        orderCreateReq.setSubject(demoOrder.getSpuName());
        orderCreateReq.setBody("");
        orderCreateReq.setPrice(demoOrder.getPrice());
        orderCreateReq.setExpireTime(now.plusHours(2));
        Long payOrderId = payOrderService.createPayOrder(orderCreateReq);

        PayDemoOrder updateOrder = new PayDemoOrder();
        updateOrder.setId(demoOrder.getId());
        updateOrder.setPayOrderId(payOrderId);
        updateOrder.setUpdateBy(String.valueOf(userId));
        updateOrder.setUpdateTime(LocalDateTime.now());
        payDemoOrderMapper.updateById(updateOrder);
        return demoOrder.getId();
    }

    @Override
    public PayDemoOrder getDemoOrder(Long id) {
        return payDemoOrderMapper.selectOne(Wrappers.<PayDemoOrder>lambdaQuery()
                .eq(PayDemoOrder::getId, id)
                .eq(PayDemoOrder::getIsDeleted, false)
                .last("LIMIT 1"));
    }

    @Override
    public PageVo<PayDemoOrderVo> getDemoOrderPage(PageBo pageBo) {
        Page<PayDemoOrderVo> page = payDemoOrderMapper.selectVoPage(pageBo.build(),
                Wrappers.<PayDemoOrder>lambdaQuery()
                        .eq(PayDemoOrder::getIsDeleted, false)
                        .orderByDesc(PayDemoOrder::getId));
        PageVo<PayDemoOrderVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 支付回调后更新示例订单支付状态。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDemoOrderPaid(Long id, Long payOrderId) {
        PayDemoOrder order = getRequiredDemoOrder(id);
        if (Boolean.TRUE.equals(order.getPayStatus())) {
            if (Objects.equals(order.getPayOrderId(), payOrderId)) {
                log.warn("[updateDemoOrderPaid][order({}) 已支付，重复回调直接返回]", id);
                return;
            }
            throw new ServiceException("示例订单更新支付状态失败，支付单编号不匹配");
        }

        PayOrderRespVo payOrder = validatePayOrderPaid(order, payOrderId);
        PayDemoOrder updateOrder = new PayDemoOrder();
        updateOrder.setPayStatus(true);
        updateOrder.setPayTime(LocalDateTime.now());
        updateOrder.setPayChannelCode(payOrder.getChannelCode());
        updateOrder.setUpdateTime(LocalDateTime.now());
        int updateCount = payDemoOrderMapper.updateByIdAndPayStatus(id, false, updateOrder);
        if (updateCount == 0) {
            throw new ServiceException("示例订单更新支付状态失败，订单不是未支付状态");
        }
    }

    /**
     * 发起示例订单退款，并写入本地支付退款单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundDemoOrder(Long id, String userIp) {
        PayDemoOrder order = validateDemoOrderCanRefund(id);
        PayOrder payOrder = payOrderMapper.selectById(order.getPayOrderId());
        if (payOrder == null || Boolean.TRUE.equals(payOrder.getIsDeleted())) {
            throw new ServiceException("支付订单不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        PayRefund refund = new PayRefund();
        refund.setNo(generateNo("R"));
        refund.setAppId(payOrder.getAppId() == null ? DEFAULT_APP_ID : payOrder.getAppId());
        refund.setChannelId(payOrder.getChannelId());
        refund.setChannelCode(payOrder.getChannelCode());
        refund.setOrderId(payOrder.getId());
        refund.setOrderNo(payOrder.getNo());
        refund.setUserId(order.getUserId());
        refund.setUserType(payOrder.getUserType());
        refund.setMerchantOrderId(String.valueOf(order.getId()));
        refund.setMerchantRefundId(order.getId() + "-refund");
        refund.setStatus(REFUND_STATUS_SUCCESS);
        refund.setPayPrice(order.getPrice());
        refund.setRefundPrice(order.getPrice());
        refund.setReason("想退钱");
        refund.setUserIp(userIp);
        refund.setChannelOrderNo(payOrder.getChannelOrderNo());
        refund.setSuccessTime(now);
        refund.setIsDeleted(false);
        refund.setCreateBy(String.valueOf(order.getUserId()));
        refund.setCreateTime(now);
        refund.setUpdateBy(String.valueOf(order.getUserId()));
        refund.setUpdateTime(now);
        payRefundMapper.insert(refund);

        PayDemoOrder updateOrder = new PayDemoOrder();
        updateOrder.setId(id);
        updateOrder.setPayRefundId(refund.getId());
        updateOrder.setRefundPrice(order.getPrice());
        updateOrder.setUpdateTime(LocalDateTime.now());
        payDemoOrderMapper.updateById(updateOrder);

        IPayNotifyService notifyService = payNotifyServiceProvider.getIfAvailable();
        if (notifyService != null) {
            notifyService.createPayNotifyTask(PayNotifyTypeEnum.REFUND.getType(), refund.getId());
        }
    }

    /**
     * 退款回调后更新示例订单退款完成时间。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDemoOrderRefunded(Long id, String refundId, Long payRefundId) {
        PayRefund payRefund = validateDemoOrderCanRefunded(id, refundId, payRefundId);
        PayDemoOrder updateOrder = new PayDemoOrder();
        updateOrder.setId(id);
        updateOrder.setRefundTime(payRefund.getSuccessTime());
        updateOrder.setUpdateTime(LocalDateTime.now());
        payDemoOrderMapper.updateById(updateOrder);
    }

    private PayDemoOrder getRequiredDemoOrder(Long id) {
        PayDemoOrder order = getDemoOrder(id);
        if (order == null) {
            throw new ServiceException("示例订单不存在");
        }
        return order;
    }

    private PayOrderRespVo validatePayOrderPaid(PayDemoOrder order, Long payOrderId) {
        PayOrderRespVo payOrder = payOrderService.getOrder(payOrderId);
        if (payOrder == null) {
            throw new ServiceException("支付订单不存在");
        }
        if (!Objects.equals(payOrder.getStatus(), ORDER_STATUS_SUCCESS)) {
            throw new ServiceException("示例订单更新支付状态失败，支付单状态不是支付成功");
        }
        if (!Objects.equals(payOrder.getPrice(), order.getPrice())) {
            throw new ServiceException("示例订单更新支付状态失败，支付金额不匹配");
        }
        if (!Objects.equals(payOrder.getMerchantOrderId(), String.valueOf(order.getId()))) {
            throw new ServiceException("示例订单更新支付状态失败，支付单编号不匹配");
        }
        return payOrder;
    }

    private PayDemoOrder validateDemoOrderCanRefund(Long id) {
        PayDemoOrder order = getRequiredDemoOrder(id);
        if (!Boolean.TRUE.equals(order.getPayStatus())) {
            throw new ServiceException("发起退款失败，示例订单未支付");
        }
        if (order.getPayRefundId() != null) {
            throw new ServiceException("发起退款失败，示例订单已退款");
        }
        return order;
    }

    private PayRefund validateDemoOrderCanRefunded(Long id, String refundId, Long payRefundId) {
        PayDemoOrder order = getRequiredDemoOrder(id);
        if (!Objects.equals(order.getPayRefundId(), payRefundId)) {
            throw new ServiceException("发起退款失败，退款单编号不匹配");
        }
        PayRefund payRefund = payRefundMapper.selectById(payRefundId);
        if (payRefund == null || Boolean.TRUE.equals(payRefund.getIsDeleted())) {
            throw new ServiceException("发起退款失败，退款订单不存在");
        }
        if (!Objects.equals(payRefund.getStatus(), REFUND_STATUS_SUCCESS)) {
            throw new ServiceException("发起退款失败，退款订单未退款成功");
        }
        if (!Objects.equals(payRefund.getRefundPrice(), order.getPrice())) {
            throw new ServiceException("发起退款失败，退款单金额不匹配");
        }
        if (!Objects.equals(payRefund.getMerchantRefundId(), refundId)
                || !Objects.equals(payRefund.getMerchantRefundId(), id + "-refund")) {
            throw new ServiceException("发起退款失败，退款单编号不匹配");
        }
        return payRefund;
    }

    private String generateNo(String prefix) {
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

}

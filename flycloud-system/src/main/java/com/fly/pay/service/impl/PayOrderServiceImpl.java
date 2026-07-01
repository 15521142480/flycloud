package com.fly.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.utils.StringUtils;
import com.fly.pay.enums.PayNotifyTypeEnum;
import com.fly.pay.enums.PayWalletBizTypeEnum;
import com.fly.pay.mapper.PayAppMapper;
import com.fly.pay.mapper.PayOrderExtensionMapper;
import com.fly.pay.mapper.PayOrderMapper;
import com.fly.pay.service.IPayNotifyService;
import com.fly.pay.service.IPayOrderService;
import com.fly.pay.service.IPayWalletRechargeService;
import com.fly.pay.service.IPayWalletService;
import com.fly.pay.utils.PayNotifyParseUtils;
import com.fly.system.api.pay.domain.PayApp;
import com.fly.system.api.pay.domain.PayWallet;
import com.fly.system.api.pay.domain.PayOrder;
import com.fly.system.api.pay.domain.PayOrderExtension;
import com.fly.system.api.pay.domain.bo.AppPayOrderSubmitReqVo;
import com.fly.system.api.pay.domain.bo.PayOrderBo;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import com.fly.system.api.pay.domain.vo.AppPayOrderSubmitRespVo;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 支付订单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class PayOrderServiceImpl implements IPayOrderService {

    /**
     * 等待支付。
     */
    private static final int ORDER_STATUS_WAITING = 0;

    /**
     * 支付成功。
     */
    private static final int ORDER_STATUS_SUCCESS = 10;

    /**
     * 支付关闭。
     */
    private static final int ORDER_STATUS_CLOSED = 30;

    /**
     * 默认支付应用编号。
     */
    private static final long DEFAULT_APP_ID = 1L;

    private final PayOrderMapper payOrderMapper;
    private final PayOrderExtensionMapper payOrderExtensionMapper;
    private final PayAppMapper payAppMapper;
    private final ObjectProvider<IPayWalletService> payWalletServiceProvider;
    private final ObjectProvider<IPayWalletRechargeService> walletRechargeServiceProvider;
    private final ObjectProvider<IPayNotifyService> payNotifyServiceProvider;

    /**
     * 创建支付订单。
     */
    @Override
    public Long createPayOrder(PayOrderCreateReqDto createReqDto) {
        Long appId = createReqDto.getAppId() == null ? DEFAULT_APP_ID : createReqDto.getAppId();
        PayOrder existsOrder = selectByAppIdAndMerchantOrderId(appId, createReqDto.getMerchantOrderId());
        if (existsOrder != null) {
            return existsOrder.getId();
        }

        LocalDateTime now = LocalDateTime.now();
        PayOrder order = new PayOrder();
        PayApp app = payAppMapper.selectById(appId);
        order.setAppId(appId);
        order.setUserId(createReqDto.getUserId());
        order.setUserType(createReqDto.getUserType());
        order.setMerchantOrderId(createReqDto.getMerchantOrderId());
        order.setSubject(createReqDto.getSubject());
        order.setBody(createReqDto.getBody());
        order.setNotifyUrl(app == null ? null : app.getOrderNotifyUrl());
        order.setPrice(createReqDto.getPrice());
        order.setChannelFeeRate(0D);
        order.setChannelFeePrice(0);
        order.setStatus(ORDER_STATUS_WAITING);
        order.setUserIp(createReqDto.getUserIp());
        order.setExpireTime(createReqDto.getExpireTime());
        order.setRefundPrice(0);
        order.setIsDeleted(false);
        order.setCreateBy(String.valueOf(createReqDto.getUserId()));
        order.setCreateTime(now);
        order.setUpdateBy(String.valueOf(createReqDto.getUserId()));
        order.setUpdateTime(now);
        payOrderMapper.insert(order);
        return order.getId();
    }

    /**
     * 根据编号查询支付订单。
     */
    @Override
    public PayOrderRespVo getOrder(Long id) {
        if (id == null) {
            return null;
        }
        PayOrderRespVo order = payOrderMapper.selectVoById(id);
        if (order == null || Boolean.TRUE.equals(order.getIsDeleted())) {
            return null;
        }
        return order;
    }

    /**
     * 根据支付订单号查询支付订单。
     */
    @Override
    public PayOrderRespVo getOrder(String no) {
        if (no == null) {
            return null;
        }
        LambdaQueryWrapper<PayOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayOrder::getIsDeleted, false);
        lqw.eq(PayOrder::getNo, no);
        lqw.last("LIMIT 1");
        return payOrderMapper.selectVoOne(lqw);
    }

    /**
     * 分页查询支付订单。
     */
    @Override
    public PageVo<PayOrderRespVo> queryPageList(PayOrderBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PayOrder> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayOrder::getId);
        Page<PayOrderRespVo> page = payOrderMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<PayOrderRespVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 查询支付订单列表。
     */
    @Override
    public List<PayOrderRespVo> queryList(PayOrderBo bo) {
        LambdaQueryWrapper<PayOrder> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayOrder::getId);
        return payOrderMapper.selectVoList(lqw);
    }

    /**
     * 提交支付订单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppPayOrderSubmitRespVo submitOrder(Long userId, AppPayOrderSubmitReqVo submitReqVo, String userIp) {
        PayOrder order = validateOrderCanSubmit(userId, submitReqVo.getId());
        PayOrderExtension extension = createOrderExtension(order, submitReqVo, userIp);

        PayOrder updateOrder = new PayOrder();
        updateOrder.setId(order.getId());
        updateOrder.setChannelId(extension.getChannelId());
        updateOrder.setChannelCode(extension.getChannelCode());
        updateOrder.setExtensionId(extension.getId());
        updateOrder.setNo(extension.getNo());
        updateOrder.setUpdateBy(String.valueOf(userId));
        updateOrder.setUpdateTime(LocalDateTime.now());

        if (Objects.equals("wallet", submitReqVo.getChannelCode())) {
            PayWallet wallet = payWalletServiceProvider.getObject().getOrCreateWallet(userId, order.getUserType());
            payWalletServiceProvider.getObject().reduceWalletBalance(wallet.getId(), String.valueOf(order.getId()),
                    PayWalletBizTypeEnum.PAYMENT, order.getPrice());
            markOrderSuccess(userId, updateOrder, extension);
        } else if (Objects.equals("mock", submitReqVo.getChannelCode())) {
            markOrderSuccess(userId, updateOrder, extension);
        }
        payOrderMapper.updateById(updateOrder);

        if (ORDER_STATUS_SUCCESS == (updateOrder.getStatus() == null ? order.getStatus() : updateOrder.getStatus())) {
            IPayWalletRechargeService rechargeService = walletRechargeServiceProvider.getIfAvailable();
            if (rechargeService != null) {
                rechargeService.updateWalletRechargePaid(order.getId(), extension.getChannelCode());
            }
            IPayNotifyService notifyService = payNotifyServiceProvider.getIfAvailable();
            if (notifyService != null) {
                notifyService.createPayNotifyTask(PayNotifyTypeEnum.ORDER.getType(), order.getId());
            }
        }

        AppPayOrderSubmitRespVo respVo = new AppPayOrderSubmitRespVo();
        respVo.setStatus(updateOrder.getStatus() == null ? order.getStatus() : updateOrder.getStatus());
        respVo.setDisplayMode("none");
        respVo.setDisplayContent("");
        return respVo;
    }

    /**
     * 支付渠道回调后更新支付订单。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyOrder(Long channelId, Map<String, String> params, String body, Map<String, String> headers) {
        Map<String, String> data = PayNotifyParseUtils.parseNotifyData(params, body);
        PayOrder order = findNotifyOrder(data);
        if (order == null) {
            throw new ServiceException("支付回调对应的支付订单不存在");
        }
        if (Objects.equals(order.getStatus(), ORDER_STATUS_SUCCESS)) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        PayOrder updateOrder = new PayOrder();
        updateOrder.setId(order.getId());
        updateOrder.setChannelId(channelId);
        updateOrder.setStatus(ORDER_STATUS_SUCCESS);
        updateOrder.setSuccessTime(now);
        updateOrder.setChannelOrderNo(PayNotifyParseUtils.firstValue(data, "channelOrderNo", "transaction_id", "trade_no"));
        updateOrder.setUpdateBy("callback");
        updateOrder.setUpdateTime(now);
        payOrderMapper.updateById(updateOrder);

        if (order.getExtensionId() != null) {
            PayOrderExtension updateExtension = new PayOrderExtension();
            updateExtension.setId(order.getExtensionId());
            updateExtension.setStatus(ORDER_STATUS_SUCCESS);
            updateExtension.setChannelNotifyData(PayNotifyParseUtils.toNotifyData(params, body, headers));
            updateExtension.setUpdateBy("callback");
            updateExtension.setUpdateTime(now);
            payOrderExtensionMapper.updateById(updateExtension);
        }

        IPayWalletRechargeService rechargeService = walletRechargeServiceProvider.getIfAvailable();
        if (rechargeService != null) {
            rechargeService.updateWalletRechargePaid(order.getId(), order.getChannelCode());
        }
        IPayNotifyService notifyService = payNotifyServiceProvider.getIfAvailable();
        if (notifyService != null) {
            notifyService.createPayNotifyTask(PayNotifyTypeEnum.ORDER.getType(), order.getId());
        }
    }

    /**
     * 标记支付订单和拓展单支付成功。
     */
    private void markOrderSuccess(Long userId, PayOrder updateOrder, PayOrderExtension extension) {
            updateOrder.setStatus(ORDER_STATUS_SUCCESS);
            updateOrder.setSuccessTime(LocalDateTime.now());
            PayOrderExtension updateExtension = new PayOrderExtension();
            updateExtension.setId(extension.getId());
            updateExtension.setStatus(ORDER_STATUS_SUCCESS);
            updateExtension.setUpdateBy(String.valueOf(userId));
            updateExtension.setUpdateTime(LocalDateTime.now());
            payOrderExtensionMapper.updateById(updateExtension);
    }

    /**
     * 校验支付订单是否可以提交。
     */
    private PayOrder validateOrderCanSubmit(Long userId, Long id) {
        PayOrder order = payOrderMapper.selectById(id);
        if (order == null || Boolean.TRUE.equals(order.getIsDeleted())) {
            throw new ServiceException("支付订单不存在");
        }
        if (order.getUserId() != null && !Objects.equals(order.getUserId(), userId)) {
            throw new ServiceException("支付订单不存在");
        }
        if (Objects.equals(order.getStatus(), ORDER_STATUS_SUCCESS)) {
            throw new ServiceException("支付订单已支付");
        }
        if (Objects.equals(order.getStatus(), ORDER_STATUS_CLOSED)) {
            throw new ServiceException("支付订单已关闭");
        }
        if (order.getExpireTime() != null && order.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new ServiceException("支付订单已过期");
        }
        return order;
    }

    /**
     * 创建支付订单拓展记录。
     */
    private PayOrderExtension createOrderExtension(PayOrder order, AppPayOrderSubmitReqVo submitReqVo, String userIp) {
        LocalDateTime now = LocalDateTime.now();
        PayOrderExtension extension = new PayOrderExtension();
        extension.setNo(generatePayOrderNo());
        extension.setOrderId(order.getId());
        extension.setChannelId(resolveChannelId(submitReqVo.getChannelCode()));
        extension.setChannelCode(submitReqVo.getChannelCode());
        extension.setUserIp(userIp);
        extension.setStatus(ORDER_STATUS_WAITING);
        extension.setChannelExtras(submitReqVo.getChannelExtras());
        extension.setIsDeleted(false);
        extension.setCreateBy(String.valueOf(order.getUserId()));
        extension.setCreateTime(now);
        extension.setUpdateBy(String.valueOf(order.getUserId()));
        extension.setUpdateTime(now);
        payOrderExtensionMapper.insert(extension);
        return extension;
    }

    /**
     * 查询指定商户订单的支付单。
     */
    private PayOrder selectByAppIdAndMerchantOrderId(Long appId, String merchantOrderId) {
        LambdaQueryWrapper<PayOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayOrder::getIsDeleted, false);
        lqw.eq(PayOrder::getAppId, appId);
        lqw.eq(PayOrder::getMerchantOrderId, merchantOrderId);
        lqw.last("LIMIT 1");
        return payOrderMapper.selectOne(lqw);
    }

    /**
     * 查询支付回调对应的本地支付订单。
     */
    private PayOrder findNotifyOrder(Map<String, String> data) {
        String no = PayNotifyParseUtils.firstValue(data, "no", "out_trade_no", "outTradeNo", "payOrderNo");
        if (StringUtils.isNotBlank(no)) {
            LambdaQueryWrapper<PayOrder> noQuery = Wrappers.lambdaQuery();
            noQuery.eq(PayOrder::getIsDeleted, false);
            noQuery.eq(PayOrder::getNo, no);
            noQuery.last("LIMIT 1");
            PayOrder order = payOrderMapper.selectOne(noQuery);
            if (order != null) {
                return order;
            }
        }
        String merchantOrderId = PayNotifyParseUtils.firstValue(data, "merchantOrderId", "merchant_order_id");
        if (StringUtils.isNotBlank(merchantOrderId)) {
            LambdaQueryWrapper<PayOrder> merchantQuery = Wrappers.lambdaQuery();
            merchantQuery.eq(PayOrder::getIsDeleted, false);
            merchantQuery.eq(PayOrder::getMerchantOrderId, merchantOrderId);
            merchantQuery.last("LIMIT 1");
            return payOrderMapper.selectOne(merchantQuery);
        }
        return null;
    }

    /**
     * 构建支付订单查询条件。
     */
    private LambdaQueryWrapper<PayOrder> buildQueryWrapper(PayOrderBo bo) {
        LambdaQueryWrapper<PayOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayOrder::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, PayOrder::getId, bo.getId());
        lqw.eq(bo.getAppId() != null, PayOrder::getAppId, bo.getAppId());
        lqw.eq(bo.getChannelId() != null, PayOrder::getChannelId, bo.getChannelId());
        lqw.eq(StringUtils.isNotBlank(bo.getChannelCode()), PayOrder::getChannelCode, bo.getChannelCode());
        lqw.eq(bo.getUserId() != null, PayOrder::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getMerchantOrderId()), PayOrder::getMerchantOrderId, bo.getMerchantOrderId());
        lqw.like(StringUtils.isNotBlank(bo.getSubject()), PayOrder::getSubject, bo.getSubject());
        lqw.eq(bo.getStatus() != null, PayOrder::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getNo()), PayOrder::getNo, bo.getNo());
        lqw.eq(bo.getExpireTime() != null, PayOrder::getExpireTime, bo.getExpireTime());
        return lqw;
    }

    /**
     * 生成支付订单号。
     */
    private String generatePayOrderNo() {
        return "P" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    /**
     * 根据渠道编码生成基础渠道编号。
     */
    private Long resolveChannelId(String channelCode) {
        return switch (channelCode) {
            case "wallet" -> 1L;
            case "mock" -> 2L;
            default -> 0L;
        };
    }

}

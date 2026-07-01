package com.fly.pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.pay.enums.PayWalletBizTypeEnum;
import com.fly.pay.mapper.PayRefundMapper;
import com.fly.pay.mapper.PayWalletRechargeMapper;
import com.fly.pay.service.IPayNotifyService;
import com.fly.pay.service.IPayOrderService;
import com.fly.pay.service.IPayWalletRechargePackageService;
import com.fly.pay.service.IPayWalletRechargeService;
import com.fly.pay.service.IPayWalletService;
import com.fly.system.api.pay.domain.PayOrder;
import com.fly.system.api.pay.domain.PayRefund;
import com.fly.system.api.pay.domain.PayWallet;
import com.fly.system.api.pay.domain.PayWalletRecharge;
import com.fly.system.api.pay.domain.PayWalletRechargePackage;
import com.fly.system.api.pay.domain.bo.AppPayWalletRechargeCreateReqVo;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import com.fly.system.api.pay.domain.bo.PayWalletRechargeBo;
import com.fly.system.api.pay.domain.vo.AppPayWalletRechargeCreateRespVo;
import com.fly.system.api.pay.domain.vo.AppPayWalletRechargeRespVo;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;
import com.fly.system.api.pay.domain.vo.PayWalletRechargeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 支付钱包充值记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PayWalletRechargeServiceImpl implements IPayWalletRechargeService {

    private static final String WALLET_RECHARGE_ORDER_SUBJECT = "钱包余额充值";

    private static final Integer REFUND_STATUS_NONE = 0;
    private static final Integer REFUND_STATUS_WAITING = 0;
    private static final Integer REFUND_STATUS_SUCCESS = 10;
    private static final Integer PAY_ORDER_STATUS_SUCCESS = 10;
    private static final Long DEFAULT_APP_ID = 1L;

    private final PayWalletRechargeMapper walletRechargeMapper;
    private final PayRefundMapper payRefundMapper;
    private final IPayWalletService payWalletService;
    private final IPayWalletRechargePackageService rechargePackageService;
    private final IPayOrderService payOrderService;
    private final ObjectProvider<IPayNotifyService> payNotifyServiceProvider;

    /**
     * 创建钱包充值记录。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppPayWalletRechargeCreateRespVo createWalletRecharge(Long userId, Integer userType, String userIp,
                                                                 AppPayWalletRechargeCreateReqVo reqVo) {
        int payPrice;
        int bonusPrice = 0;
        Long packageId = reqVo.getPackageId();
        if (packageId != null) {
            PayWalletRechargePackage rechargePackage = rechargePackageService.validWalletRechargePackage(packageId);
            payPrice = rechargePackage.getPayPrice();
            bonusPrice = rechargePackage.getBonusPrice() == null ? 0 : rechargePackage.getBonusPrice();
        } else {
            payPrice = reqVo.getPayPrice();
        }

        PayWallet wallet = payWalletService.getOrCreateWallet(userId, userType);
        PayWalletRecharge recharge = createRecharge(wallet.getId(), payPrice, bonusPrice, packageId, userId);
        walletRechargeMapper.insert(recharge);

        PayOrderCreateReqDto payOrderCreateReqDto = new PayOrderCreateReqDto();
        payOrderCreateReqDto.setAppId(1L);
        payOrderCreateReqDto.setUserIp(userIp);
        payOrderCreateReqDto.setUserId(userId);
        payOrderCreateReqDto.setUserType(userType);
        payOrderCreateReqDto.setMerchantOrderId(String.valueOf(recharge.getId()));
        payOrderCreateReqDto.setSubject(WALLET_RECHARGE_ORDER_SUBJECT);
        payOrderCreateReqDto.setBody("");
        payOrderCreateReqDto.setPrice(recharge.getPayPrice());
        payOrderCreateReqDto.setExpireTime(LocalDateTime.now().plusHours(2));
        Long payOrderId = payOrderService.createPayOrder(payOrderCreateReqDto);

        PayWalletRecharge updateRecharge = new PayWalletRecharge();
        updateRecharge.setId(recharge.getId());
        updateRecharge.setPayOrderId(payOrderId);
        updateRecharge.setUpdateBy(String.valueOf(userId));
        updateRecharge.setUpdateTime(LocalDateTime.now());
        walletRechargeMapper.updateById(updateRecharge);

        AppPayWalletRechargeCreateRespVo respVo = new AppPayWalletRechargeCreateRespVo();
        respVo.setId(recharge.getId());
        respVo.setPayOrderId(payOrderId);
        return respVo;
    }

    /**
     * 查询移动端钱包充值分页。
     */
    @Override
    public PageVo<AppPayWalletRechargeRespVo> getAppWalletRechargePage(Long userId, Integer userType, PageBo pageBo) {
        PayWallet wallet = payWalletService.getOrCreateWallet(userId, userType);
        LambdaQueryWrapper<PayWalletRecharge> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletRecharge::getIsDeleted, false);
        lqw.eq(PayWalletRecharge::getWalletId, wallet.getId());
        lqw.eq(PayWalletRecharge::getPayStatus, true);
        lqw.orderByDesc(PayWalletRecharge::getId);
        Page<AppPayWalletRechargeRespVo> page = walletRechargeMapper.selectVoPage(pageBo.build(), lqw,
                AppPayWalletRechargeRespVo.class);
        List<AppPayWalletRechargeRespVo> list = page.getRecords();
        list.forEach(item -> item.setPayChannelName(resolveChannelName(item.getPayChannelCode())));
        PageVo<AppPayWalletRechargeRespVo> pageVo = new PageVo<>();
        pageVo.setList(list);
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 支付成功后回充钱包余额。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWalletRechargePaid(Long payOrderId, String channelCode) {
        PayWalletRecharge recharge = selectByPayOrderId(payOrderId);
        if (recharge == null || Boolean.TRUE.equals(recharge.getPayStatus())) {
            return;
        }
        PayWalletRecharge updateRecharge = new PayWalletRecharge();
        updateRecharge.setId(recharge.getId());
        updateRecharge.setPayStatus(true);
        updateRecharge.setPayTime(LocalDateTime.now());
        updateRecharge.setPayChannelCode(channelCode);
        updateRecharge.setUpdateBy(String.valueOf(recharge.getWalletId()));
        updateRecharge.setUpdateTime(LocalDateTime.now());
        walletRechargeMapper.updateById(updateRecharge);
        payWalletService.addWalletBalance(recharge.getWalletId(), String.valueOf(recharge.getId()),
                PayWalletBizTypeEnum.RECHARGE, recharge.getTotalPrice());
    }

    /**
     * 支付回调后更新钱包充值为已支付。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWalletRechargerPaid(Long id, Long payOrderId) {
        PayWalletRecharge recharge = getRequiredRecharge(id);
        if (Boolean.TRUE.equals(recharge.getPayStatus())) {
            if (Objects.equals(recharge.getPayOrderId(), payOrderId)) {
                return;
            }
            throw new ServiceException("钱包充值已支付，但是支付单编号不匹配");
        }

        PayOrderRespVo payOrder = validatePayOrderPaid(recharge, payOrderId);
        PayWalletRecharge updateRecharge = new PayWalletRecharge();
        updateRecharge.setId(id);
        updateRecharge.setPayStatus(true);
        updateRecharge.setPayTime(LocalDateTime.now());
        updateRecharge.setPayChannelCode(payOrder.getChannelCode());
        updateRecharge.setUpdateBy(String.valueOf(recharge.getWalletId()));
        updateRecharge.setUpdateTime(LocalDateTime.now());
        walletRechargeMapper.updateById(updateRecharge);

        payWalletService.addWalletBalance(recharge.getWalletId(), String.valueOf(id),
                PayWalletBizTypeEnum.RECHARGE, recharge.getTotalPrice());
    }

    /**
     * 发起钱包充值退款。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundWalletRecharge(Long id, String userIp) {
        PayWalletRecharge recharge = validateWalletRechargeCanRefund(id);
        PayWallet wallet = payWalletService.getWallet(recharge.getWalletId());
        PayOrderRespVo payOrder = payOrderService.getOrder(recharge.getPayOrderId());
        if (payOrder == null) {
            throw new ServiceException("支付订单不存在");
        }

        payWalletService.freezePrice(wallet.getId(), recharge.getTotalPrice());

        LocalDateTime now = LocalDateTime.now();
        PayRefund refund = new PayRefund();
        refund.setNo(generateNo("R"));
        refund.setAppId(payOrder.getAppId() == null ? DEFAULT_APP_ID : payOrder.getAppId());
        refund.setChannelId(payOrder.getChannelId());
        refund.setChannelCode(payOrder.getChannelCode());
        refund.setOrderId(payOrder.getId());
        refund.setOrderNo(payOrder.getNo());
        refund.setUserId(wallet.getUserId());
        refund.setUserType(wallet.getUserType());
        refund.setMerchantOrderId(String.valueOf(id));
        refund.setMerchantRefundId(id + "-refund");
        refund.setStatus(REFUND_STATUS_SUCCESS);
        refund.setPayPrice(recharge.getPayPrice());
        refund.setRefundPrice(recharge.getPayPrice());
        refund.setReason("想退钱");
        refund.setUserIp(userIp);
        refund.setChannelOrderNo(payOrder.getChannelOrderNo());
        refund.setSuccessTime(now);
        refund.setIsDeleted(false);
        refund.setCreateBy(String.valueOf(wallet.getUserId()));
        refund.setCreateTime(now);
        refund.setUpdateBy(String.valueOf(wallet.getUserId()));
        refund.setUpdateTime(now);
        payRefundMapper.insert(refund);

        PayWalletRecharge updateRecharge = new PayWalletRecharge();
        updateRecharge.setId(id);
        updateRecharge.setPayRefundId(refund.getId());
        updateRecharge.setRefundStatus(REFUND_STATUS_WAITING);
        updateRecharge.setUpdateBy(String.valueOf(wallet.getUserId()));
        updateRecharge.setUpdateTime(LocalDateTime.now());
        walletRechargeMapper.updateById(updateRecharge);

        IPayNotifyService notifyService = payNotifyServiceProvider.getIfAvailable();
        if (notifyService != null) {
            notifyService.createPayNotifyTask(2, refund.getId());
        }
    }

    /**
     * 退款回调后更新钱包充值为已退款。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWalletRechargeRefunded(Long id, String refundId, Long payRefundId) {
        PayWalletRecharge recharge = getRequiredRecharge(id);
        if (!Objects.equals(recharge.getPayRefundId(), payRefundId)) {
            throw new ServiceException("钱包充值退款单编号不匹配");
        }
        PayRefund refund = payRefundMapper.selectById(payRefundId);
        if (refund == null || Boolean.TRUE.equals(refund.getIsDeleted())) {
            throw new ServiceException("支付退款单不存在");
        }
        if (!Objects.equals(refund.getMerchantRefundId(), refundId)) {
            throw new ServiceException("商户退款单编号不匹配");
        }
        if (!Objects.equals(refund.getRefundPrice(), recharge.getPayPrice())) {
            throw new ServiceException("支付退款金额与钱包充值金额不匹配");
        }
        if (!Objects.equals(refund.getStatus(), REFUND_STATUS_SUCCESS)) {
            return;
        }

        payWalletService.reduceWalletBalance(recharge.getWalletId(), String.valueOf(id),
                PayWalletBizTypeEnum.RECHARGE_REFUND, recharge.getTotalPrice());

        PayWalletRecharge updateRecharge = new PayWalletRecharge();
        updateRecharge.setId(id);
        updateRecharge.setRefundStatus(REFUND_STATUS_SUCCESS);
        updateRecharge.setRefundTime(refund.getSuccessTime() == null ? LocalDateTime.now() : refund.getSuccessTime());
        updateRecharge.setRefundTotalPrice(recharge.getTotalPrice());
        updateRecharge.setRefundPayPrice(recharge.getPayPrice());
        updateRecharge.setRefundBonusPrice(recharge.getBonusPrice());
        updateRecharge.setUpdateBy(String.valueOf(recharge.getWalletId()));
        updateRecharge.setUpdateTime(LocalDateTime.now());
        walletRechargeMapper.updateById(updateRecharge);
    }

    /**
     * 分页查询钱包充值记录。
     */
    @Override
    public PageVo<PayWalletRechargeVo> queryPageList(PayWalletRechargeBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PayWalletRecharge> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayWalletRecharge::getId);
        Page<PayWalletRechargeVo> page = walletRechargeMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<PayWalletRechargeVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 创建钱包充值记录实体。
     */
    private PayWalletRecharge createRecharge(Long walletId, int payPrice, int bonusPrice, Long packageId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        PayWalletRecharge recharge = new PayWalletRecharge();
        recharge.setWalletId(walletId);
        recharge.setTotalPrice(payPrice + bonusPrice);
        recharge.setPayPrice(payPrice);
        recharge.setBonusPrice(bonusPrice);
        recharge.setPackageId(packageId);
        recharge.setPayStatus(false);
        recharge.setRefundTotalPrice(0);
        recharge.setRefundPayPrice(0);
        recharge.setRefundBonusPrice(0);
        recharge.setRefundStatus(REFUND_STATUS_NONE);
        recharge.setIsDeleted(false);
        recharge.setCreateBy(String.valueOf(userId));
        recharge.setCreateTime(now);
        recharge.setUpdateBy(String.valueOf(userId));
        recharge.setUpdateTime(now);
        return recharge;
    }

    /**
     * 根据支付订单编号查询充值记录。
     */
    private PayWalletRecharge selectByPayOrderId(Long payOrderId) {
        LambdaQueryWrapper<PayWalletRecharge> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletRecharge::getIsDeleted, false);
        lqw.eq(PayWalletRecharge::getPayOrderId, payOrderId);
        lqw.last("LIMIT 1");
        return walletRechargeMapper.selectOne(lqw);
    }

    /**
     * 查询必需存在的钱包充值记录。
     */
    private PayWalletRecharge getRequiredRecharge(Long id) {
        PayWalletRecharge recharge = walletRechargeMapper.selectById(id);
        if (recharge == null || Boolean.TRUE.equals(recharge.getIsDeleted())) {
            throw new ServiceException("钱包充值记录不存在");
        }
        return recharge;
    }

    /**
     * 校验支付订单是否已经成功支付并匹配充值记录。
     */
    private PayOrderRespVo validatePayOrderPaid(PayWalletRecharge recharge, Long payOrderId) {
        PayOrderRespVo payOrder = payOrderService.getOrder(payOrderId);
        if (payOrder == null) {
            throw new ServiceException("支付订单不存在");
        }
        if (!Objects.equals(payOrder.getStatus(), PAY_ORDER_STATUS_SUCCESS)) {
            throw new ServiceException("支付订单未支付成功");
        }
        if (!Objects.equals(payOrder.getPrice(), recharge.getPayPrice())) {
            throw new ServiceException("支付订单金额与钱包充值金额不匹配");
        }
        if (!Objects.equals(payOrder.getMerchantOrderId(), String.valueOf(recharge.getId()))) {
            throw new ServiceException("支付订单商户单号与钱包充值记录不匹配");
        }
        return payOrder;
    }

    /**
     * 校验钱包充值记录是否可以退款。
     */
    private PayWalletRecharge validateWalletRechargeCanRefund(Long id) {
        PayWalletRecharge recharge = getRequiredRecharge(id);
        if (!Boolean.TRUE.equals(recharge.getPayStatus())) {
            throw new ServiceException("钱包充值未支付，不能退款");
        }
        if (recharge.getPayRefundId() != null) {
            throw new ServiceException("钱包充值已发起退款");
        }
        PayWallet wallet = payWalletService.getWallet(recharge.getWalletId());
        if (wallet == null) {
            throw new ServiceException("钱包不存在");
        }
        if (wallet.getBalance() == null || wallet.getBalance() < recharge.getTotalPrice()) {
            throw new ServiceException("钱包余额不足，不能退款");
        }
        return recharge;
    }

    /**
     * 构建钱包充值记录查询条件。
     */
    private LambdaQueryWrapper<PayWalletRecharge> buildQueryWrapper(PayWalletRechargeBo bo) {
        LambdaQueryWrapper<PayWalletRecharge> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletRecharge::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, PayWalletRecharge::getId, bo.getId());
        lqw.eq(bo.getWalletId() != null, PayWalletRecharge::getWalletId, bo.getWalletId());
        lqw.eq(bo.getPayStatus() != null, PayWalletRecharge::getPayStatus, bo.getPayStatus());
        lqw.eq(bo.getPayOrderId() != null, PayWalletRecharge::getPayOrderId, bo.getPayOrderId());
        return lqw;
    }

    /**
     * 获取支付渠道名称。
     */
    private String resolveChannelName(String channelCode) {
        if ("wallet".equals(channelCode)) {
            return "钱包支付";
        }
        if ("mock".equals(channelCode)) {
            return "模拟支付";
        }
        return channelCode;
    }

    /**
     * 生成支付退款单号。
     */
    private String generateNo(String prefix) {
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

}

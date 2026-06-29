package com.fly.pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.enums.PayWalletBizTypeEnum;
import com.fly.pay.mapper.PayWalletRechargeMapper;
import com.fly.pay.service.IPayOrderService;
import com.fly.pay.service.IPayWalletRechargePackageService;
import com.fly.pay.service.IPayWalletRechargeService;
import com.fly.pay.service.IPayWalletService;
import com.fly.system.api.pay.domain.PayWallet;
import com.fly.system.api.pay.domain.PayWalletRecharge;
import com.fly.system.api.pay.domain.PayWalletRechargePackage;
import com.fly.system.api.pay.domain.bo.AppPayWalletRechargeCreateReqVo;
import com.fly.system.api.pay.domain.bo.PayOrderCreateReqDto;
import com.fly.system.api.pay.domain.bo.PayWalletRechargeBo;
import com.fly.system.api.pay.domain.vo.AppPayWalletRechargeCreateRespVo;
import com.fly.system.api.pay.domain.vo.AppPayWalletRechargeRespVo;
import com.fly.system.api.pay.domain.vo.PayWalletRechargeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付钱包充值记录 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class PayWalletRechargeServiceImpl implements IPayWalletRechargeService {

    private static final String WALLET_RECHARGE_ORDER_SUBJECT = "钱包余额充值";

    private static final Integer REFUND_STATUS_NONE = 0;

    private final PayWalletRechargeMapper walletRechargeMapper;
    private final IPayWalletService payWalletService;
    private final IPayWalletRechargePackageService rechargePackageService;
    private final IPayOrderService payOrderService;

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

}

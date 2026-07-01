package com.fly.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.utils.BeanUtils;
import com.fly.pay.enums.PayDemoWithdrawStatusEnum;
import com.fly.pay.enums.PayDemoWithdrawTypeEnum;
import com.fly.pay.enums.PayNotifyTypeEnum;
import com.fly.pay.mapper.PayDemoWithdrawMapper;
import com.fly.pay.mapper.PayTransferMapper;
import com.fly.pay.service.IPayDemoWithdrawService;
import com.fly.pay.service.IPayNotifyService;
import com.fly.system.api.pay.domain.PayDemoWithdraw;
import com.fly.system.api.pay.domain.PayTransfer;
import com.fly.system.api.pay.domain.bo.PayDemoWithdrawCreateReqBo;
import com.fly.system.api.pay.domain.vo.PayDemoWithdrawVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 支付示例提现单 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PayDemoWithdrawServiceImpl implements IPayDemoWithdrawService {

    private static final int USER_TYPE_ADMIN = 1;

    private static final int TRANSFER_STATUS_SUCCESS = 10;

    private static final int TRANSFER_STATUS_CLOSED = 20;

    private static final long DEFAULT_APP_ID = 1L;

    private final PayDemoWithdrawMapper payDemoWithdrawMapper;
    private final PayTransferMapper payTransferMapper;
    private final ObjectProvider<IPayNotifyService> payNotifyServiceProvider;

    /**
     * 创建支付示例提现单。
     */
    @Override
    public Long createDemoWithdraw(PayDemoWithdrawCreateReqBo createReqBo) {
        PayDemoWithdraw withdraw = BeanUtils.toBean(createReqBo, PayDemoWithdraw.class);
        withdraw.setTransferChannelCode(getTransferChannelCode(createReqBo.getType()));
        withdraw.setStatus(PayDemoWithdrawStatusEnum.WAITING.getStatus());
        withdraw.setIsDeleted(false);
        withdraw.setCreateTime(LocalDateTime.now());
        withdraw.setUpdateTime(LocalDateTime.now());
        payDemoWithdrawMapper.insert(withdraw);
        return withdraw.getId();
    }

    /**
     * 发起支付示例提现单转账。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long transferDemoWithdraw(Long id, Long userId, String userIp) {
        PayDemoWithdraw withdraw = validateDemoWithdrawCanTransfer(id);
        if (PayDemoWithdrawStatusEnum.isClosed(withdraw.getStatus())) {
            PayDemoWithdraw updateWaiting = new PayDemoWithdraw();
            updateWaiting.setStatus(PayDemoWithdrawStatusEnum.WAITING.getStatus());
            updateWaiting.setTransferErrorMsg("");
            updateWaiting.setUpdateTime(LocalDateTime.now());
            int updateCount = payDemoWithdrawMapper.updateByIdAndStatus(id, withdraw.getStatus(), updateWaiting);
            if (updateCount == 0) {
                throw new ServiceException("发起转账失败，示例提现单状态不是等待提现或提现关闭");
            }
            withdraw.setStatus(PayDemoWithdrawStatusEnum.WAITING.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        PayTransfer transfer = new PayTransfer();
        transfer.setNo(generateTransferNo());
        transfer.setAppId(DEFAULT_APP_ID);
        transfer.setChannelCode(withdraw.getTransferChannelCode());
        transfer.setUserId(userId);
        transfer.setUserType(USER_TYPE_ADMIN);
        transfer.setMerchantTransferId(String.valueOf(withdraw.getId()));
        transfer.setSubject(withdraw.getSubject());
        transfer.setPrice(withdraw.getPrice());
        transfer.setUserAccount(withdraw.getUserAccount());
        transfer.setUserName(withdraw.getUserName());
        transfer.setStatus(TRANSFER_STATUS_SUCCESS);
        transfer.setSuccessTime(now);
        transfer.setUserIp(userIp);
        transfer.setIsDeleted(false);
        transfer.setCreateBy(String.valueOf(userId));
        transfer.setCreateTime(now);
        transfer.setUpdateBy(String.valueOf(userId));
        transfer.setUpdateTime(now);
        payTransferMapper.insert(transfer);

        PayDemoWithdraw updateWithdraw = new PayDemoWithdraw();
        updateWithdraw.setPayTransferId(transfer.getId());
        updateWithdraw.setUpdateTime(LocalDateTime.now());
        int updateCount = payDemoWithdrawMapper.updateByIdAndStatus(withdraw.getId(), withdraw.getStatus(), updateWithdraw);
        if (updateCount == 0) {
            throw new ServiceException("发起转账失败，示例提现单状态不是等待提现或提现关闭");
        }

        IPayNotifyService notifyService = payNotifyServiceProvider.getIfAvailable();
        if (notifyService != null) {
            notifyService.createPayNotifyTask(PayNotifyTypeEnum.TRANSFER.getType(), transfer.getId());
        }
        return transfer.getId();
    }

    @Override
    public PageVo<PayDemoWithdrawVo> getDemoWithdrawPage(PageBo pageBo) {
        Page<PayDemoWithdrawVo> page = payDemoWithdrawMapper.selectVoPage(pageBo.build(),
                com.baomidou.mybatisplus.core.toolkit.Wrappers.<PayDemoWithdraw>lambdaQuery()
                        .eq(PayDemoWithdraw::getIsDeleted, false)
                        .orderByDesc(PayDemoWithdraw::getId));
        PageVo<PayDemoWithdrawVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 转账回调后更新示例提现单状态。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDemoWithdrawTransferred(Long id, Long payTransferId) {
        PayDemoWithdraw withdraw = getRequiredDemoWithdraw(id);
        if (PayDemoWithdrawStatusEnum.isSuccess(withdraw.getStatus())
                || PayDemoWithdrawStatusEnum.isClosed(withdraw.getStatus())) {
            if (Objects.equals(withdraw.getPayTransferId(), payTransferId)) {
                log.warn("[updateDemoWithdrawTransferred][withdraw({}) 已结束，重复回调直接返回]", id);
                return;
            }
            throw new ServiceException("更新示例提现单状态失败，支付转账单编号不匹配");
        }

        PayTransfer payTransfer = validateDemoTransferStatusCanUpdate(withdraw, payTransferId);
        Integer newStatus = Objects.equals(payTransfer.getStatus(), TRANSFER_STATUS_SUCCESS)
                ? PayDemoWithdrawStatusEnum.SUCCESS.getStatus()
                : Objects.equals(payTransfer.getStatus(), TRANSFER_STATUS_CLOSED)
                ? PayDemoWithdrawStatusEnum.CLOSED.getStatus()
                : null;
        if (newStatus == null) {
            throw new ServiceException("更新示例提现单状态失败，支付转账单状态不是转账成功或转账失败");
        }

        PayDemoWithdraw updateWithdraw = new PayDemoWithdraw();
        updateWithdraw.setStatus(newStatus);
        updateWithdraw.setTransferTime(payTransfer.getSuccessTime());
        updateWithdraw.setTransferErrorMsg(payTransfer.getChannelErrorMsg());
        updateWithdraw.setUpdateTime(LocalDateTime.now());
        int updateCount = payDemoWithdrawMapper.updateByIdAndStatus(withdraw.getId(), withdraw.getStatus(), updateWithdraw);
        if (updateCount == 0) {
            throw new ServiceException("更新示例提现单状态失败，提现单状态已变更");
        }
    }

    private PayDemoWithdraw validateDemoWithdrawCanTransfer(Long id) {
        PayDemoWithdraw withdraw = getRequiredDemoWithdraw(id);
        if (!PayDemoWithdrawStatusEnum.isWaiting(withdraw.getStatus())
                && !PayDemoWithdrawStatusEnum.isClosed(withdraw.getStatus())) {
            throw new ServiceException("发起转账失败，示例提现单状态不是等待提现或提现关闭");
        }
        return withdraw;
    }

    private PayDemoWithdraw getRequiredDemoWithdraw(Long id) {
        PayDemoWithdraw withdraw = payDemoWithdrawMapper.selectById(id);
        if (withdraw == null || Boolean.TRUE.equals(withdraw.getIsDeleted())) {
            throw new ServiceException("示例提现单不存在");
        }
        return withdraw;
    }

    private PayTransfer validateDemoTransferStatusCanUpdate(PayDemoWithdraw withdraw, Long payTransferId) {
        PayTransfer payTransfer = payTransferMapper.selectById(payTransferId);
        if (payTransfer == null || Boolean.TRUE.equals(payTransfer.getIsDeleted())) {
            throw new ServiceException("转账单不存在");
        }
        if (!Objects.equals(payTransfer.getStatus(), TRANSFER_STATUS_SUCCESS)
                && !Objects.equals(payTransfer.getStatus(), TRANSFER_STATUS_CLOSED)) {
            throw new ServiceException("更新示例提现单状态失败，支付转账单状态不是转账成功或转账失败");
        }
        if (!Objects.equals(payTransfer.getPrice(), withdraw.getPrice())) {
            throw new ServiceException("更新示例提现单状态失败，支付转账单金额不匹配");
        }
        if (!Objects.equals(payTransfer.getMerchantTransferId(), String.valueOf(withdraw.getId()))) {
            throw new ServiceException("更新示例提现单状态失败，支付转账单商户订单号不匹配");
        }
        if (!Objects.equals(payTransfer.getChannelCode(), withdraw.getTransferChannelCode())) {
            throw new ServiceException("更新示例提现单状态失败，支付转账单渠道不匹配");
        }
        return payTransfer;
    }

    private String getTransferChannelCode(Integer type) {
        if (PayDemoWithdrawTypeEnum.ALIPAY.getType().equals(type)) {
            return "alipay_pc";
        }
        if (PayDemoWithdrawTypeEnum.WECHAT.getType().equals(type)) {
            return "wx_lite";
        }
        if (PayDemoWithdrawTypeEnum.WALLET.getType().equals(type)) {
            return "wallet";
        }
        throw new ServiceException("未知提现方式");
    }

    private String generateTransferNo() {
        return "T" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

}

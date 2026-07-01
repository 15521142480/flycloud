package com.fly.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.pay.enums.PayWalletBizTypeEnum;
import com.fly.pay.mapper.PayWalletMapper;
import com.fly.pay.service.IPayWalletService;
import com.fly.pay.service.IPayWalletTransactionService;
import com.fly.system.api.pay.domain.PayWallet;
import com.fly.system.api.pay.domain.PayWalletTransaction;
import com.fly.system.api.pay.domain.bo.PayWalletBo;
import com.fly.system.api.pay.domain.bo.WalletTransactionCreateReqBo;
import com.fly.system.api.pay.domain.vo.PayWalletRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 支付钱包 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PayWalletServiceImpl implements IPayWalletService {

    private final PayWalletMapper payWalletMapper;

    private final ObjectProvider<IPayWalletTransactionService> payWalletTransactionServiceProvider;

    /**
     * 获取或创建用户钱包。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized PayWallet getOrCreateWallet(Long userId, Integer userType) {
        PayWallet wallet = selectByUserIdAndType(userId, userType);
        if (wallet != null) {
            return wallet;
        }
        LocalDateTime now = LocalDateTime.now();
        wallet = new PayWallet();
        wallet.setUserId(userId);
        wallet.setUserType(userType);
        wallet.setBalance(0);
        wallet.setFreezePrice(0);
        wallet.setTotalExpense(0);
        wallet.setTotalRecharge(0);
        wallet.setIsDeleted(false);
        wallet.setCreateBy(String.valueOf(userId));
        wallet.setCreateTime(now);
        wallet.setUpdateBy(String.valueOf(userId));
        wallet.setUpdateTime(now);
        payWalletMapper.insert(wallet);
        return wallet;
    }

    /**
     * 根据编号查询钱包。
     */
    @Override
    public PayWallet getWallet(Long id) {
        if (id == null) {
            return null;
        }
        PayWallet wallet = payWalletMapper.selectById(id);
        return wallet == null || Boolean.TRUE.equals(wallet.getIsDeleted()) ? null : wallet;
    }

    /**
     * 分页查询钱包。
     */
    @Override
    public PageVo<PayWalletRespVo> queryPageList(PayWalletBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PayWallet> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayWallet::getId);
        Page<PayWalletRespVo> page = payWalletMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<PayWalletRespVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 扣减钱包余额并生成流水。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayWalletTransaction reduceWalletBalance(Long walletId, String bizId, PayWalletBizTypeEnum bizType, Integer price) {
        if (price == null || price <= 0) {
            throw new ServiceException("钱包扣减金额必须大于 0");
        }
        PayWallet wallet = getWallet(walletId);
        if (wallet == null) {
            throw new ServiceException("钱包不存在");
        }
        if (PayWalletBizTypeEnum.RECHARGE_REFUND.equals(bizType)) {
            if (wallet.getFreezePrice() == null || wallet.getFreezePrice() < price) {
                throw new ServiceException("钱包冻结金额不足");
            }
            PayWallet updateWallet = new PayWallet();
            updateWallet.setId(walletId);
            updateWallet.setFreezePrice(wallet.getFreezePrice() - price);
            updateWallet.setUpdateBy(String.valueOf(wallet.getUserId()));
            updateWallet.setUpdateTime(LocalDateTime.now());
            payWalletMapper.updateById(updateWallet);
            return createTransaction(walletId, bizId, bizType, -price, wallet.getBalance());
        }
        if (wallet.getBalance() == null || wallet.getBalance() < price) {
            throw new ServiceException("钱包余额不足");
        }
        Integer afterBalance = wallet.getBalance() - price;
        PayWallet updateWallet = new PayWallet();
        updateWallet.setId(walletId);
        updateWallet.setBalance(afterBalance);
        updateWallet.setTotalExpense((wallet.getTotalExpense() == null ? 0 : wallet.getTotalExpense()) + price);
        updateWallet.setUpdateBy(String.valueOf(wallet.getUserId()));
        updateWallet.setUpdateTime(LocalDateTime.now());
        payWalletMapper.updateById(updateWallet);
        return createTransaction(walletId, bizId, bizType, -price, afterBalance);
    }

    /**
     * 增加钱包余额并生成流水。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayWalletTransaction addWalletBalance(Long walletId, String bizId, PayWalletBizTypeEnum bizType, Integer price) {
        if (price == null || price <= 0) {
            throw new ServiceException("钱包增加金额必须大于 0");
        }
        PayWallet wallet = getWallet(walletId);
        if (wallet == null) {
            throw new ServiceException("钱包不存在");
        }
        Integer afterBalance = (wallet.getBalance() == null ? 0 : wallet.getBalance()) + price;
        PayWallet updateWallet = new PayWallet();
        updateWallet.setId(walletId);
        updateWallet.setBalance(afterBalance);
        if (PayWalletBizTypeEnum.RECHARGE.equals(bizType)) {
            updateWallet.setTotalRecharge((wallet.getTotalRecharge() == null ? 0 : wallet.getTotalRecharge()) + price);
        }
        updateWallet.setUpdateBy(String.valueOf(wallet.getUserId()));
        updateWallet.setUpdateTime(LocalDateTime.now());
        payWalletMapper.updateById(updateWallet);
        return createTransaction(walletId, bizId, bizType, price, afterBalance);
    }

    /**
     * 冻结钱包金额。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezePrice(Long walletId, Integer price) {
        PayWallet wallet = getWallet(walletId);
        if (wallet == null) {
            throw new ServiceException("钱包不存在");
        }
        if (wallet.getBalance() == null || wallet.getBalance() < price) {
            throw new ServiceException("钱包余额不足");
        }
        PayWallet updateWallet = new PayWallet();
        updateWallet.setId(walletId);
        updateWallet.setBalance(wallet.getBalance() - price);
        updateWallet.setFreezePrice((wallet.getFreezePrice() == null ? 0 : wallet.getFreezePrice()) + price);
        updateWallet.setUpdateBy(String.valueOf(wallet.getUserId()));
        updateWallet.setUpdateTime(LocalDateTime.now());
        payWalletMapper.updateById(updateWallet);
    }

    /**
     * 解冻钱包金额。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfreezePrice(Long walletId, Integer price) {
        PayWallet wallet = getWallet(walletId);
        if (wallet == null) {
            throw new ServiceException("钱包不存在");
        }
        if (wallet.getFreezePrice() == null || wallet.getFreezePrice() < price) {
            throw new ServiceException("钱包冻结金额不足");
        }
        PayWallet updateWallet = new PayWallet();
        updateWallet.setId(walletId);
        updateWallet.setBalance((wallet.getBalance() == null ? 0 : wallet.getBalance()) + price);
        updateWallet.setFreezePrice(wallet.getFreezePrice() - price);
        updateWallet.setUpdateBy(String.valueOf(wallet.getUserId()));
        updateWallet.setUpdateTime(LocalDateTime.now());
        payWalletMapper.updateById(updateWallet);
    }

    /**
     * 根据用户和类型查询钱包。
     */
    private PayWallet selectByUserIdAndType(Long userId, Integer userType) {
        LambdaQueryWrapper<PayWallet> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWallet::getIsDeleted, false);
        lqw.eq(PayWallet::getUserId, userId);
        lqw.eq(PayWallet::getUserType, userType);
        lqw.last("LIMIT 1");
        return payWalletMapper.selectOne(lqw);
    }

    /**
     * 构建钱包查询条件。
     */
    private LambdaQueryWrapper<PayWallet> buildQueryWrapper(PayWalletBo bo) {
        LambdaQueryWrapper<PayWallet> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWallet::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, PayWallet::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, PayWallet::getUserId, bo.getUserId());
        lqw.eq(bo.getUserType() != null, PayWallet::getUserType, bo.getUserType());
        return lqw;
    }

    /**
     * 创建钱包流水。
     */
    private PayWalletTransaction createTransaction(Long walletId, String bizId, PayWalletBizTypeEnum bizType,
                                                   Integer price, Integer balance) {
        WalletTransactionCreateReqBo transactionBo = new WalletTransactionCreateReqBo();
        transactionBo.setWalletId(walletId);
        transactionBo.setBizId(bizId);
        transactionBo.setBizType(bizType.getType());
        transactionBo.setTitle(bizType.getDescription());
        transactionBo.setPrice(price);
        transactionBo.setBalance(balance);
        return payWalletTransactionServiceProvider.getObject().createWalletTransaction(transactionBo);
    }

}

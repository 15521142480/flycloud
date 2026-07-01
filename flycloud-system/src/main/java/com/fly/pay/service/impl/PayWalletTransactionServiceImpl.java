package com.fly.pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.enums.PayWalletBizTypeEnum;
import com.fly.pay.mapper.PayWalletTransactionMapper;
import com.fly.pay.service.IPayWalletService;
import com.fly.pay.service.IPayWalletTransactionService;
import com.fly.system.api.pay.domain.PayWallet;
import com.fly.system.api.pay.domain.PayWalletTransaction;
import com.fly.system.api.pay.domain.bo.AppPayWalletTransactionPageReqBo;
import com.fly.system.api.pay.domain.bo.PayWalletTransactionBo;
import com.fly.system.api.pay.domain.bo.WalletTransactionCreateReqBo;
import com.fly.system.api.pay.domain.vo.AppPayWalletTransactionRespVo;
import com.fly.system.api.pay.domain.vo.AppPayWalletTransactionSummaryRespVo;
import com.fly.system.api.pay.domain.vo.PayWalletTransactionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 支付钱包流水 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PayWalletTransactionServiceImpl implements IPayWalletTransactionService {

    private static final String WALLET_NO_PREFIX = "W";

    @Lazy
    private final IPayWalletService payWalletService;

    private final PayWalletTransactionMapper payWalletTransactionMapper;

    /**
     * 查询移动端钱包流水分页。
     */
    @Override
    public PageVo<AppPayWalletTransactionRespVo> getWalletTransactionPage(Long userId, Integer userType,
                                                                          AppPayWalletTransactionPageReqBo pageBo) {
        PayWallet wallet = payWalletService.getOrCreateWallet(userId, userType);
        LambdaQueryWrapper<PayWalletTransaction> lqw = buildAppQueryWrapper(wallet.getId(), pageBo);
        lqw.orderByDesc(PayWalletTransaction::getId);
        Page<AppPayWalletTransactionRespVo> page = payWalletTransactionMapper.selectVoPage(pageBo.build(), lqw,
                AppPayWalletTransactionRespVo.class);
        PageVo<AppPayWalletTransactionRespVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 查询钱包流水统计。
     */
    @Override
    public AppPayWalletTransactionSummaryRespVo getWalletTransactionSummary(Long userId, Integer userType,
                                                                            LocalDateTime[] createTime) {
        PayWallet wallet = payWalletService.getOrCreateWallet(userId, userType);
        AppPayWalletTransactionSummaryRespVo respVo = new AppPayWalletTransactionSummaryRespVo();
        respVo.setTotalExpense(sumPrice(wallet.getId(), AppPayWalletTransactionPageReqBo.TYPE_EXPENSE, createTime));
        respVo.setTotalIncome(sumPrice(wallet.getId(), AppPayWalletTransactionPageReqBo.TYPE_INCOME, createTime));
        return respVo;
    }

    /**
     * 创建钱包流水。
     */
    @Override
    public PayWalletTransaction createWalletTransaction(WalletTransactionCreateReqBo bo) {
        PayWalletTransaction transaction = BeanUtil.toBean(bo, PayWalletTransaction.class);
        transaction.setNo(generateWalletNo());
        transaction.setIsDeleted(false);
        transaction.setCreateBy(String.valueOf(bo.getWalletId()));
        transaction.setCreateTime(LocalDateTime.now());
        transaction.setUpdateBy(String.valueOf(bo.getWalletId()));
        transaction.setUpdateTime(LocalDateTime.now());
        payWalletTransactionMapper.insert(transaction);
        return transaction;
    }

    /**
     * 根据业务查询钱包流水。
     */
    @Override
    public PayWalletTransaction getWalletTransaction(String bizId, PayWalletBizTypeEnum bizType) {
        LambdaQueryWrapper<PayWalletTransaction> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletTransaction::getIsDeleted, false);
        lqw.eq(PayWalletTransaction::getBizId, bizId);
        lqw.eq(PayWalletTransaction::getBizType, bizType.getType());
        lqw.last("LIMIT 1");
        return payWalletTransactionMapper.selectOne(lqw);
    }

    /**
     * 分页查询钱包流水。
     */
    @Override
    public PageVo<PayWalletTransactionVo> queryPageList(PayWalletTransactionBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PayWalletTransaction> lqw = buildAdminQueryWrapper(bo);
        lqw.orderByDesc(PayWalletTransaction::getId);
        Page<PayWalletTransactionVo> page = payWalletTransactionMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<PayWalletTransactionVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 构建移动端钱包流水查询条件。
     */
    private LambdaQueryWrapper<PayWalletTransaction> buildAppQueryWrapper(Long walletId,
                                                                          AppPayWalletTransactionPageReqBo pageBo) {
        LambdaQueryWrapper<PayWalletTransaction> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletTransaction::getIsDeleted, false);
        lqw.eq(PayWalletTransaction::getWalletId, walletId);
        if (pageBo == null) {
            return lqw;
        }
        if (AppPayWalletTransactionPageReqBo.TYPE_INCOME.equals(pageBo.getType())) {
            lqw.gt(PayWalletTransaction::getPrice, 0);
        } else if (AppPayWalletTransactionPageReqBo.TYPE_EXPENSE.equals(pageBo.getType())) {
            lqw.lt(PayWalletTransaction::getPrice, 0);
        }
        betweenCreateTime(lqw, pageBo.getCreateTime());
        return lqw;
    }

    /**
     * 构建后台钱包流水查询条件。
     */
    private LambdaQueryWrapper<PayWalletTransaction> buildAdminQueryWrapper(PayWalletTransactionBo bo) {
        LambdaQueryWrapper<PayWalletTransaction> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletTransaction::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        Long walletId = bo.getWalletId();
        if (walletId == null && bo.getUserId() != null && bo.getUserType() != null) {
            PayWallet wallet = payWalletService.getOrCreateWallet(bo.getUserId(), bo.getUserType());
            walletId = wallet.getId();
        }
        lqw.eq(walletId != null, PayWalletTransaction::getWalletId, walletId);
        lqw.eq(bo.getBizType() != null, PayWalletTransaction::getBizType, bo.getBizType());
        betweenCreateTime(lqw, bo.getCreateTimeRange());
        return lqw;
    }

    /**
     * 查询指定时间范围的钱包流水金额合计。
     */
    private Integer sumPrice(Long walletId, Integer type, LocalDateTime[] createTime) {
        LambdaQueryWrapper<PayWalletTransaction> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletTransaction::getIsDeleted, false);
        lqw.eq(PayWalletTransaction::getWalletId, walletId);
        if (AppPayWalletTransactionPageReqBo.TYPE_INCOME.equals(type)) {
            lqw.gt(PayWalletTransaction::getPrice, 0);
        } else if (AppPayWalletTransactionPageReqBo.TYPE_EXPENSE.equals(type)) {
            lqw.lt(PayWalletTransaction::getPrice, 0);
        }
        betweenCreateTime(lqw, createTime);
        return payWalletTransactionMapper.selectList(lqw).stream()
                .map(PayWalletTransaction::getPrice)
                .filter(price -> price != null)
                .mapToInt(price -> Math.abs(price))
                .sum();
    }

    /**
     * 添加创建时间范围查询。
     */
    private void betweenCreateTime(LambdaQueryWrapper<PayWalletTransaction> lqw, LocalDateTime[] createTime) {
        if (createTime != null && createTime.length == 2 && createTime[0] != null && createTime[1] != null) {
            lqw.between(PayWalletTransaction::getCreateTime, createTime[0], createTime[1]);
        }
    }

    /**
     * 生成钱包流水号。
     */
    private String generateWalletNo() {
        return WALLET_NO_PREFIX + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

}

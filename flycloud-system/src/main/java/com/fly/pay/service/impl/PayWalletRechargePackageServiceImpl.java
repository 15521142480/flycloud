package com.fly.pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.pay.mapper.PayWalletRechargePackageMapper;
import com.fly.pay.service.IPayWalletRechargePackageService;
import com.fly.system.api.pay.domain.PayWalletRechargePackage;
import com.fly.system.api.pay.domain.bo.PayWalletRechargePackageBo;
import com.fly.system.api.pay.domain.vo.PayWalletRechargePackageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付钱包充值套餐 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class PayWalletRechargePackageServiceImpl implements IPayWalletRechargePackageService {

    /**
     * 启用状态。
     */
    private static final int STATUS_ENABLE = 0;

    private final PayWalletRechargePackageMapper walletRechargePackageMapper;

    /**
     * 查询充值套餐。
     */
    @Override
    public PayWalletRechargePackage getWalletRechargePackage(Long id) {
        if (id == null) {
            return null;
        }
        PayWalletRechargePackage rechargePackage = walletRechargePackageMapper.selectById(id);
        return rechargePackage == null || Boolean.TRUE.equals(rechargePackage.getIsDeleted()) ? null : rechargePackage;
    }

    /**
     * 校验并查询启用的充值套餐。
     */
    @Override
    public PayWalletRechargePackage validWalletRechargePackage(Long id) {
        PayWalletRechargePackage rechargePackage = getWalletRechargePackage(id);
        if (rechargePackage == null) {
            throw new ServiceException("钱包充值套餐不存在");
        }
        if (!Integer.valueOf(STATUS_ENABLE).equals(rechargePackage.getStatus())) {
            throw new ServiceException("钱包充值套餐已禁用");
        }
        return rechargePackage;
    }

    /**
     * 查询启用充值套餐列表。
     */
    @Override
    public List<PayWalletRechargePackageVo> getWalletRechargePackageList(Integer status) {
        LambdaQueryWrapper<PayWalletRechargePackage> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletRechargePackage::getIsDeleted, false);
        lqw.eq(status != null, PayWalletRechargePackage::getStatus, status);
        lqw.orderByAsc(PayWalletRechargePackage::getPayPrice);
        return walletRechargePackageMapper.selectVoList(lqw);
    }

    /**
     * 分页查询充值套餐。
     */
    @Override
    public PageVo<PayWalletRechargePackageVo> queryPageList(PayWalletRechargePackageBo bo, PageBo pageBo) {
        LambdaQueryWrapper<PayWalletRechargePackage> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(PayWalletRechargePackage::getPayPrice);
        Page<PayWalletRechargePackageVo> page = walletRechargePackageMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<PayWalletRechargePackageVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    /**
     * 新增或修改充值套餐。
     */
    @Override
    public Boolean saveOrUpdate(PayWalletRechargePackageBo bo) {
        validateNameUnique(bo.getId(), bo.getName());
        PayWalletRechargePackage rechargePackage = BeanUtil.toBean(bo, PayWalletRechargePackage.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        rechargePackage.setUpdateBy(userId);
        rechargePackage.setUpdateTime(now);
        if (rechargePackage.getIsDeleted() == null) {
            rechargePackage.setIsDeleted(false);
        }
        if (rechargePackage.getStatus() == null) {
            rechargePackage.setStatus(STATUS_ENABLE);
        }
        if (rechargePackage.getId() != null) {
            validateExists(rechargePackage.getId());
            return walletRechargePackageMapper.updateById(rechargePackage) > 0;
        }
        rechargePackage.setCreateBy(userId);
        rechargePackage.setCreateTime(now);
        return walletRechargePackageMapper.insert(rechargePackage) > 0;
    }

    /**
     * 新增充值套餐并返回编号。
     */
    @Override
    public Long createWalletRechargePackage(PayWalletRechargePackageBo bo) {
        validateNameUnique(null, bo.getName());
        PayWalletRechargePackage rechargePackage = BeanUtil.toBean(bo, PayWalletRechargePackage.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        rechargePackage.setIsDeleted(false);
        if (rechargePackage.getStatus() == null) {
            rechargePackage.setStatus(STATUS_ENABLE);
        }
        rechargePackage.setCreateBy(userId);
        rechargePackage.setCreateTime(now);
        rechargePackage.setUpdateBy(userId);
        rechargePackage.setUpdateTime(now);
        walletRechargePackageMapper.insert(rechargePackage);
        return rechargePackage.getId();
    }

    /**
     * 删除充值套餐。
     */
    @Override
    public Boolean deleteById(Long id) {
        validateExists(id);
        PayWalletRechargePackage rechargePackage = new PayWalletRechargePackage();
        rechargePackage.setId(id);
        rechargePackage.setIsDeleted(true);
        rechargePackage.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        rechargePackage.setUpdateTime(LocalDateTime.now());
        return walletRechargePackageMapper.updateById(rechargePackage) > 0;
    }

    /**
     * 构建充值套餐查询条件。
     */
    private LambdaQueryWrapper<PayWalletRechargePackage> buildQueryWrapper(PayWalletRechargePackageBo bo) {
        LambdaQueryWrapper<PayWalletRechargePackage> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletRechargePackage::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, PayWalletRechargePackage::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), PayWalletRechargePackage::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, PayWalletRechargePackage::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 校验充值套餐名称唯一。
     */
    private void validateNameUnique(Long id, String name) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        LambdaQueryWrapper<PayWalletRechargePackage> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayWalletRechargePackage::getIsDeleted, false);
        lqw.eq(PayWalletRechargePackage::getName, name);
        lqw.last("LIMIT 1");
        PayWalletRechargePackage rechargePackage = walletRechargePackageMapper.selectOne(lqw);
        if (rechargePackage != null && !rechargePackage.getId().equals(id)) {
            throw new ServiceException("钱包充值套餐名称已存在");
        }
    }

    /**
     * 校验充值套餐存在。
     */
    private void validateExists(Long id) {
        if (getWalletRechargePackage(id) == null) {
            throw new ServiceException("钱包充值套餐不存在");
        }
    }

}

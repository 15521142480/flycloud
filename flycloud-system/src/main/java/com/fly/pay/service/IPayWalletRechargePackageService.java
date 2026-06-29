package com.fly.pay.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.system.api.pay.domain.PayWalletRechargePackage;
import com.fly.system.api.pay.domain.bo.PayWalletRechargePackageBo;
import com.fly.system.api.pay.domain.vo.PayWalletRechargePackageVo;

import java.util.List;

/**
 * 支付钱包充值套餐 Service 接口。
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface IPayWalletRechargePackageService {

    /**
     * 查询充值套餐。
     */
    PayWalletRechargePackage getWalletRechargePackage(Long id);

    /**
     * 校验并查询启用的充值套餐。
     */
    PayWalletRechargePackage validWalletRechargePackage(Long id);

    /**
     * 查询启用充值套餐列表。
     */
    List<PayWalletRechargePackageVo> getWalletRechargePackageList(Integer status);

    /**
     * 分页查询充值套餐。
     */
    PageVo<PayWalletRechargePackageVo> queryPageList(PayWalletRechargePackageBo bo, PageBo pageBo);

    /**
     * 新增或修改充值套餐。
     */
    Boolean saveOrUpdate(PayWalletRechargePackageBo bo);

    /**
     * 删除充值套餐。
     */
    Boolean deleteById(Long id);

}

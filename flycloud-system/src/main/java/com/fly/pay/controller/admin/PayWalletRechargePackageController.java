package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayWalletRechargePackageService;
import com.fly.system.api.pay.domain.bo.PayWalletRechargePackageBo;
import com.fly.system.api.pay.domain.vo.PayWalletRechargePackageVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付钱包充值套餐控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/wallet-recharge-package")
public class PayWalletRechargePackageController {

    private final IPayWalletRechargePackageService rechargePackageService;

    /**
     * 查询支付钱包充值套餐分页列表。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge-package:list')")
    @GetMapping("/list")
    public R<PageVo<PayWalletRechargePackageVo>> list(PayWalletRechargePackageBo bo, PageBo pageBo) {
        return R.ok(rechargePackageService.queryPageList(bo, pageBo));
    }

    /**
     * 查询支付钱包充值套餐分页列表，兼容 yudao 前端接口。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge-package:list')")
    @GetMapping("/page")
    public R<PageVo<PayWalletRechargePackageVo>> page(PayWalletRechargePackageBo bo, PageBo pageBo) {
        return R.ok(rechargePackageService.queryPageList(bo, pageBo));
    }

    /**
     * 获取支付钱包充值套餐详情。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge-package:query')")
    @GetMapping("/get")
    public R<PayWalletRechargePackageVo> get(@RequestParam("id") Long id) {
        return R.ok(cn.hutool.core.bean.BeanUtil.toBean(rechargePackageService.getWalletRechargePackage(id),
                PayWalletRechargePackageVo.class));
    }

    /**
     * 新增或修改支付钱包充值套餐。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge-package:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody PayWalletRechargePackageBo bo) {
        return R.ok(rechargePackageService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody PayWalletRechargePackageBo bo) {
        return R.ok(rechargePackageService.saveOrUpdate(bo));
    }

    /**
     * 删除支付钱包充值套餐。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge-package:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(rechargePackageService.deleteById(id));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(rechargePackageService.deleteById(id));
    }

}

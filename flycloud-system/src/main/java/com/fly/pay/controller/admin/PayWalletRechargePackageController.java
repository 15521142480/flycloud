package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayWalletRechargePackageService;
import com.fly.system.api.pay.domain.bo.PayWalletRechargePackageBo;
import com.fly.system.api.pay.domain.vo.PayWalletRechargePackageVo;
import com.fly.system.api.pay.domain.vo.WalletRechargePackageRespVo;
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
 * @date 2026-07-02
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
    public R<PageVo<WalletRechargePackageRespVo>> list(PayWalletRechargePackageBo bo, PageBo pageBo) {
        return R.ok(convertPage(rechargePackageService.queryPageList(bo, pageBo)));
    }

    /**
     * 查询支付钱包充值套餐分页列表，兼容 yudao 前端接口。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge-package:list')")
    @GetMapping("/page")
    public R<PageVo<WalletRechargePackageRespVo>> page(PayWalletRechargePackageBo bo, PageBo pageBo) {
        return R.ok(convertPage(rechargePackageService.queryPageList(bo, pageBo)));
    }

    /**
     * 获取支付钱包充值套餐详情。
     */
//    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge-package:query')")
    @GetMapping("/get")
    public R<WalletRechargePackageRespVo> get(@RequestParam("id") Long id) {
        return R.ok(cn.hutool.core.bean.BeanUtil.toBean(rechargePackageService.getWalletRechargePackage(id),
                WalletRechargePackageRespVo.class));
    }

    /**
     * 新增或修改支付钱包充值套餐。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge-package:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Long> saveOrUpdate(@RequestBody PayWalletRechargePackageBo bo) {
        if (bo.getId() == null) {
            return R.ok(rechargePackageService.createWalletRechargePackage(bo));
        }
        rechargePackageService.saveOrUpdate(bo);
        return R.ok(bo.getId());
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody PayWalletRechargePackageBo bo) {
        return R.result(rechargePackageService.saveOrUpdate(bo));
    }

    /**
     * 删除支付钱包充值套餐。
     */
    @PreAuthorize("@pms.hasPermission('pay:wallet-recharge-package:delete')")
    @DeleteMapping("/delete/{id}")
    public R<Boolean> remove(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.result(rechargePackageService.deleteById(id));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(rechargePackageService.deleteById(id));
    }

    /**
     * 转换充值套餐分页响应对象。
     */
    private PageVo<WalletRechargePackageRespVo> convertPage(PageVo<PayWalletRechargePackageVo> page) {
        PageVo<WalletRechargePackageRespVo> respPage = new PageVo<>();
        respPage.setList(cn.hutool.core.bean.BeanUtil.copyToList(page.getList(), WalletRechargePackageRespVo.class));
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

}

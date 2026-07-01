package com.fly.pay.controller.app;

import com.fly.common.domain.model.R;
import com.fly.pay.service.IPayWalletRechargePackageService;
import com.fly.system.api.pay.domain.vo.AppPayWalletPackageRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端 - 钱包充值套餐控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/pay/wallet-recharge-package")
public class AppPayWalletRechargePackageController {

    private static final int STATUS_ENABLE = 0;

    private final IPayWalletRechargePackageService rechargePackageService;

    /**
     * 查询启用的钱包充值套餐列表。
     */
    @GetMapping("/list")
    public R<List<AppPayWalletPackageRespVo>> list() {
        return R.ok(cn.hutool.core.bean.BeanUtil.copyToList(
                rechargePackageService.getWalletRechargePackageList(STATUS_ENABLE), AppPayWalletPackageRespVo.class));
    }

}

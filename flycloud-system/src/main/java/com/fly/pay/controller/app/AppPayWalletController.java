package com.fly.pay.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.pay.service.IPayWalletService;
import com.fly.system.api.pay.domain.PayWallet;
import com.fly.system.api.pay.domain.vo.AppPayWalletRespVo;
import com.fly.system.api.pay.domain.vo.PayWalletRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 支付钱包控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/pay/wallet")
public class AppPayWalletController {

    private static final int USER_TYPE_MEMBER = 2;

    private final IPayWalletService payWalletService;

    /**
     * 获取当前用户钱包。
     */
    @GetMapping("/get")
    public R<AppPayWalletRespVo> getPayWallet() {
        PayWallet walletEntity = payWalletService.getOrCreateWallet(UserUtils.getCurUserId(), USER_TYPE_MEMBER);
        AppPayWalletRespVo wallet = new AppPayWalletRespVo();
        wallet.setId(walletEntity.getId());
        wallet.setUserId(walletEntity.getUserId());
        wallet.setUserType(walletEntity.getUserType());
        wallet.setBalance(walletEntity.getBalance());
        wallet.setFreezePrice(walletEntity.getFreezePrice());
        wallet.setTotalExpense(walletEntity.getTotalExpense());
        wallet.setTotalRecharge(walletEntity.getTotalRecharge());
        return R.ok(wallet);
    }

}

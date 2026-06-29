package com.fly.pay.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.security.util.UserUtils;
import com.fly.system.api.pay.domain.vo.PayWalletRespVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 支付钱包控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RestController
@RequestMapping("/app/pay/wallet")
public class AppPayWalletController {

    /**
     * 获取当前用户钱包。
     */
    @GetMapping("/get")
    public R<PayWalletRespVo> getPayWallet() {
        PayWalletRespVo wallet = new PayWalletRespVo();
        wallet.setId(UserUtils.getCurUserId());
        wallet.setUserId(UserUtils.getCurUserId());
        wallet.setUserType(2);
        wallet.setBalance(0);
        wallet.setFreezePrice(0);
        wallet.setTotalExpense(0);
        wallet.setTotalRecharge(0);
        return R.ok(wallet);
    }

}

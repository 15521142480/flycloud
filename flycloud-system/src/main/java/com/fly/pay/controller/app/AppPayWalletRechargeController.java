package com.fly.pay.controller.app;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.pay.service.IPayWalletRechargeService;
import com.fly.system.api.pay.domain.bo.AppPayWalletRechargeCreateReqVo;
import com.fly.system.api.pay.domain.vo.AppPayWalletRechargeCreateRespVo;
import com.fly.system.api.pay.domain.vo.AppPayWalletRechargeRespVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 移动端 - 钱包充值控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/pay/wallet-recharge")
public class AppPayWalletRechargeController {

    private static final int USER_TYPE_MEMBER = 2;

    private final IPayWalletRechargeService walletRechargeService;

    /**
     * 创建钱包充值记录。
     */
    @PostMapping("/create")
    public R<AppPayWalletRechargeCreateRespVo> createWalletRecharge(@RequestBody AppPayWalletRechargeCreateReqVo reqVo,
                                                                    HttpServletRequest request) {
        return R.ok(walletRechargeService.createWalletRecharge(UserUtils.getCurUserId(), USER_TYPE_MEMBER,
                getClientIp(request), reqVo));
    }

    /**
     * 查询钱包充值记录分页。
     */
    @GetMapping("/page")
    public R<PageVo<AppPayWalletRechargeRespVo>> page(PageBo pageBo) {
        return R.ok(walletRechargeService.getAppWalletRechargePage(UserUtils.getCurUserId(), USER_TYPE_MEMBER, pageBo));
    }

    /**
     * 获取客户端 IP。
     */
    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

}

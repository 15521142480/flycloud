package com.fly.pay.controller.app;

import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.pay.service.IPayWalletTransactionService;
import com.fly.system.api.pay.domain.bo.AppPayWalletTransactionPageReqBo;
import com.fly.system.api.pay.domain.vo.AppPayWalletTransactionRespVo;
import com.fly.system.api.pay.domain.vo.AppPayWalletTransactionSummaryRespVo;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 移动端 - 钱包流水控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/pay/wallet-transaction")
public class AppPayWalletTransactionController {

    private static final int USER_TYPE_MEMBER = 2;

    private final IPayWalletTransactionService walletTransactionService;

    /**
     * 查询钱包流水分页。
     */
    @GetMapping("/page")
    public R<PageVo<AppPayWalletTransactionRespVo>> page(AppPayWalletTransactionPageReqBo pageBo) {
        return R.ok(walletTransactionService.getWalletTransactionPage(UserUtils.getCurUserId(), USER_TYPE_MEMBER, pageBo));
    }

    /**
     * 查询钱包流水统计。
     */
    @GetMapping("/get-summary")
    public R<AppPayWalletTransactionSummaryRespVo> getSummary(
            @RequestParam("createTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime[] createTime) {
        return R.ok(walletTransactionService.getWalletTransactionSummary(UserUtils.getCurUserId(), USER_TYPE_MEMBER,
                createTime));
    }

}

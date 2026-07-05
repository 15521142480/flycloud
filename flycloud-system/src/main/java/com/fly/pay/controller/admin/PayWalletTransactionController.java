package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayWalletTransactionService;
import com.fly.system.api.pay.domain.bo.PayWalletTransactionBo;
import com.fly.system.api.pay.domain.vo.PayWalletTransactionRespVo;
import com.fly.system.api.pay.domain.vo.PayWalletTransactionVo;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付钱包流水控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/wallet-transaction")
public class PayWalletTransactionController {

    private final IPayWalletTransactionService walletTransactionService;

    /**
     * 查询支付钱包流水分页列表。
     */
//    @PreAuthorize("@pms.hasPermission('pay:wallet-transaction:list')")
    @GetMapping("/list")
    public R<PageVo<PayWalletTransactionRespVo>> list(PayWalletTransactionBo bo, PageBo pageBo) {
        return R.ok(convertPage(walletTransactionService.queryPageList(bo, pageBo)));
    }

    /**
     * 查询支付钱包流水分页列表，兼容 yudao 前端接口。
     */
    @GetMapping("/page")
    public R<PageVo<PayWalletTransactionRespVo>> page(PayWalletTransactionBo bo, PageBo pageBo) {
        return R.ok(convertPage(walletTransactionService.queryPageList(bo, pageBo)));
    }

    /**
     * 转换钱包流水分页响应对象。
     */
    private PageVo<PayWalletTransactionRespVo> convertPage(PageVo<PayWalletTransactionVo> page) {
        PageVo<PayWalletTransactionRespVo> respPage = new PageVo<>();
        respPage.setList(BeanUtil.copyToList(page.getList(), PayWalletTransactionRespVo.class));
        respPage.setTotal(page.getTotal());
        respPage.setPages(page.getPages());
        return respPage;
    }

}

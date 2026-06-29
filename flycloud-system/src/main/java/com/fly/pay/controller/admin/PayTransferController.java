package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayTransferService;
import com.fly.system.api.pay.domain.bo.PayTransferBo;
import com.fly.system.api.pay.domain.vo.PayTransferVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付转账单控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/pay/transfer")
public class PayTransferController {

    private final IPayTransferService payTransferService;

    @PreAuthorize("@pms.hasPermission('pay:transfer:list')")
    @GetMapping("/page")
    public R<PageVo<PayTransferVo>> page(PayTransferBo bo, PageBo pageBo) {
        return R.ok(payTransferService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('pay:transfer:query')")
    @GetMapping("/get")
    public R<PayTransferVo> get(@RequestParam Long id) {
        return R.ok(payTransferService.queryById(id));
    }

}

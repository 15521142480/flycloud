package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.utils.ExcelUtil;
import com.fly.pay.service.IPayRefundService;
import com.fly.system.api.pay.domain.bo.PayRefundBo;
import com.fly.system.api.pay.domain.vo.PayRefundVo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台 - 支付退款单控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/refund")
public class PayRefundController {

    private final IPayRefundService payRefundService;

    @PreAuthorize("@pms.hasPermission('pay:refund:list')")
    @GetMapping("/page")
    public R<PageVo<PayRefundVo>> page(PayRefundBo bo, PageBo pageBo) {
        return R.ok(payRefundService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('pay:refund:query')")
    @GetMapping("/get")
    public R<PayRefundVo> get(@RequestParam Long id) {
        return R.ok(payRefundService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('pay:refund:download')")
    @GetMapping("/export-excel")
    public void export(PayRefundBo bo, HttpServletResponse response) {
        ExcelUtil.exportExcel(payRefundService.queryList(bo), "支付退款单", PayRefundVo.class, response);
    }

}

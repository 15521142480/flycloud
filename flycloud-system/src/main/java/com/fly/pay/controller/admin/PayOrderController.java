package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayOrderService;
import com.fly.system.api.pay.domain.bo.PayOrderBo;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 后台 - 支付订单控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/pay/order")
public class PayOrderController {

    private final IPayOrderService payOrderService;

    /**
     * 查询支付订单分页列表。
     */
    @PreAuthorize("@pms.hasPermission('pay:order:list')")
    @GetMapping("/list")
    public R<PageVo<PayOrderRespVo>> list(PayOrderBo bo, PageBo page) {
        return R.ok(payOrderService.queryPageList(bo, page));
    }

    /**
     * 查询支付订单列表。
     */
    @PreAuthorize("@pms.hasPermission('pay:order:list')")
    @GetMapping("/allList")
    public R<List<PayOrderRespVo>> allList(PayOrderBo bo) {
        return R.ok(payOrderService.queryList(bo));
    }

    /**
     * 获取支付订单详情。
     */
    @PreAuthorize("@pms.hasPermission('pay:order:query')")
    @GetMapping("/get/{id}")
    public R<PayOrderRespVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(payOrderService.getOrder(id));
    }

}

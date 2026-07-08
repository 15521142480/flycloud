package com.fly.pay.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.ExcelUtil;
import com.fly.pay.service.IPayAppService;
import com.fly.pay.service.IPayOrderService;
import com.fly.system.api.pay.domain.PayOrderExtension;
import com.fly.system.api.pay.domain.bo.AppPayOrderSubmitReqVo;
import com.fly.system.api.pay.domain.bo.PayOrderBo;
import com.fly.system.api.pay.domain.vo.AppPayOrderSubmitRespVo;
import com.fly.system.api.pay.domain.vo.PayAppVo;
import com.fly.system.api.pay.domain.vo.PayOrderDetailsRespVo;
import com.fly.system.api.pay.domain.vo.PayOrderRespVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 后台 - 支付订单控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/order")
public class PayOrderController {

    private static final int ORDER_STATUS_WAITING = 0;

    private final IPayOrderService payOrderService;
    private final IPayAppService payAppService;

    /**
     * 查询支付订单分页列表。
     */
    @PreAuthorize("@pms.hasPermission('pay:order:list')")
    @GetMapping("/list")
    public R<PageVo<PayOrderRespVo>> list(PayOrderBo bo, PageBo page) {
        return R.ok(payOrderService.queryPageList(bo, page));
    }

    /**
     * 查询支付订单分页列表，兼容 yudao 前端接口。
     */
    @PreAuthorize("@pms.hasPermission('pay:order:list')")
    @GetMapping("/page")
    public R<PageVo<PayOrderRespVo>> page(PayOrderBo bo, PageBo page) {
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
//    @PreAuthorize("@pms.hasPermission('pay:order:list')")
    @GetMapping("/get/{id}")
    public R<PayOrderRespVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(payOrderService.getOrder(id));
    }

    /**
     * 获取支付订单详情，兼容 yudao 前端接口。
     */
//    @PreAuthorize("@pms.hasPermission('pay:order:list')")
    @GetMapping("/get")
    public R<PayOrderRespVo> get(@RequestParam("id") Long id,
                                 @RequestParam(value = "sync", required = false) Boolean sync) {
        PayOrderRespVo order = payOrderService.getOrder(id);
        if (order != null && Boolean.TRUE.equals(sync) && Objects.equals(order.getStatus(), ORDER_STATUS_WAITING)) {
            payOrderService.syncOrderQuietly(order.getId());
            order = payOrderService.getOrder(id);
        }
        return R.ok(order);
    }

    /**
     * 获取支付订单详情，兼容 yudao 前端接口。
     */
//    @PreAuthorize("@pms.hasPermission('pay:order:list')")
    @GetMapping("/get-detail")
    public R<PayOrderDetailsRespVo> getDetail(@RequestParam("id") Long id) {
        PayOrderRespVo order = payOrderService.getOrder(id);
        if (order == null) {
            return R.ok((PayOrderDetailsRespVo) null);
        }
        PayOrderDetailsRespVo detail = BeanUtil.toBean(order, PayOrderDetailsRespVo.class);
        PayAppVo app = payAppService.queryById(order.getAppId());
        if (app != null) {
            detail.setAppName(app.getName());
        }
        PayOrderExtension extension = payOrderService.getOrderExtension(order.getExtensionId());
        if (extension != null) {
            PayOrderDetailsRespVo.PayOrderExtensionRespVo extensionRespVo =
                    BeanUtil.toBean(extension, PayOrderDetailsRespVo.PayOrderExtensionRespVo.class);
            detail.setExtension(extensionRespVo);
        }
        return R.ok(detail);
    }

    /**
     * 提交支付订单，兼容 yudao 前端接口。
     */
    @PostMapping("/submit")
    public R<AppPayOrderSubmitRespVo> submit(@RequestBody AppPayOrderSubmitReqVo reqVo,
                                             HttpServletRequest request) {
        return R.ok(payOrderService.submitOrder(UserUtils.getCurUserId(), reqVo, getClientIp(request)));
    }

    /**
     * 导出支付订单，兼容 yudao 前端接口。
     */
    @PreAuthorize("@pms.hasPermission('pay:order:download')")
    @GetMapping("/export-excel")
    public void export(PayOrderBo bo, HttpServletResponse response) {
        ExcelUtil.exportExcel(payOrderService.queryList(bo), "支付订单", PayOrderRespVo.class, response);
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

package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.pay.service.IPayNotifyService;
import com.fly.system.api.pay.domain.bo.PayNotifyTaskBo;
import com.fly.system.api.pay.domain.vo.PayNotifyTaskDetailVo;
import com.fly.system.api.pay.domain.vo.PayNotifyTaskVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 后台 - 支付通知控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/pay/notify")
public class PayNotifyController {

    private final IPayNotifyService notifyService;

    @GetMapping("/get-detail")
    @Operation(summary = "获得回调通知的明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@pms.hasPermission('pay:notify:query')")
    public R<PayNotifyTaskDetailVo> getNotifyTaskDetail(@RequestParam("id") Long id) {
        return R.ok(notifyService.getNotifyTaskDetail(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得回调通知分页")
    @PreAuthorize("@pms.hasPermission('pay:notify:query')")
    public R<PageVo<PayNotifyTaskVo>> getNotifyTaskPage(PayNotifyTaskBo bo, PageBo pageBo) {
        return R.ok(notifyService.queryPageList(bo, pageBo));
    }

    @PostMapping("/execute")
    @Operation(summary = "手动执行到期的支付通知任务")
    @PreAuthorize("@pms.hasPermission('pay:notify:execute')")
    public R<Integer> executeNotify() {
        return R.ok(notifyService.executeNotify());
    }

    @PostMapping(value = "/order/{channelId}")
    @Operation(summary = "支付渠道的统一支付回调")
    @PermitAll
    public String notifyOrder(@PathVariable("channelId") Long channelId,
                              @RequestParam(required = false) Map<String, String> params,
                              @RequestBody(required = false) String body,
                              @RequestHeader Map<String, String> headers) {
        log.info("[notifyOrder][channelId({}) 回调数据({}/{}/{})]", channelId, params, body, headers);
        throw new ServiceException("支付渠道回调解析器未接入");
    }

    @PostMapping(value = "/refund/{channelId}")
    @Operation(summary = "支付渠道的统一退款回调")
    @PermitAll
    public String notifyRefund(@PathVariable("channelId") Long channelId,
                               @RequestParam(required = false) Map<String, String> params,
                               @RequestBody(required = false) String body,
                               @RequestHeader Map<String, String> headers) {
        log.info("[notifyRefund][channelId({}) 回调数据({}/{}/{})]", channelId, params, body, headers);
        throw new ServiceException("支付渠道回调解析器未接入");
    }

    @PostMapping(value = "/transfer/{channelId}")
    @Operation(summary = "支付渠道的统一转账回调")
    @PermitAll
    public String notifyTransfer(@PathVariable("channelId") Long channelId,
                                 @RequestParam(required = false) Map<String, String> params,
                                 @RequestBody(required = false) String body,
                                 @RequestHeader Map<String, String> headers) {
        log.info("[notifyTransfer][channelId({}) 回调数据({}/{}/{})]", channelId, params, body, headers);
        throw new ServiceException("支付渠道回调解析器未接入");
    }

}

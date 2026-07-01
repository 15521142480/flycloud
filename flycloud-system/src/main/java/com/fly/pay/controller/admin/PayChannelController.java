package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayChannelService;
import com.fly.system.api.pay.domain.bo.PayChannelBo;
import com.fly.system.api.pay.domain.vo.PayChannelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 后台 - 支付渠道控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/channel")
public class PayChannelController {

    private final IPayChannelService payChannelService;

    @PreAuthorize("@pms.hasPermission('pay:channel:list')")
    @GetMapping("/page")
    public R<PageVo<PayChannelVo>> page(PayChannelBo bo, PageBo pageBo) {
        return R.ok(payChannelService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('pay:channel:list')")
    @GetMapping("/list")
    public R<List<PayChannelVo>> list(PayChannelBo bo) {
        return R.ok(payChannelService.queryList(bo));
    }

    @PreAuthorize("@pms.hasPermission('pay:channel:query')")
    @GetMapping("/get")
    public R<PayChannelVo> get(@RequestParam Long id) {
        return R.ok(payChannelService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('pay:channel:create')")
    @PostMapping("/create")
    public R<Void> create(@RequestBody PayChannelBo bo) {
        return R.ok(payChannelService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('pay:channel:update')")
    @PutMapping("/update")
    public R<Void> update(@RequestBody PayChannelBo bo) {
        return R.ok(payChannelService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('pay:channel:delete')")
    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam Long id) {
        return R.ok(payChannelService.deleteById(id));
    }

    @PreAuthorize("@pms.hasPermission('pay:channel:list')")
    @GetMapping("/get-enable-code-list")
    public R<Set<String>> getEnableChannelCodeList(@RequestParam Long appId) {
        return R.ok(payChannelService.getEnableChannelCodeList(appId));
    }

}

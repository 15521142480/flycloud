package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayChannelService;
import com.fly.system.api.pay.domain.bo.PayChannelBo;
import com.fly.system.api.pay.domain.vo.PayChannelRespVo;
import com.fly.system.api.pay.domain.vo.PayChannelVo;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 后台 - 支付渠道控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/channel")
public class PayChannelController {

    private final IPayChannelService payChannelService;

//    @PreAuthorize("@pms.hasPermission('pay:channel:list')")
    @GetMapping("/page")
    public R<PageVo<PayChannelVo>> page(PayChannelBo bo, PageBo pageBo) {
        return R.ok(payChannelService.queryPageList(bo, pageBo));
    }

//    @PreAuthorize("@pms.hasPermission('pay:channel:list')")
    @GetMapping("/list")
    public R<List<PayChannelRespVo>> list(PayChannelBo bo) {
        return R.ok(BeanUtil.copyToList(payChannelService.queryList(bo), PayChannelRespVo.class));
    }

    @GetMapping("/get")
    public R<PayChannelRespVo> get(@RequestParam(value = "id", required = false) Long id,
                                   @RequestParam(value = "appId", required = false) Long appId,
                                   @RequestParam(value = "code", required = false) String code) {
        return R.ok(payChannelService.getChannel(id, appId, code));
    }

//    @PreAuthorize("@pms.hasPermission('pay:channel:saveOrUpdate')")
    @PostMapping("/create")
    public R<Long> create(@RequestBody PayChannelBo bo) {
        return R.ok(payChannelService.createChannel(bo));
    }

//    @PreAuthorize("@pms.hasPermission('pay:channel:saveOrUpdate')")
    @PutMapping("/update")
    public R<Boolean> update(@RequestBody PayChannelBo bo) {
        return R.result(payChannelService.saveOrUpdate(bo));
    }

//    @PreAuthorize("@pms.hasPermission('pay:channel:delete')")
    @DeleteMapping("/delete")
    public R<Boolean> delete(@RequestParam Long id) {
        return R.result(payChannelService.deleteById(id));
    }


    @GetMapping("/get-enable-code-list")
    public R<Set<String>> getEnableChannelCodeList(@RequestParam Long appId) {
        return R.ok(payChannelService.getEnableChannelCodeList(appId));
    }

}

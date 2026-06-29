package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayAppService;
import com.fly.system.api.pay.domain.bo.PayAppBo;
import com.fly.system.api.pay.domain.vo.PayAppVo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台 - 支付应用控制器。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/pay/app")
public class PayAppController {

    private final IPayAppService payAppService;

    @PreAuthorize("@pms.hasPermission('pay:app:list')")
    @GetMapping("/page")
    public R<PageVo<PayAppVo>> page(PayAppBo bo, PageBo pageBo) {
        return R.ok(payAppService.queryPageList(bo, pageBo));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:list')")
    @GetMapping("/list")
    public R<List<PayAppVo>> list(PayAppBo bo) {
        return R.ok(payAppService.queryList(bo));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:query')")
    @GetMapping("/get")
    public R<PayAppVo> get(@RequestParam Long id) {
        return R.ok(payAppService.queryById(id));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:create')")
    @PostMapping("/create")
    public R<Void> create(@RequestBody PayAppBo bo) {
        return R.ok(payAppService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:update')")
    @PutMapping("/update")
    public R<Void> update(@RequestBody PayAppBo bo) {
        return R.ok(payAppService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:update')")
    @PutMapping("/update-status")
    public R<Void> updateStatus(@RequestBody PayAppUpdateStatusReq req) {
        return R.ok(payAppService.updateStatus(req.getId(), req.getStatus()));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:delete')")
    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam Long id) {
        return R.ok(payAppService.deleteById(id));
    }

    @Data
    public static class PayAppUpdateStatusReq {
        private Long id;
        private Integer status;
    }

}

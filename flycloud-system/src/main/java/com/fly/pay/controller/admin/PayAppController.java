package com.fly.pay.controller.admin;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.pay.service.IPayAppService;
import com.fly.pay.service.IPayChannelService;
import com.fly.system.api.pay.domain.bo.PayAppBo;
import com.fly.system.api.pay.domain.vo.PayAppPageItemRespVo;
import com.fly.system.api.pay.domain.vo.PayAppRespVo;
import com.fly.system.api.pay.domain.vo.PayAppVo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import cn.hutool.core.bean.BeanUtil;
import java.util.List;

/**
 * 后台 - 支付应用控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/pay/app")
public class PayAppController {

    private final IPayAppService payAppService;
    private final IPayChannelService payChannelService;

    @PreAuthorize("@pms.hasPermission('pay:app:list')")
    @GetMapping("/page")
    public R<PageVo<PayAppPageItemRespVo>> page(PayAppBo bo, PageBo pageBo) {
        return R.ok(buildPayAppPage(payAppService.queryPageList(bo, pageBo)));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:list')")
    @GetMapping("/list")
    public R<List<PayAppRespVo>> list(PayAppBo bo) {
        return R.ok(payAppService.queryList(bo).stream().map(this::buildPayAppRespVo).toList());
    }

    @GetMapping("/get")
    public R<PayAppRespVo> get(@RequestParam Long id) {
        return R.ok(buildPayAppRespVo(payAppService.queryById(id)));
    }


    @PreAuthorize("@pms.hasPermission('pay:app:saveOrUpdate')")
    @PostMapping("/create")
    public R<Long> create(@RequestBody PayAppBo bo) {
        return R.ok(payAppService.createApp(bo));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:saveOrUpdate')")
    @PutMapping("/update")
    public R<Boolean> update(@RequestBody PayAppBo bo) {
        return R.result(payAppService.saveOrUpdate(bo));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:saveOrUpdate')")
    @PutMapping("/update-status")
    public R<Boolean> updateStatus(@RequestBody PayAppUpdateStatusReq req) {
        return R.result(payAppService.updateStatus(req.getId(), req.getStatus()));
    }

    @PreAuthorize("@pms.hasPermission('pay:app:delete')")
    @DeleteMapping("/delete")
    public R<Boolean> delete(@RequestParam Long id) {
        return R.result(payAppService.deleteById(id));
    }

    @Data
    public static class PayAppUpdateStatusReq {
        private Long id;
        private Integer status;
    }

    /**
     * 构建支付应用分页响应对象。
     */
    private PageVo<PayAppPageItemRespVo> buildPayAppPage(PageVo<PayAppVo> pageVo) {
        PageVo<PayAppPageItemRespVo> result = new PageVo<>();
        result.setTotal(pageVo.getTotal());
        result.setPages(pageVo.getPages());
        result.setList(pageVo.getList().stream().map(app -> {
            PayAppPageItemRespVo respVo = BeanUtil.toBean(app, PayAppPageItemRespVo.class);
            respVo.setChannelCodes(payChannelService.getEnableChannelCodeList(app.getId()));
            return respVo;
        }).toList());
        return result;
    }

    /**
     * 构建支付应用响应对象。
     */
    private PayAppRespVo buildPayAppRespVo(PayAppVo app) {
        return app == null ? null : BeanUtil.toBean(app, PayAppRespVo.class);
    }

}

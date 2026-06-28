package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.trade.bo.DeliveryExpressTemplateFreeBo;
import com.fly.mall.api.domain.trade.vo.DeliveryExpressTemplateFreeVo;
import com.fly.mall.trade.service.IDeliveryExpressTemplateFreeService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 运费模板包邮规则 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/delivery-express-template-free")
public class DeliveryExpressTemplateFreeController extends BaseController {

    private final IDeliveryExpressTemplateFreeService deliveryExpressTemplateFreeService;

    /**
     * 查询运费模板包邮规则分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template-free:list')")
    @GetMapping("/list")
    public R<PageVo<DeliveryExpressTemplateFreeVo>> list(DeliveryExpressTemplateFreeBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateFreeService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template-free:list')")
    @GetMapping("/page")
    public R<PageVo<DeliveryExpressTemplateFreeVo>> page(DeliveryExpressTemplateFreeBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateFreeService.queryPageList(bo, page));
    }

    /**
     * 查询所有运费模板包邮规则。
     */
    @GetMapping("/getList")
    public R<List<DeliveryExpressTemplateFreeVo>> allList(DeliveryExpressTemplateFreeBo bo) {
        return R.ok(deliveryExpressTemplateFreeService.queryList(bo));
    }

    /**
     * 获取运费模板包邮规则详情。
     */
    @GetMapping("/get/{id}")
    public R<DeliveryExpressTemplateFreeVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deliveryExpressTemplateFreeService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<DeliveryExpressTemplateFreeVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(deliveryExpressTemplateFreeService.queryById(id));
    }

    /**
     * 新增或修改运费模板包邮规则。
     */
    @Log(title = "运费模板包邮规则", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template-free:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody DeliveryExpressTemplateFreeBo bo) {
        return R.ok(deliveryExpressTemplateFreeService.saveOrUpdate(bo));
    }

    /**
     * 删除运费模板包邮规则。
     */
    @Log(title = "运费模板包邮规则", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template-free:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(deliveryExpressTemplateFreeService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

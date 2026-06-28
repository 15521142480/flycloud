package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.trade.bo.DeliveryExpressTemplateBo;
import com.fly.mall.api.domain.trade.vo.DeliveryExpressTemplateVo;
import com.fly.mall.trade.service.IDeliveryExpressTemplateService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 运费模板 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/delivery-express-template")
public class DeliveryExpressTemplateController extends BaseController {

    private final IDeliveryExpressTemplateService deliveryExpressTemplateService;

    /**
     * 查询运费模板分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template:list')")
    @GetMapping("/list")
    public R<PageVo<DeliveryExpressTemplateVo>> list(DeliveryExpressTemplateBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template:list')")
    @GetMapping("/page")
    public R<PageVo<DeliveryExpressTemplateVo>> page(DeliveryExpressTemplateBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateService.queryPageList(bo, page));
    }

    /**
     * 查询所有运费模板。
     */
    @GetMapping("/getList")
    public R<List<DeliveryExpressTemplateVo>> allList(DeliveryExpressTemplateBo bo) {
        return R.ok(deliveryExpressTemplateService.queryList(bo));
    }

    /**
     * 获取运费模板详情。
     */
    @GetMapping("/get/{id}")
    public R<DeliveryExpressTemplateVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deliveryExpressTemplateService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<DeliveryExpressTemplateVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(deliveryExpressTemplateService.queryById(id));
    }

    /**
     * 新增或修改运费模板。
     */
    @Log(title = "运费模板", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody DeliveryExpressTemplateBo bo) {
        return R.ok(deliveryExpressTemplateService.saveOrUpdate(bo));
    }

    /**
     * 删除运费模板。
     */
    @Log(title = "运费模板", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(deliveryExpressTemplateService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressTemplateChargeBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressTemplateChargeVo;
import com.fly.mall.trade.service.IDeliveryExpressTemplateChargeService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 运费模板计费规则 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/delivery-express-template-charge")
public class DeliveryExpressTemplateChargeController extends BaseController {

    private final IDeliveryExpressTemplateChargeService deliveryExpressTemplateChargeService;

    /**
     * 查询运费模板计费规则分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template-charge:list')")
    @GetMapping("/list")
    public R<PageVo<DeliveryExpressTemplateChargeVo>> list(DeliveryExpressTemplateChargeBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateChargeService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template-charge:list')")
    @GetMapping("/page")
    public R<PageVo<DeliveryExpressTemplateChargeVo>> page(DeliveryExpressTemplateChargeBo bo, PageBo page) {
        return R.ok(deliveryExpressTemplateChargeService.queryPageList(bo, page));
    }

    /**
     * 查询所有运费模板计费规则。
     */
    @GetMapping("/getList")
    public R<List<DeliveryExpressTemplateChargeVo>> allList(DeliveryExpressTemplateChargeBo bo) {
        return R.ok(deliveryExpressTemplateChargeService.queryList(bo));
    }

    /**
     * 获取运费模板计费规则详情。
     */
    @GetMapping("/get/{id}")
    public R<DeliveryExpressTemplateChargeVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deliveryExpressTemplateChargeService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<DeliveryExpressTemplateChargeVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(deliveryExpressTemplateChargeService.queryById(id));
    }

    /**
     * 新增或修改运费模板计费规则。
     */
    @Log(title = "运费模板计费规则", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template-charge:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody DeliveryExpressTemplateChargeBo bo) {
        return R.ok(deliveryExpressTemplateChargeService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody DeliveryExpressTemplateChargeBo bo) {
        return R.ok(deliveryExpressTemplateChargeService.saveOrUpdate(bo));
    }

    /**
     * 删除运费模板计费规则。
     */
    @Log(title = "运费模板计费规则", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express-template-charge:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(deliveryExpressTemplateChargeService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(deliveryExpressTemplateChargeService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

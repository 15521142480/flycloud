package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressVo;
import com.fly.mall.trade.service.IDeliveryExpressService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 快递公司 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/trade/delivery-express")
public class DeliveryExpressController extends BaseController {

    private final IDeliveryExpressService deliveryExpressService;

    /**
     * 查询快递公司分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express:list')")
    @GetMapping("/list")
    public R<PageVo<DeliveryExpressVo>> list(DeliveryExpressBo bo, PageBo page) {
        return R.ok(deliveryExpressService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express:list')")
    @GetMapping("/page")
    public R<PageVo<DeliveryExpressVo>> page(DeliveryExpressBo bo, PageBo page) {
        return R.ok(deliveryExpressService.queryPageList(bo, page));
    }

    /**
     * 查询所有快递公司。
     */
    @GetMapping("/getList")
    public R<List<DeliveryExpressVo>> allList(DeliveryExpressBo bo) {
        return R.ok(deliveryExpressService.queryList(bo));
    }

    /**
     * 获取快递公司详情。
     */
    @GetMapping("/get/{id}")
    public R<DeliveryExpressVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deliveryExpressService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<DeliveryExpressVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(deliveryExpressService.queryById(id));
    }

    /**
     * 新增或修改快递公司。
     */
    @Log(title = "快递公司", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody DeliveryExpressBo bo) {
        return R.ok(deliveryExpressService.saveOrUpdate(bo));
    }

    /**
     * 删除快递公司。
     */
    @Log(title = "快递公司", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery-express:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(deliveryExpressService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

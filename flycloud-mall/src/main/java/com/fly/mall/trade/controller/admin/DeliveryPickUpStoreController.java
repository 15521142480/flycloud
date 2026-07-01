package com.fly.mall.trade.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.trade.domain.bo.DeliveryPickUpStoreBo;
import com.fly.mall.api.trade.domain.vo.DeliveryPickUpStoreVo;
import com.fly.mall.trade.service.IDeliveryPickUpStoreService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 自提门店 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/admin/trade/delivery-pick-up-store", "/admin/trade/delivery/pick-up-store"})
public class DeliveryPickUpStoreController extends BaseController {

    private final IDeliveryPickUpStoreService deliveryPickUpStoreService;

    /**
     * 查询自提门店分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery:pick-up-store:shop:list')")
    @GetMapping("/list")
    public R<PageVo<DeliveryPickUpStoreVo>> list(DeliveryPickUpStoreBo bo, PageBo page) {
        return R.ok(deliveryPickUpStoreService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:trade:delivery:pick-up-store:shop:list')")
    @GetMapping("/page")
    public R<PageVo<DeliveryPickUpStoreVo>> page(DeliveryPickUpStoreBo bo, PageBo page) {
        return R.ok(deliveryPickUpStoreService.queryPageList(bo, page));
    }

    /**
     * 查询所有自提门店。
     */
    @GetMapping({"/getList", "/list-all-simple", "/simple-list"})
    public R<List<DeliveryPickUpStoreVo>> allList(DeliveryPickUpStoreBo bo) {
        return R.ok(deliveryPickUpStoreService.queryList(bo));
    }

    /**
     * 绑定自提门店。
     */
    @PostMapping("/bind")
    public R<Void> bind(@RequestBody DeliveryPickUpStoreBo bo) {
        return R.ok(deliveryPickUpStoreService.saveOrUpdate(bo));
    }

    /**
     * 获取自提门店详情。
     */
    @GetMapping("/get/{id}")
    public R<DeliveryPickUpStoreVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deliveryPickUpStoreService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<DeliveryPickUpStoreVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(deliveryPickUpStoreService.queryById(id));
    }

    /**
     * 新增或修改自提门店。
     */
    @Log(title = "自提门店", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery:pick-up-store:shop:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody DeliveryPickUpStoreBo bo) {
        return R.ok(deliveryPickUpStoreService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody DeliveryPickUpStoreBo bo) {
        return R.ok(deliveryPickUpStoreService.saveOrUpdate(bo));
    }

    /**
     * 删除自提门店。
     */
    @Log(title = "自提门店", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:trade:delivery:pick-up-store:shop:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(deliveryPickUpStoreService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(deliveryPickUpStoreService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

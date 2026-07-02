package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.CouponTemplateBo;
import com.fly.mall.api.promotion.domain.vo.CouponTemplateVo;
import com.fly.mall.promotion.service.ICouponTemplateService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 管理后台 - 优惠券模板 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/coupon-template")
public class CouponTemplateController extends BaseController {

    private final ICouponTemplateService couponTemplateService;



    /**
     * 查询分页列表。
     */
//    @PreAuthorize("@pms.hasPermission('mall:promotion:coupon-template:list')")
    @GetMapping("/page")
    public R<PageVo<CouponTemplateVo>> page(CouponTemplateBo bo, PageBo page) {
        return R.ok(couponTemplateService.queryPageList(bo, page));
    }

    /**
     * 查询优惠券模板分页列表。
     */
//    @PreAuthorize("@pms.hasPermission('mall:promotion:coupon-template:list')")
    @GetMapping("/list")
    public R<List<CouponTemplateVo>>list(@RequestParam("ids") Collection<Long> ids) {
        return R.ok(couponTemplateService.queryList(ids));
    }

    /**
     * 查询所有优惠券模板。
     */
    @GetMapping("/getList")
    public R<List<CouponTemplateVo>> allList(CouponTemplateBo bo) {
        return R.ok(couponTemplateService.queryList(bo));
    }

    /**
     * 获取优惠券模板详情。
     */
    @GetMapping("/get/{id}")
    public R<CouponTemplateVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(couponTemplateService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<CouponTemplateVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(couponTemplateService.queryById(id));
    }

    /**
     * 新增或修改优惠券模板。
     */
    @Log(title = "优惠券模板", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:coupon-template:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody CouponTemplateBo bo) {
        return R.ok(couponTemplateService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody CouponTemplateBo bo) {
        return R.ok(couponTemplateService.saveOrUpdate(bo));
    }

    /**
     * 更新优惠券模板状态。
     */
    @PutMapping("/update-status")
    public R<Void> updateStatus(@RequestBody CouponTemplateBo bo) {
        return R.ok(couponTemplateService.saveOrUpdate(bo));
    }

    /**
     * 删除优惠券模板。
     */
    @Log(title = "优惠券模板", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:coupon-template:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(couponTemplateService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(couponTemplateService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

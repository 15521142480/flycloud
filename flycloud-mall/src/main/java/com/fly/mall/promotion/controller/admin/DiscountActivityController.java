package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.DiscountActivityBo;
import com.fly.mall.api.promotion.domain.vo.DiscountActivityVo;
import com.fly.mall.promotion.service.IDiscountActivityService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 限时折扣活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/discount-activity")
public class DiscountActivityController extends BaseController {

    private final IDiscountActivityService discountActivityService;

    /**
     * 查询限时折扣活动分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:discount-activity:list')")
    @GetMapping("/list")
    public R<PageVo<DiscountActivityVo>> list(DiscountActivityBo bo, PageBo page) {
        return R.ok(discountActivityService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:discount-activity:list')")
    @GetMapping("/page")
    public R<PageVo<DiscountActivityVo>> page(DiscountActivityBo bo, PageBo page) {
        return R.ok(discountActivityService.queryPageList(bo, page));
    }

    /**
     * 查询所有限时折扣活动。
     */
    @GetMapping("/getList")
    public R<List<DiscountActivityVo>> allList(DiscountActivityBo bo) {
        return R.ok(discountActivityService.queryList(bo));
    }

    /**
     * 获取限时折扣活动详情。
     */
    @GetMapping("/get/{id}")
    public R<DiscountActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(discountActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<DiscountActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(discountActivityService.queryById(id));
    }

    /**
     * 新增或修改限时折扣活动。
     */
    @Log(title = "限时折扣活动", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:discount-activity:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody DiscountActivityBo bo) {
        return R.ok(discountActivityService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody DiscountActivityBo bo) {
        return R.ok(discountActivityService.saveOrUpdate(bo));
    }

    /**
     * 关闭限时折扣活动。
     */
    @PutMapping("/close")
    public R<Void> close(@RequestParam("id") Long id) {
        DiscountActivityBo bo = new DiscountActivityBo();
        bo.setId(id);
        bo.setStatus(com.fly.common.enums.StatusEnum.DISABLE.getStatus());
        return R.ok(discountActivityService.saveOrUpdate(bo));
    }

    /**
     * 删除限时折扣活动。
     */
    @Log(title = "限时折扣活动", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:discount-activity:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(discountActivityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(discountActivityService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

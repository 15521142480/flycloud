package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.BannerBo;
import com.fly.mall.api.promotion.domain.vo.BannerVo;
import com.fly.mall.promotion.service.IBannerService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 轮播图 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/banner")
public class BannerController extends BaseController {

    private final IBannerService bannerService;

    /**
     * 查询轮播图分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:banner:list')")
    @GetMapping("/list")
    public R<PageVo<BannerVo>> list(BannerBo bo, PageBo page) {
        return R.ok(bannerService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:banner:list')")
    @GetMapping("/page")
    public R<PageVo<BannerVo>> page(BannerBo bo, PageBo page) {
        return R.ok(bannerService.queryPageList(bo, page));
    }

    /**
     * 查询所有轮播图。
     */
    @GetMapping("/getList")
    public R<List<BannerVo>> allList(BannerBo bo) {
        return R.ok(bannerService.queryList(bo));
    }

    /**
     * 获取轮播图详情。
     */
    @GetMapping("/get/{id}")
    public R<BannerVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(bannerService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<BannerVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(bannerService.queryById(id));
    }

    /**
     * 新增或修改轮播图。
     */
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:banner:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody BannerBo bo) {
        return R.ok(bannerService.saveOrUpdate(bo));
    }

    /**
     * 删除轮播图。
     */
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:banner:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(bannerService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

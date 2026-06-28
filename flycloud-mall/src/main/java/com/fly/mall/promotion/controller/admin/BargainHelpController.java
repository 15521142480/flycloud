package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.promotion.bo.BargainHelpBo;
import com.fly.mall.api.domain.promotion.vo.BargainHelpVo;
import com.fly.mall.promotion.service.IBargainHelpService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 砍价助力 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/bargain-help")
public class BargainHelpController extends BaseController {

    private final IBargainHelpService bargainHelpService;

    /**
     * 查询砍价助力分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-help:list')")
    @GetMapping("/list")
    public R<PageVo<BargainHelpVo>> list(BargainHelpBo bo, PageBo page) {
        return R.ok(bargainHelpService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-help:list')")
    @GetMapping("/page")
    public R<PageVo<BargainHelpVo>> page(BargainHelpBo bo, PageBo page) {
        return R.ok(bargainHelpService.queryPageList(bo, page));
    }

    /**
     * 查询所有砍价助力。
     */
    @GetMapping("/getList")
    public R<List<BargainHelpVo>> allList(BargainHelpBo bo) {
        return R.ok(bargainHelpService.queryList(bo));
    }

    /**
     * 获取砍价助力详情。
     */
    @GetMapping("/get/{id}")
    public R<BargainHelpVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(bargainHelpService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<BargainHelpVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(bargainHelpService.queryById(id));
    }

    /**
     * 新增或修改砍价助力。
     */
    @Log(title = "砍价助力", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-help:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody BargainHelpBo bo) {
        return R.ok(bargainHelpService.saveOrUpdate(bo));
    }

    /**
     * 删除砍价助力。
     */
    @Log(title = "砍价助力", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-help:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(bargainHelpService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.CombinationActivityBo;
import com.fly.mall.api.promotion.domain.vo.CombinationActivityVo;
import com.fly.mall.promotion.service.ICombinationActivityService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 拼团活动 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/combination-activity")
public class CombinationActivityController extends BaseController {

    private final ICombinationActivityService combinationActivityService;

    /**
     * 查询拼团活动分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-activity:list')")
    @GetMapping("/list")
    public R<PageVo<CombinationActivityVo>> list(CombinationActivityBo bo, PageBo page) {
        return R.ok(combinationActivityService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-activity:list')")
    @GetMapping("/page")
    public R<PageVo<CombinationActivityVo>> page(CombinationActivityBo bo, PageBo page) {
        return R.ok(combinationActivityService.queryPageList(bo, page));
    }

    /**
     * 查询所有拼团活动。
     */
    @GetMapping("/getList")
    public R<List<CombinationActivityVo>> allList(CombinationActivityBo bo) {
        return R.ok(combinationActivityService.queryList(bo));
    }

    /**
     * 获取拼团活动详情。
     */
    @GetMapping("/get/{id}")
    public R<CombinationActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(combinationActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<CombinationActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(combinationActivityService.queryById(id));
    }

    /**
     * 新增或修改拼团活动。
     */
    @Log(title = "拼团活动", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-activity:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody CombinationActivityBo bo) {
        return R.ok(combinationActivityService.saveOrUpdate(bo));
    }

    /**
     * 删除拼团活动。
     */
    @Log(title = "拼团活动", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-activity:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(combinationActivityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

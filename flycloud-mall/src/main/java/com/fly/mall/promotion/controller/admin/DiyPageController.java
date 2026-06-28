package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.promotion.bo.DiyPageBo;
import com.fly.mall.api.domain.promotion.vo.DiyPageVo;
import com.fly.mall.promotion.service.IDiyPageService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 装修页面 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/diy-page")
public class DiyPageController extends BaseController {

    private final IDiyPageService diyPageService;

    /**
     * 查询装修页面分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:list')")
    @GetMapping("/list")
    public R<PageVo<DiyPageVo>> list(DiyPageBo bo, PageBo page) {
        return R.ok(diyPageService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:list')")
    @GetMapping("/page")
    public R<PageVo<DiyPageVo>> page(DiyPageBo bo, PageBo page) {
        return R.ok(diyPageService.queryPageList(bo, page));
    }

    /**
     * 查询所有装修页面。
     */
    @GetMapping("/getList")
    public R<List<DiyPageVo>> allList(DiyPageBo bo) {
        return R.ok(diyPageService.queryList(bo));
    }

    /**
     * 获取装修页面详情。
     */
    @GetMapping("/get/{id}")
    public R<DiyPageVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(diyPageService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<DiyPageVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(diyPageService.queryById(id));
    }

    /**
     * 新增或修改装修页面。
     */
    @Log(title = "装修页面", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody DiyPageBo bo) {
        return R.ok(diyPageService.saveOrUpdate(bo));
    }

    /**
     * 删除装修页面。
     */
    @Log(title = "装修页面", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(diyPageService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

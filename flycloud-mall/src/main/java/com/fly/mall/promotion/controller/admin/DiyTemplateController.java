package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.promotion.bo.DiyTemplateBo;
import com.fly.mall.api.domain.promotion.vo.DiyTemplateVo;
import com.fly.mall.promotion.service.IDiyTemplateService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 装修模板 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/diy-template")
public class DiyTemplateController extends BaseController {

    private final IDiyTemplateService diyTemplateService;

    /**
     * 查询装修模板分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-template:list')")
    @GetMapping("/list")
    public R<PageVo<DiyTemplateVo>> list(DiyTemplateBo bo, PageBo page) {
        return R.ok(diyTemplateService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-template:list')")
    @GetMapping("/page")
    public R<PageVo<DiyTemplateVo>> page(DiyTemplateBo bo, PageBo page) {
        return R.ok(diyTemplateService.queryPageList(bo, page));
    }

    /**
     * 查询所有装修模板。
     */
    @GetMapping("/getList")
    public R<List<DiyTemplateVo>> allList(DiyTemplateBo bo) {
        return R.ok(diyTemplateService.queryList(bo));
    }

    /**
     * 获取装修模板详情。
     */
    @GetMapping("/get/{id}")
    public R<DiyTemplateVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(diyTemplateService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<DiyTemplateVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(diyTemplateService.queryById(id));
    }

    /**
     * 新增或修改装修模板。
     */
    @Log(title = "装修模板", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-template:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody DiyTemplateBo bo) {
        return R.ok(diyTemplateService.saveOrUpdate(bo));
    }

    /**
     * 删除装修模板。
     */
    @Log(title = "装修模板", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-template:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(diyTemplateService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

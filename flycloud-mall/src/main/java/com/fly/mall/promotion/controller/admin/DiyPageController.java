package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.DiyPageBo;
import com.fly.mall.api.promotion.domain.vo.DiyPagePropertyRespVo;
import com.fly.mall.api.promotion.domain.vo.DiyPageRespVo;
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
     * 新增装修页面。
     */
    @Log(title = "装修页面", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:create')")
    @PostMapping("/create")
    public R<Long> create(@RequestBody DiyPageBo bo) {
        return R.ok(diyPageService.createDiyPage(bo));
    }

    /**
     * 修改装修页面。
     */
    @Log(title = "装修页面", businessType = BusinessType.UPDATE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:update')")
    @PutMapping("/update")
    public R<Void> update(@RequestBody DiyPageBo bo) {
        return R.ok(diyPageService.updateDiyPage(bo));
    }

    /**
     * 查询装修页面分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:list')")
    @GetMapping("/list")
    public R<List<DiyPageRespVo>> list(@RequestParam("ids") List<Long> ids) {
        return R.ok(diyPageService.queryRespListByIds(ids));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:list')")
    @GetMapping("/page")
    public R<PageVo<DiyPageRespVo>> page(DiyPageBo bo, PageBo page) {
        return R.ok(diyPageService.queryRespPageList(bo, page));
    }

    /**
     * 查询所有装修页面。
     */
    @GetMapping("/getList")
    public R<List<DiyPageRespVo>> allList(DiyPageBo bo) {
        return R.ok(diyPageService.queryRespList(bo));
    }

    /**
     * 根据编号集合查询装修页面。
     */
    @GetMapping("/list-by-ids")
    public R<List<DiyPageRespVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(diyPageService.queryRespListByIds(ids));
    }

    /**
     * 获取装修页面详情。
     */
    @GetMapping("/get/{id}")
    public R<DiyPageRespVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(diyPageService.queryRespById(id));
    }

    /**
     * 获取装修页面详情。
     */
    @GetMapping("/get")
    public R<DiyPageRespVo> get(@RequestParam("id") Long id) {
        return R.ok(diyPageService.queryRespById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<DiyPageRespVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(diyPageService.queryRespById(id));
    }

    /**
     * 获取装修页面属性。
     */
    @GetMapping("/get-property")
    public R<DiyPagePropertyRespVo> getProperty(@RequestParam("id") Long id) {
        return R.ok(diyPageService.queryPropertyRespById(id));
    }

    /**
     * 修改装修页面属性。
     */
    @Log(title = "装修页面属性", businessType = BusinessType.UPDATE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:update')")
    @PutMapping("/update-property")
    public R<Void> updateProperty(@RequestBody DiyPageBo bo) {
        return R.ok(diyPageService.updateDiyPageProperty(bo));
    }

    /**
     * 新增或修改装修页面。
     */
    @Log(title = "装修页面", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
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

    /**
     * 删除装修页面。
     */
    @Log(title = "装修页面", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:diy-page:delete')")
    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        return R.ok(diyPageService.deleteDiyPage(id));
    }

}

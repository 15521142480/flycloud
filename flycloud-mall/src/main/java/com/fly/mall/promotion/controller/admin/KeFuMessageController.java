package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.KeFuMessageBo;
import com.fly.mall.api.promotion.domain.vo.KeFuMessageVo;
import com.fly.mall.promotion.service.IKeFuMessageService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 客服消息 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/ke-fu-message")
public class KeFuMessageController extends BaseController {

    private final IKeFuMessageService keFuMessageService;

    /**
     * 查询客服消息分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:ke-fu-message:list')")
    @GetMapping("/list")
    public R<PageVo<KeFuMessageVo>> list(KeFuMessageBo bo, PageBo page) {
        return R.ok(keFuMessageService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:ke-fu-message:list')")
    @GetMapping("/page")
    public R<PageVo<KeFuMessageVo>> page(KeFuMessageBo bo, PageBo page) {
        return R.ok(keFuMessageService.queryPageList(bo, page));
    }

    /**
     * 查询所有客服消息。
     */
    @GetMapping("/getList")
    public R<List<KeFuMessageVo>> allList(KeFuMessageBo bo) {
        return R.ok(keFuMessageService.queryList(bo));
    }

    /**
     * 获取客服消息详情。
     */
    @GetMapping("/get/{id}")
    public R<KeFuMessageVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(keFuMessageService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<KeFuMessageVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(keFuMessageService.queryById(id));
    }

    /**
     * 新增或修改客服消息。
     */
    @Log(title = "客服消息", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:ke-fu-message:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody KeFuMessageBo bo) {
        return R.ok(keFuMessageService.saveOrUpdate(bo));
    }

    /**
     * 删除客服消息。
     */
    @Log(title = "客服消息", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:ke-fu-message:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(keFuMessageService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

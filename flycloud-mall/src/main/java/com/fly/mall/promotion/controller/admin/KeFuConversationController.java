package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.KeFuConversationBo;
import com.fly.mall.api.promotion.domain.vo.KeFuConversationVo;
import com.fly.mall.promotion.service.IKeFuConversationService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 客服会话 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping({"/admin/promotion/ke-fu-conversation", "/admin/promotion/kefu-conversation"})
public class KeFuConversationController extends BaseController {

    private final IKeFuConversationService keFuConversationService;

    /**
     * 查询客服会话分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:ke-fu-conversation:list')")
    @GetMapping("/list")
    public R<PageVo<KeFuConversationVo>> list(KeFuConversationBo bo, PageBo page) {
        return R.ok(keFuConversationService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:ke-fu-conversation:list')")
    @GetMapping("/page")
    public R<PageVo<KeFuConversationVo>> page(KeFuConversationBo bo, PageBo page) {
        return R.ok(keFuConversationService.queryPageList(bo, page));
    }

    /**
     * 查询所有客服会话。
     */
    @GetMapping("/getList")
    public R<List<KeFuConversationVo>> allList(KeFuConversationBo bo) {
        return R.ok(keFuConversationService.queryList(bo));
    }

    /**
     * 获取客服会话详情。
     */
    @GetMapping("/get/{id}")
    public R<KeFuConversationVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(keFuConversationService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<KeFuConversationVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(keFuConversationService.queryById(id));
    }

    /**
     * 新增或修改客服会话。
     */
    @Log(title = "客服会话", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:ke-fu-conversation:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody KeFuConversationBo bo) {
        return R.ok(keFuConversationService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody KeFuConversationBo bo) {
        return R.ok(keFuConversationService.saveOrUpdate(bo));
    }

    /**
     * 更新客服会话置顶状态。
     */
    @PutMapping("/update-conversation-pinned")
    public R<Void> updateConversationPinned(@RequestBody KeFuConversationBo bo) {
        return R.ok(keFuConversationService.saveOrUpdate(bo));
    }

    /**
     * 删除客服会话。
     */
    @Log(title = "客服会话", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:ke-fu-conversation:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(keFuConversationService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(keFuConversationService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.BargainActivityBo;
import com.fly.mall.api.promotion.domain.vo.BargainActivityVo;
import com.fly.mall.promotion.service.IBargainActivityService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 砍价活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/bargain-activity")
public class BargainActivityController extends BaseController {

    private final IBargainActivityService bargainActivityService;

    /**
     * 查询砍价活动分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-activity:list')")
    @GetMapping("/list")
    public R<PageVo<BargainActivityVo>> list(BargainActivityBo bo, PageBo page) {
        return R.ok(bargainActivityService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-activity:list')")
    @GetMapping("/page")
    public R<PageVo<BargainActivityVo>> page(BargainActivityBo bo, PageBo page) {
        return R.ok(bargainActivityService.queryPageList(bo, page));
    }

    /**
     * 查询所有砍价活动。
     */
    @GetMapping("/getList")
    public R<List<BargainActivityVo>> allList(BargainActivityBo bo) {
        return R.ok(bargainActivityService.queryList(bo));
    }

    /**
     * 获取砍价活动详情。
     */
    @GetMapping("/get/{id}")
    public R<BargainActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(bargainActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<BargainActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(bargainActivityService.queryById(id));
    }

    /**
     * 新增或修改砍价活动。
     */
    @Log(title = "砍价活动", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-activity:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody BargainActivityBo bo) {
        return R.result(bargainActivityService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody BargainActivityBo bo) {
        return R.result(bargainActivityService.saveOrUpdate(bo));
    }

    /**
     * 关闭砍价活动。
     */
    @PutMapping("/close")
    public R<Void> close(@RequestParam("id") Long id) {
        BargainActivityBo bo = new BargainActivityBo();
        bo.setId(id);
        bo.setStatus(com.fly.common.enums.StatusEnum.DISABLE.getStatus());
        return R.result(bargainActivityService.saveOrUpdate(bo));
    }

    /**
     * 删除砍价活动。
     */
    @Log(title = "砍价活动", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-activity:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(bargainActivityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(bargainActivityService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.PointActivityBo;
import com.fly.mall.api.promotion.domain.vo.PointActivityVo;
import com.fly.mall.promotion.service.IPointActivityService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 积分商城活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/point-activity")
public class PointActivityController extends BaseController {

    private final IPointActivityService pointActivityService;

    /**
     * 查询积分商城活动分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:point-activity:list')")
    @GetMapping("/list")
    public R<PageVo<PointActivityVo>> list(PointActivityBo bo, PageBo page) {
        return R.ok(pointActivityService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:point-activity:list')")
    @GetMapping("/page")
    public R<PageVo<PointActivityVo>> page(PointActivityBo bo, PageBo page) {
        return R.ok(pointActivityService.queryPageList(bo, page));
    }

    /**
     * 查询所有积分商城活动。
     */
    @GetMapping("/getList")
    public R<List<PointActivityVo>> allList(PointActivityBo bo) {
        return R.ok(pointActivityService.queryList(bo));
    }

    /**
     * 获取积分商城活动详情。
     */
    @GetMapping("/get/{id}")
    public R<PointActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(pointActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<PointActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(pointActivityService.queryById(id));
    }

    /**
     * 新增或修改积分商城活动。
     */
    @Log(title = "积分商城活动", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:point-activity:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody PointActivityBo bo) {
        return R.result(pointActivityService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody PointActivityBo bo) {
        return R.result(pointActivityService.saveOrUpdate(bo));
    }

    /**
     * 根据编号列表查询积分商城活动。
     */
    @GetMapping("/list-by-ids")
    public R<List<PointActivityVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(pointActivityService.queryList(new PointActivityBo()).stream()
                .filter(item -> ids.contains(item.getId())).toList());
    }

    /**
     * 关闭积分商城活动。
     */
    @PutMapping("/close")
    public R<Void> close(@RequestParam("id") Long id) {
        PointActivityBo bo = new PointActivityBo();
        bo.setId(id);
        bo.setStatus(com.fly.common.enums.StatusEnum.DISABLE.getStatus());
        return R.result(pointActivityService.saveOrUpdate(bo));
    }

    /**
     * 删除积分商城活动。
     */
    @Log(title = "积分商城活动", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:point-activity:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(pointActivityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(pointActivityService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

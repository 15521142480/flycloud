package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.RewardActivityBo;
import com.fly.mall.api.promotion.domain.vo.RewardActivityVo;
import com.fly.mall.promotion.service.IRewardActivityService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 满减送活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/reward-activity")
public class RewardActivityController extends BaseController {

    private final IRewardActivityService rewardActivityService;

    /**
     * 查询满减送活动分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:reward-activity:list')")
    @GetMapping("/list")
    public R<PageVo<RewardActivityVo>> list(RewardActivityBo bo, PageBo page) {
        return R.ok(rewardActivityService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:reward-activity:list')")
    @GetMapping("/page")
    public R<PageVo<RewardActivityVo>> page(RewardActivityBo bo, PageBo page) {
        return R.ok(rewardActivityService.queryPageList(bo, page));
    }

    /**
     * 查询所有满减送活动。
     */
    @GetMapping("/getList")
    public R<List<RewardActivityVo>> allList(RewardActivityBo bo) {
        return R.ok(rewardActivityService.queryList(bo));
    }

    /**
     * 获取满减送活动详情。
     */
    @GetMapping("/get/{id}")
    public R<RewardActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(rewardActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<RewardActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(rewardActivityService.queryById(id));
    }

    /**
     * 新增或修改满减送活动。
     */
    @Log(title = "满减送活动", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:reward-activity:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Long> saveOrUpdate(@RequestBody RewardActivityBo bo) {
        if (bo != null && bo.getId() != null) {
            rewardActivityService.updateRewardActivity(bo);
            return R.ok(bo.getId());
        }
        return R.ok(rewardActivityService.createRewardActivity(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Boolean> yudaoUpdate(@RequestBody RewardActivityBo bo) {
        return R.result(rewardActivityService.updateRewardActivity(bo));
    }

    /**
     * 关闭满减送活动。
     */
    @PutMapping("/close")
    public R<Boolean> close(@RequestParam("id") Long id) {
        return R.result(rewardActivityService.closeRewardActivity(id));
    }

    /**
     * 删除满减送活动。
     */
    @Log(title = "满减送活动", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:reward-activity:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Boolean> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(rewardActivityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Boolean> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(rewardActivityService.deleteRewardActivity(id));
    }

}

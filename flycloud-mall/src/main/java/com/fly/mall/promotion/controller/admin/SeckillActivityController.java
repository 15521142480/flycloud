package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.SeckillActivityBo;
import com.fly.mall.api.promotion.domain.vo.SeckillActivityVo;
import com.fly.mall.promotion.service.ISeckillActivityService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 秒杀活动 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/seckill-activity")
public class SeckillActivityController extends BaseController {

    private final ISeckillActivityService seckillActivityService;

    /**
     * 查询秒杀活动分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-activity:list')")
    @GetMapping("/list")
    public R<PageVo<SeckillActivityVo>> list(SeckillActivityBo bo, PageBo page) {
        return R.ok(seckillActivityService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-activity:list')")
    @GetMapping("/page")
    public R<PageVo<SeckillActivityVo>> page(SeckillActivityBo bo, PageBo page) {
        return R.ok(seckillActivityService.queryPageList(bo, page));
    }

    /**
     * 查询所有秒杀活动。
     */
    @GetMapping("/getList")
    public R<List<SeckillActivityVo>> allList(SeckillActivityBo bo) {
        return R.ok(seckillActivityService.queryList(bo));
    }

    /**
     * 获取秒杀活动详情。
     */
    @GetMapping("/get/{id}")
    public R<SeckillActivityVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(seckillActivityService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<SeckillActivityVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(seckillActivityService.queryById(id));
    }

    /**
     * 新增或修改秒杀活动。
     */
    @Log(title = "秒杀活动", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-activity:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody SeckillActivityBo bo) {
        return R.result(seckillActivityService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody SeckillActivityBo bo) {
        return R.result(seckillActivityService.saveOrUpdate(bo));
    }

    /**
     * 根据编号列表查询秒杀活动。
     */
    @GetMapping("/list-by-ids")
    public R<List<SeckillActivityVo>> listByIds(@RequestParam("ids") List<Long> ids) {
        return R.ok(seckillActivityService.queryList(new SeckillActivityBo()).stream()
                .filter(item -> ids.contains(item.getId())).toList());
    }

    /**
     * 关闭秒杀活动。
     */
    @PutMapping("/close")
    public R<Void> close(@RequestParam("id") Long id) {
        SeckillActivityBo bo = new SeckillActivityBo();
        bo.setId(id);
        bo.setStatus(com.fly.common.enums.StatusEnum.DISABLE.getStatus());
        return R.result(seckillActivityService.saveOrUpdate(bo));
    }

    /**
     * 删除秒杀活动。
     */
    @Log(title = "秒杀活动", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-activity:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.result(seckillActivityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.result(seckillActivityService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

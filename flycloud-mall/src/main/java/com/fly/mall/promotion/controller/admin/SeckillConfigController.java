package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.SeckillConfigBo;
import com.fly.mall.api.promotion.domain.vo.SeckillConfigVo;
import com.fly.mall.promotion.service.ISeckillConfigService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 秒杀时段配置 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/seckill-config")
public class SeckillConfigController extends BaseController {

    private final ISeckillConfigService seckillConfigService;

    /**
     * 查询秒杀时段配置分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-config:list')")
    @GetMapping("/list")
    public R<PageVo<SeckillConfigVo>> list(SeckillConfigBo bo, PageBo page) {
        return R.ok(seckillConfigService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-config:list')")
    @GetMapping("/page")
    public R<PageVo<SeckillConfigVo>> page(SeckillConfigBo bo, PageBo page) {
        return R.ok(seckillConfigService.queryPageList(bo, page));
    }

    /**
     * 查询所有秒杀时段配置。
     */
    @GetMapping("/getList")
    public R<List<SeckillConfigVo>> allList(SeckillConfigBo bo) {
        return R.ok(seckillConfigService.queryList(bo));
    }

    /**
     * 获取秒杀时段配置详情。
     */
    @GetMapping("/get/{id}")
    public R<SeckillConfigVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(seckillConfigService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<SeckillConfigVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(seckillConfigService.queryById(id));
    }

    /**
     * 新增或修改秒杀时段配置。
     */
    @Log(title = "秒杀时段配置", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-config:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody SeckillConfigBo bo) {
        return R.ok(seckillConfigService.saveOrUpdate(bo));
    }

    /**
     * 删除秒杀时段配置。
     */
    @Log(title = "秒杀时段配置", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-config:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(seckillConfigService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

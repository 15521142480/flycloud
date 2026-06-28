package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.domain.promotion.bo.SeckillProductBo;
import com.fly.mall.api.domain.promotion.vo.SeckillProductVo;
import com.fly.mall.promotion.service.ISeckillProductService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 秒杀商品 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/seckill-product")
public class SeckillProductController extends BaseController {

    private final ISeckillProductService seckillProductService;

    /**
     * 查询秒杀商品分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-product:list')")
    @GetMapping("/list")
    public R<PageVo<SeckillProductVo>> list(SeckillProductBo bo, PageBo page) {
        return R.ok(seckillProductService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-product:list')")
    @GetMapping("/page")
    public R<PageVo<SeckillProductVo>> page(SeckillProductBo bo, PageBo page) {
        return R.ok(seckillProductService.queryPageList(bo, page));
    }

    /**
     * 查询所有秒杀商品。
     */
    @GetMapping("/getList")
    public R<List<SeckillProductVo>> allList(SeckillProductBo bo) {
        return R.ok(seckillProductService.queryList(bo));
    }

    /**
     * 获取秒杀商品详情。
     */
    @GetMapping("/get/{id}")
    public R<SeckillProductVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(seckillProductService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping("/get-detail")
    public R<SeckillProductVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(seckillProductService.queryById(id));
    }

    /**
     * 新增或修改秒杀商品。
     */
    @Log(title = "秒杀商品", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-product:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@RequestBody SeckillProductBo bo) {
        return R.ok(seckillProductService.saveOrUpdate(bo));
    }

    /**
     * 删除秒杀商品。
     */
    @Log(title = "秒杀商品", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:seckill-product:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(seckillProductService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

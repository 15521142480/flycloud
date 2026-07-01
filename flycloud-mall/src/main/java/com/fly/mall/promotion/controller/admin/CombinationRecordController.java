package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.CombinationRecordBo;
import com.fly.mall.api.promotion.domain.vo.CombinationRecordVo;
import com.fly.mall.promotion.service.ICombinationRecordService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 管理后台 - 拼团记录 控制器。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/combination-record")
public class CombinationRecordController extends BaseController {

    private final ICombinationRecordService combinationRecordService;

    /**
     * 查询拼团记录分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-record:list')")
    @GetMapping("/list")
    public R<PageVo<CombinationRecordVo>> list(CombinationRecordBo bo, PageBo page) {
        return R.ok(combinationRecordService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-record:list')")
    @GetMapping("/page")
    public R<PageVo<CombinationRecordVo>> page(CombinationRecordBo bo, PageBo page) {
        return R.ok(combinationRecordService.queryPageList(bo, page));
    }

    /**
     * 查询所有拼团记录。
     */
    @GetMapping("/getList")
    public R<List<CombinationRecordVo>> allList(CombinationRecordBo bo) {
        return R.ok(combinationRecordService.queryList(bo));
    }

    /**
     * 获取拼团记录详情。
     */
    @GetMapping("/get/{id}")
    public R<CombinationRecordVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(combinationRecordService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<CombinationRecordVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(combinationRecordService.queryById(id));
    }

    /**
     * 获得拼团记录汇总。
     */
    @GetMapping("/get-summary")
    public R<Map<String, Long>> getSummary(CombinationRecordBo bo) {
        return R.ok(Map.of("count", (long) combinationRecordService.queryList(bo).size()));
    }

    /**
     * 新增或修改拼团记录。
     */
    @Log(title = "拼团记录", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-record:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody CombinationRecordBo bo) {
        return R.ok(combinationRecordService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody CombinationRecordBo bo) {
        return R.ok(combinationRecordService.saveOrUpdate(bo));
    }

    /**
     * 删除拼团记录。
     */
    @Log(title = "拼团记录", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:combination-record:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(combinationRecordService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(combinationRecordService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

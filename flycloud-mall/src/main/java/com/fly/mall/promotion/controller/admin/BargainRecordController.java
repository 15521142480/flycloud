package com.fly.mall.promotion.controller.admin;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.mall.api.promotion.domain.bo.BargainRecordBo;
import com.fly.mall.api.promotion.domain.vo.BargainRecordVo;
import com.fly.mall.promotion.service.IBargainRecordService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 管理后台 - 砍价记录 控制器。
 *
 * @author lxs
 * @date 2026-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/promotion/bargain-record")
public class BargainRecordController extends BaseController {

    private final IBargainRecordService bargainRecordService;

    /**
     * 查询砍价记录分页列表。
     */
    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-record:list')")
    @GetMapping("/list")
    public R<PageVo<BargainRecordVo>> list(BargainRecordBo bo, PageBo page) {
        return R.ok(bargainRecordService.queryPageList(bo, page));
    }

    /**
     * 查询分页列表。
     */

    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-record:list')")
    @GetMapping("/page")
    public R<PageVo<BargainRecordVo>> page(BargainRecordBo bo, PageBo page) {
        return R.ok(bargainRecordService.queryPageList(bo, page));
    }

    /**
     * 查询所有砍价记录。
     */
    @GetMapping("/getList")
    public R<List<BargainRecordVo>> allList(BargainRecordBo bo) {
        return R.ok(bargainRecordService.queryList(bo));
    }

    /**
     * 获取砍价记录详情。
     */
    @GetMapping("/get/{id}")
    public R<BargainRecordVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(bargainRecordService.queryById(id));
    }

    /**
     * 获得详情。
     */
    @GetMapping({"/get-detail", "/get"})
    public R<BargainRecordVo> getDetail(@RequestParam("id") Long id) {
        return R.ok(bargainRecordService.queryById(id));
    }

    /**
     * 新增或修改砍价记录。
     */
    @Log(title = "砍价记录", businessType = BusinessType.INSERT)
    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-record:saveOrUpdate')")
    @PostMapping({"/saveOrUpdate", "/create"})
    public R<Void> saveOrUpdate(@RequestBody BargainRecordBo bo) {
        return R.ok(bargainRecordService.saveOrUpdate(bo));
    }

    /**
     * 更新数据，兼容 yudao 前端接口。
     */
    @PutMapping("/update")
    public R<Void> yudaoUpdate(@RequestBody BargainRecordBo bo) {
        return R.ok(bargainRecordService.saveOrUpdate(bo));
    }

    /**
     * 删除砍价记录。
     */
    @Log(title = "砍价记录", businessType = BusinessType.DELETE)
    @PreAuthorize("@pms.hasPermission('mall:promotion:bargain-record:delete')")
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(bargainRecordService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 删除数据，兼容 yudao 前端接口。
     */
    @DeleteMapping("/delete")
    public R<Void> yudaoDelete(@RequestParam("id") Long id) {
        return R.ok(bargainRecordService.deleteWithValidByIds(java.util.List.of(id), true));
    }

}

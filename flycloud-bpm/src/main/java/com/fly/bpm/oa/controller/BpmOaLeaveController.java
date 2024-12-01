package com.fly.bpm.oa.controller;

import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.annotation.Log;
import com.fly.common.enums.BusinessType;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.bpm.api.domain.vo.BpmOaLeaveVo;
import com.fly.bpm.api.domain.bo.BpmOaLeaveBo;
import com.fly.bpm.oa.service.IBpmOaLeaveService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * OA 请假申请控制器
 *
 * @author fly
 * @date 2024-11-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/oaLeave")
public class BpmOaLeaveController extends BaseController {

    private final IBpmOaLeaveService iBpmOaLeaveService;


    /**
     * 查询OA 请假申请列表
     */
    @GetMapping("/list")
    public R<PageVo<BpmOaLeaveVo>> list(BpmOaLeaveBo bo, PageBo page) {
        return R.ok(iBpmOaLeaveService.queryPageList(bo, page));
    }


    /**
     * 导出OA 请假申请列表
     */
    @Log(title = "OA 请假申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BpmOaLeaveBo bo, HttpServletResponse response) {
        List<BpmOaLeaveVo> list = iBpmOaLeaveService.queryList(bo);
        ExcelUtil.exportExcel(list, "OA 请假申请", BpmOaLeaveVo.class, response);
    }


    /**
     * 获取OA 请假申请详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<BpmOaLeaveVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iBpmOaLeaveService.queryById(id));
    }


    /**
     * 新增OA 请假申请
     */
    @Log(title = "OA 请假申请", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BpmOaLeaveBo bo) {
        return R.ok(iBpmOaLeaveService.insertByBo(bo));
    }


    /**
     * 修改OA 请假申请
     */
    @Log(title = "OA 请假申请", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BpmOaLeaveBo bo) {
        return R.ok(iBpmOaLeaveService.updateByBo(bo));
    }


    /**
     * 删除OA 请假申请
     *
     * @param ids 主键串
     */
    @Log(title = "OA 请假申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iBpmOaLeaveService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}

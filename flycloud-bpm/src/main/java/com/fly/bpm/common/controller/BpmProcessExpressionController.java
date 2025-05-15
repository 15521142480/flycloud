package com.fly.bpm.common.controller;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.bpm.api.domain.vo.BpmProcessExpressionVo;
import com.fly.bpm.api.domain.bo.BpmProcessExpressionBo;
import com.fly.bpm.common.service.IBpmProcessExpressionService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * BPM 流程达式控制器
 *
 * @author fly
 * @date 2024-11-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/processExpression")
public class BpmProcessExpressionController extends BaseController {

    private final IBpmProcessExpressionService iBpmProcessExpressionService;


    /**
     * 查询BPM 流程达式列表
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('bpm:manage:expression:list')")
    public R<PageVo<BpmProcessExpressionVo>> list(BpmProcessExpressionBo bo, PageBo page) {
        return R.ok(iBpmProcessExpressionService.queryPageList(bo, page));
    }


    /**
     * 获取BPM 流程达式详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<BpmProcessExpressionVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iBpmProcessExpressionService.queryById(id));
    }


    /**
     * 新增BPM 流程达式
     */
    @Log(title = "BPM 流程达式", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    @PreAuthorize("@pms.hasPermission('bpm:manage:expression:saveOrUpdate')")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BpmProcessExpressionBo bo) {
        return R.ok(iBpmProcessExpressionService.insertByBo(bo));
    }


    /**
     * 修改BPM 流程达式
     */
    @Log(title = "BPM 流程达式", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @PreAuthorize("@pms.hasPermission('bpm:manage:expression:saveOrUpdate')")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BpmProcessExpressionBo bo) {
        return R.ok(iBpmProcessExpressionService.updateByBo(bo));
    }


    /**
     * 删除BPM 流程达式
     *
     * @param ids 主键串
     */
    @Log(title = "BPM 流程达式", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("@pms.hasPermission('bpm:manage:expression:delete')")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iBpmProcessExpressionService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}

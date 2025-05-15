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
import com.fly.bpm.api.domain.vo.BpmFormVo;
import com.fly.bpm.api.domain.bo.BpmFormBo;
import com.fly.bpm.common.service.IBpmFormService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * BPM 表单定义控制器
 *
 * @author fly
 * @date 2024-11-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/form")
public class BpmFormController extends BaseController {

    private final IBpmFormService iBpmFormService;


    /**
     * 表单定义分页列表
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('bpm:manage:form:list')")
    public R<PageVo<BpmFormVo>> page(BpmFormBo bo, PageBo page) {
        return R.ok(iBpmFormService.queryPageList(bo, page));
    }


    /**
     * 表单定义列表
     */
    @GetMapping("/list")
    public R<List<BpmFormVo>> list(BpmFormBo bo) {
        return R.ok(iBpmFormService.queryList(bo));
    }


    /**
     * 获取BPM 表单定义详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<BpmFormVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iBpmFormService.queryById(id));
    }


    /**
     * 新增BPM 表单定义
     */
    @Log(title = "BPM 表单定义", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    @PreAuthorize("@pms.hasPermission('bpm:manage:form:saveOrUpdate')")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BpmFormBo bo) {
        return R.ok(iBpmFormService.insertByBo(bo));
    }


    /**
     * 修改BPM 表单定义
     */
    @Log(title = "BPM 表单定义", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    @PreAuthorize("@pms.hasPermission('bpm:manage:form:saveOrUpdate')")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BpmFormBo bo) {
        return R.ok(iBpmFormService.updateByBo(bo));
    }


    /**
     * 删除BPM 表单定义
     *
     * @param ids 主键串
     */
    @Log(title = "BPM 表单定义", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    @PreAuthorize("@pms.hasPermission('bpm:manage:form:delete')")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iBpmFormService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 导出BPM 表单定义列表
     */
    @Log(title = "BPM 表单定义", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BpmFormBo bo, HttpServletResponse response) {
        List<BpmFormVo> list = iBpmFormService.queryList(bo);
        ExcelUtil.exportExcel(list, "BPM 表单定义", BpmFormVo.class, response);
    }
}

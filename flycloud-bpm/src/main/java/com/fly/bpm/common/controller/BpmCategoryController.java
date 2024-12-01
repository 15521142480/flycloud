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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.bpm.api.domain.vo.BpmCategoryVo;
import com.fly.bpm.api.domain.bo.BpmCategoryBo;
import com.fly.bpm.common.service.IBpmCategoryService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * BPM 流程分类控制器
 *
 * @author fly
 * @date 2024-11-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class BpmCategoryController extends BaseController {

    private final IBpmCategoryService iBpmCategoryService;


    /**
     * 流程分类分页列表
     */
    @GetMapping("/page")
    public R<PageVo<BpmCategoryVo>> pageList(BpmCategoryBo bo, PageBo page) {
        return R.ok(iBpmCategoryService.queryPageList(bo, page));
    }

    /**
     * 流程分类列表
     */
    @GetMapping("/list")
    public R<List<BpmCategoryVo>> list(BpmCategoryBo bo) {
        return R.ok(iBpmCategoryService.queryList(bo));
    }


    /**
     * 导出BPM 流程分类列表
     */
    @Log(title = "BPM 流程分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BpmCategoryBo bo, HttpServletResponse response) {
        List<BpmCategoryVo> list = iBpmCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "BPM 流程分类", BpmCategoryVo.class, response);
    }


    /**
     * 获取BPM 流程分类详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<BpmCategoryVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iBpmCategoryService.queryById(id));
    }


    /**
     * 新增BPM 流程分类
     */
    @Log(title = "BPM 流程分类", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BpmCategoryBo bo) {
        return R.ok(iBpmCategoryService.insertByBo(bo));
    }


    /**
     * 修改BPM 流程分类
     */
    @Log(title = "BPM 流程分类", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BpmCategoryBo bo) {
        return R.ok(iBpmCategoryService.updateByBo(bo));
    }


    /**
     * 删除BPM 流程分类
     *
     * @param ids 主键串
     */
    @Log(title = "BPM 流程分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iBpmCategoryService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}

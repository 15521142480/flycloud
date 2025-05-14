package com.fly.system.controller;

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
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.domain.bo.SysDeptBo;
import com.fly.system.service.ISysDeptService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 部门控制器
 *
 * @author fly
 * @date 2024-12-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dept")
public class SysDeptController extends BaseController {

    private final ISysDeptService iSysDeptService;


    /**
     * 查询部门列表
     */
    @GetMapping("/list")
    public R<PageVo<SysDeptVo>> list(SysDeptBo bo, PageBo page) {
        return R.ok(iSysDeptService.queryPageList(bo, page));
    }

    /**
     * 查询所有部门精简版
     */
    @GetMapping("/getList")
    public R<List<SysDeptVo>> allListSimple(SysDeptBo bo) {
        return R.ok(iSysDeptService.queryList(bo));
    }

    /**
     * 获取部门详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<SysDeptVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysDeptService.queryById(id));
    }


    /**
     * 新增/修改部门
     */
    @Log(title = "部门", businessType = BusinessType.INSERT)
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@Validated(AddGroup.class) @RequestBody SysDeptBo bo) {
        return R.ok(iSysDeptService.saveOrUpdate(bo));
    }



    /**
     * 删除部门
     *
     * @param ids 主键串
     */
    @Log(title = "部门", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysDeptService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 导出部门列表
     */
    @Log(title = "部门", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysDeptBo bo, HttpServletResponse response) {
        List<SysDeptVo> list = iSysDeptService.queryList(bo);
        ExcelUtil.exportExcel(list, "部门", SysDeptVo.class, response);
    }


}

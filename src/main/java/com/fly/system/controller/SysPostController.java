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
import com.fly.system.api.domain.vo.SysPostVo;
import com.fly.system.api.domain.bo.SysPostBo;
import com.fly.system.service.ISysPostService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 岗位控制器
 *
 * @author fly
 * @date 2026-03-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class SysPostController extends BaseController {

    private final ISysPostService iSysPostService;


    /**
     * 查询岗位分页列表
     */
    @GetMapping("/page")
    public R<PageVo<SysPostVo>> page(SysPostBo bo, PageBo page) {
        return R.ok(iSysPostService.queryPageList(bo, page));
    }

    /**
     * 获取岗位详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<SysPostVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysPostService.queryById(id));
    }


    /**
     * 新增岗位
     */
    @Log(title = "岗位", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysPostBo bo) {
        return R.ok(iSysPostService.insertByBo(bo));
    }


    /**
     * 修改岗位
     */
    @Log(title = "岗位", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysPostBo bo) {
        return R.ok(iSysPostService.updateByBo(bo));
    }


    /**
     * 删除岗位
     *
     * @param ids 主键串
     */
    @Log(title = "岗位", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysPostService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 导出岗位列表
     */
    @Log(title = "岗位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysPostBo bo, HttpServletResponse response) {
        List<SysPostVo> list = iSysPostService.queryList(bo);
        ExcelUtil.exportExcel(list, "岗位", SysPostVo.class, response);
    }

}

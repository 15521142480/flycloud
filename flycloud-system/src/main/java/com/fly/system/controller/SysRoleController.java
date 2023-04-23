package com.fly.system.controller;

import com.fly.common.annotation.Log;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.enums.BusinessType;
import com.fly.common.model.R;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.system.domain.bo.SysRoleBo;
import com.fly.system.domain.vo.SysRoleVo;
import com.fly.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 角色信息控制器
 *
 * @author fly
 * @date 2023-04-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    private final ISysRoleService iSysRoleService;


    /**
     * 查询角色信息列表
     */
    @GetMapping("/list")
    public R<PageVo<SysRoleVo>> list(SysRoleBo bo, PageBo page) {
        return R.ok(iSysRoleService.queryPageList(bo, page));
    }


    /**
     * 导出角色信息列表
     */
    @Log(title = "角色信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysRoleBo bo, HttpServletResponse response) {
        List<SysRoleVo> list = iSysRoleService.queryList(bo);
        ExcelUtil.exportExcel(list, "角色信息", SysRoleVo.class, response);
    }


    /**
     * 获取角色信息详细信息
     *
     * @param roleId 主键
     */
    @GetMapping("/{roleId}")
    public R<SysRoleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long roleId) {
        return R.ok(iSysRoleService.queryById(roleId));
    }


    /**
     * 新增角色信息
     */
    @Log(title = "角色信息", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysRoleBo bo) {
        return R.ok(iSysRoleService.insertByBo(bo));
    }


    /**
     * 修改角色信息
     */
    @Log(title = "角色信息", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysRoleBo bo) {
        return R.ok(iSysRoleService.updateByBo(bo));
    }


    /**
     * 删除角色信息
     *
     * @param roleIds 主键串
     */
    @Log(title = "角色信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] roleIds) {
        return R.ok(iSysRoleService.deleteWithValidByIds(Arrays.asList(roleIds), true));
    }
}

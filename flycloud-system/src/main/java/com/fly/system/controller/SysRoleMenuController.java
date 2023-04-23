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
import com.fly.system.domain.bo.SysRoleMenuBo;
import com.fly.system.domain.vo.SysRoleMenuVo;
import com.fly.system.service.ISysRoleMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 角色和菜单关联控制器
 *
 * @author fly
 * @date 2023-04-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/roleMenu")
public class SysRoleMenuController extends BaseController {

    private final ISysRoleMenuService iSysRoleMenuService;


    /**
     * 查询角色和菜单关联列表
     */
    @GetMapping("/list")
    public R<PageVo<SysRoleMenuVo>> list(SysRoleMenuBo bo, PageBo page) {
        return R.ok(iSysRoleMenuService.queryPageList(bo, page));
    }


    /**
     * 导出角色和菜单关联列表
     */
    @Log(title = "角色和菜单关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysRoleMenuBo bo, HttpServletResponse response) {
        List<SysRoleMenuVo> list = iSysRoleMenuService.queryList(bo);
        ExcelUtil.exportExcel(list, "角色和菜单关联", SysRoleMenuVo.class, response);
    }


    /**
     * 获取角色和菜单关联详细信息
     *
     * @param roleId 主键
     */
    @GetMapping("/{roleId}")
    public R<SysRoleMenuVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long roleId) {
        return R.ok(iSysRoleMenuService.queryById(roleId));
    }


    /**
     * 新增角色和菜单关联
     */
    @Log(title = "角色和菜单关联", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysRoleMenuBo bo) {
        return R.ok(iSysRoleMenuService.insertByBo(bo));
    }


    /**
     * 修改角色和菜单关联
     */
    @Log(title = "角色和菜单关联", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysRoleMenuBo bo) {
        return R.ok(iSysRoleMenuService.updateByBo(bo));
    }


    /**
     * 删除角色和菜单关联
     *
     * @param roleIds 主键串
     */
    @Log(title = "角色和菜单关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] roleIds) {
        return R.ok(iSysRoleMenuService.deleteWithValidByIds(Arrays.asList(roleIds), true));
    }
}

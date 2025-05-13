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
import com.fly.system.api.domain.vo.SysRoleMenuVo;
import com.fly.system.api.domain.bo.SysRoleMenuBo;
import com.fly.system.service.ISysRoleMenuService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 角色菜单权限控制器
 *
 * @author fly
 * @date 2025-05-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/roleMenu")
public class SysRoleMenuController extends BaseController {

    private final ISysRoleMenuService iSysRoleMenuService;


    /**
     * 查询角色菜单权限分页列表
     */
    @GetMapping("/page")
    public R<PageVo<SysRoleMenuVo>> page(SysRoleMenuBo bo, PageBo page) {
        return R.ok(iSysRoleMenuService.queryPageList(bo, page));
    }

    /**
     * 获取角色菜单权限详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<SysRoleMenuVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysRoleMenuService.queryById(id));
    }


    /**
     * 新增角色菜单权限
     */
    @Log(title = "角色菜单权限", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysRoleMenuBo bo) {
        return R.ok(iSysRoleMenuService.insertByBo(bo));
    }


    /**
     * 修改角色菜单权限
     */
    @Log(title = "角色菜单权限", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysRoleMenuBo bo) {
        return R.ok(iSysRoleMenuService.updateByBo(bo));
    }


    /**
     * 删除角色菜单权限
     *
     * @param ids 主键串
     */
    @Log(title = "角色菜单权限", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysRoleMenuService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 导出角色菜单权限列表
     */
    @Log(title = "角色菜单权限", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysRoleMenuBo bo, HttpServletResponse response) {
        List<SysRoleMenuVo> list = iSysRoleMenuService.queryList(bo);
        ExcelUtil.exportExcel(list, "角色菜单权限", SysRoleMenuVo.class, response);
    }

}

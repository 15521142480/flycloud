package com.fly.system.controller;

import com.fly.common.domain.entity.LoginUser;
import com.fly.common.utils.auth.SecurityUtils;
import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.annotation.Log;
import com.fly.common.enums.BusinessType;
import com.fly.common.domain.model.R;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.system.api.domain.vo.SysMenuTreeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.system.api.domain.vo.SysRoleVo;
import com.fly.system.api.domain.bo.SysRoleBo;
import com.fly.system.service.ISysRoleService;

import java.util.List;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 角色控制器
 *
 * @author fly
 * @date 2024-08-31
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    private final ISysRoleService iSysRoleService;


    /**
     * 查询角色列表
     */
    @PreAuthorize("@pms.hasPermission('sys:role:list')")
    @GetMapping("/list")
    public R<PageVo<SysRoleVo>> list(SysRoleBo bo, PageBo page) {
        return R.ok(iSysRoleService.queryPageList(bo, page));
    }

    @PostMapping("/getList")
    public R<List<SysRoleVo>> queryList(@RequestBody SysRoleBo bo) {
        return R.ok(iSysRoleService.queryList(bo));
    }

    /**
     * 查询角色菜单列表 - 树型
     */
//    @GetMapping("/getRoleTreeList")
    @GetMapping("/getRoleMenuTreeList/{roleId}")
    public R<List<SysMenuTreeVo>> getRoleMenuTreeList(@PathVariable Long roleId) {
        return R.ok(iSysRoleService.getRoleMenuTreeList(roleId));
    }


    /**
     * 获取角色详细信息
     *
     * @param id 主键
     */
    @GetMapping("/get/{id}")
    public R<SysRoleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysRoleService.queryById(id));
    }


    /**
     * 新增/修改角色
     */
    @PreAuthorize("@pms.hasPermission('sys:role:saveOrUpdate')")
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@Validated @RequestBody SysRoleBo bo, HttpServletRequest request) {

        LoginUser loginUser = SecurityUtils.getCurUser(request);
        return R.ok(iSysRoleService.saveOrUpdate(bo));
    }

    /**
     * 修改角色信息
     */
    @PreAuthorize("@pms.hasPermission('sys.role.saveOrUpdate')")
    @PostMapping("/updateById")
    public R<Void> updateById(@Validated @RequestBody SysRoleBo bo, HttpServletRequest request) {

        LoginUser loginUser = SecurityUtils.getCurUser(request);
        return R.ok(iSysRoleService.updateById(bo));
    }

    /**
     * 修改角色菜单权限
     */
    @PreAuthorize("@pms.hasPermission('sys:role:menuPermission')")
    @PostMapping("/updateMenuPermission")
    public R<Void> updateMenuPermission(@Validated @RequestBody SysRoleBo bo, HttpServletRequest request) {

        return R.ok(iSysRoleService.updateMenuPermission(bo));
    }

//    /**
//     * 修改角色数据权限
//     */
////    @PreAuthorize("@pms.hasPermission('sys:role:dataPermission')")
//    @PostMapping("/updateDataPermission")
//    public R<Void> updateDataPermission(@Validated @RequestBody SysRoleBo bo, HttpServletRequest request) {
//
//        return R.ok(iSysRoleService.updateDataPermission(bo));
//    }


    /**
     * 删除角色
     *
     * @param ids 主键串
     */
    @PreAuthorize("@pms.hasPermission('sys:role:delete')")
    @Log(title = "角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysRoleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }




    /**
     * 导出角色列表
     */
    @Log(title = "角色", businessType = BusinessType.EXPORT)
    @PreAuthorize("@pms.hasPermission('sys:role:download')")
    @PostMapping("/export")
    public void export(SysRoleBo bo, HttpServletResponse response) {
        List<SysRoleVo> list = iSysRoleService.queryList(bo);
        ExcelUtil.exportExcel(list, "角色", SysRoleVo.class, response);
    }
}

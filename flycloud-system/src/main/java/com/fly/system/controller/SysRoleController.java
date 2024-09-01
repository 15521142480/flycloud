package com.fly.system.controller;

import com.fly.common.entity.LoginUser;
import com.fly.common.utils.SecurityUtils;
import com.fly.common.validate.AddGroup;
import com.fly.common.validate.EditGroup;
import com.fly.common.database.web.controller.BaseController;
import com.fly.common.utils.ExcelUtil;
import com.fly.common.annotation.Log;
import com.fly.common.enums.BusinessType;
import com.fly.common.model.R;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.system.api.domain.bo.SysMenuBo;
import com.fly.system.api.domain.vo.SysMenuTreeVo;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/list")
    public R<PageVo<SysRoleVo>> list(SysRoleBo bo, PageBo page) {
        return R.ok(iSysRoleService.queryPageList(bo, page));
    }


    /**
     * 查询角色菜单权限列表 - 树型
     */
//    @GetMapping("/getRoleTreeList/{id}")
    @GetMapping("/getRoleTreeList")
    public R<List<SysMenuTreeVo>> getRoleTreeList(@RequestParam(required = false) Long id) {
        return R.ok(iSysRoleService.getRoleTreeList(id));
    }


    /**
     * 新增/修改角色
     */
    @PostMapping("/saveOrUpdate")
    public R<Void> saveOrUpdate(@Validated @RequestBody SysRoleBo bo, HttpServletRequest request) {

        LoginUser loginUser = SecurityUtils.getCurUser(request);
        return R.ok(iSysRoleService.saveOrUpdate(bo));
    }

    /**
     * 修改角色信息
     */
    @PostMapping("/updateById")
    public R<Void> updateById(@Validated @RequestBody SysRoleBo bo, HttpServletRequest request) {

        LoginUser loginUser = SecurityUtils.getCurUser(request);
        return R.ok(iSysRoleService.updateById(bo));
    }



    /**
     * 导出角色列表
     */
    @Log(title = "角色", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysRoleBo bo, HttpServletResponse response) {
        List<SysRoleVo> list = iSysRoleService.queryList(bo);
        ExcelUtil.exportExcel(list, "角色", SysRoleVo.class, response);
    }


    /**
     * 获取角色详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<SysRoleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iSysRoleService.queryById(id));
    }


    /**
     * 新增角色
     */
    @Log(title = "角色", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysRoleBo bo) {
        return R.ok(iSysRoleService.insertByBo(bo));
    }


    /**
     * 修改角色
     */
    @Log(title = "角色", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysRoleBo bo) {
        return R.ok(iSysRoleService.updateByBo(bo));
    }


    /**
     * 删除角色
     *
     * @param ids 主键串
     */
    @Log(title = "角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysRoleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}

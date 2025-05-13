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
import com.fly.system.api.domain.vo.SysUserRoleVo;
import com.fly.system.api.domain.bo.SysUserRoleBo;
import com.fly.system.service.ISysUserRoleService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户角色控制器
 *
 * @author fly
 * @date 2025-05-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/userRole")
public class SysUserRoleController extends BaseController {

    private final ISysUserRoleService iSysUserRoleService;


    /**
     * 查询用户角色分页列表
     */
//    @GetMapping("/page")
//    public R<PageVo<SysUserRoleVo>> page(SysUserRoleBo bo, PageBo page) {
//        return R.ok(iSysUserRoleService.queryPageList(bo, page));
//    }



    /**
     * 获取用户角色列表
     *
     * @param userId 用户id
     */
//    @GetMapping("/getListByUserId/{userId}")
//    public R<List<SysUserRoleVo>> getListByUserId(@NotNull(message = "用户id不能为空") @PathVariable Long userId) {
//        return R.ok(iSysUserRoleService.queryListByUserId(userId));
//    }

    /**
     * 获取用户角色id列表
     *
     * @param userId 用户id
     */
    @GetMapping("/getRoleIdsByUserId/{userId}")
    public R<List<Long>> getRoleIdsByUserId(@NotNull(message = "用户id不能为空") @PathVariable Long userId) {
        return R.ok(iSysUserRoleService.queryRoleIdsByUserId(userId));
    }


    /**
     * 修改用户角色信息
     */
    @Log(title = "用户角色", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysUserRoleBo bo) {
        return R.ok(iSysUserRoleService.updateByBo(bo));
    }



    /**
     * 删除用户角色
     *
     * @param ids 主键串
     */
    @Log(title = "用户角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return R.ok(iSysUserRoleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}

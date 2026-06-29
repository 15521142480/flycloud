package com.fly.system.controller;

import com.fly.common.database.web.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fly.system.service.ISysRoleMenuService;

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
     * 获取角色菜单权限信息列表
     *
     * @param roleId 角色id
     */
//    @GetMapping("/getListByRoleId/{roleId}")
//    public R<List<SysRoleMenuVo>> getListByRoleId(@NotNull(message = "主键不能为空") @PathVariable Long roleId) {
//        return R.ok(iSysRoleMenuService.queryListByRoleId(roleId));
//    }



}

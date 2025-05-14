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
     * 获取角色菜单权限信息列表
     *
     * @param roleId 角色id
     */
//    @GetMapping("/getListByRoleId/{roleId}")
//    public R<List<SysRoleMenuVo>> getListByRoleId(@NotNull(message = "主键不能为空") @PathVariable Long roleId) {
//        return R.ok(iSysRoleMenuService.queryListByRoleId(roleId));
//    }



}

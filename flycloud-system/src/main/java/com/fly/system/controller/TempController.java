package com.fly.system.controller;

import com.fly.common.database.web.controller.BaseController;
import com.fly.common.model.R;
import com.fly.system.service.*;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 临时
 *
 * @author: lxs
 * @date: 2024/8/13
 */
//@Validated
@RestController
@RequestMapping("/temp")
@AllArgsConstructor
public class TempController extends BaseController {

//    private final ISysUserService sysUserService;
//    private final ISysDepartService sysDepartService;
//    private final ISysDictService sysDictService;
//    private final ISysMenuService sysMenuService;
//    private final ISysRolePermissionService sysRolePermissionService;
//    private final ISysRoleService sysRoleService;

    @GetMapping("/test")
    public R<String> list() {
        return R.ok("testOk");
    }


    @GetMapping("/need")
    public R<String> need() {
        return R.ok("这是一个需要权限访问的接口，由此看到此消息已证明权限已无问题。");
    }


}

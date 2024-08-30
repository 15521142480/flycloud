package com.fly.system.controller;

import com.fly.common.database.web.controller.BaseController;
import com.fly.common.model.R;
import com.fly.system.service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 - 控制层
 *
 * @author: lxs
 * @date: 2024/8/13
 */
//@Validated
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController extends BaseController {

    private final ISysUserService sysUserService;

    @PostMapping("/getUserList")
    public R<String> getUserList() {

        return R.ok("getUserList");
    }

}
